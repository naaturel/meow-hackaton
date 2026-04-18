import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

const API_BASE_URL = 'http://localhost:8081'
const SSE_URL = '/api/sse/subscribe'

// Derive trend direction from current vs previous value
function trend(current, previous) {
  if (previous == null || current === previous) return 'flat'
  return current > previous ? 'up' : 'down'
}

// Derive status from value relative to thresholds { warn, bad }
function status(value, thresholds) {
  if (thresholds.bad != null && value >= thresholds.bad) return 'bad'
  if (thresholds.warn != null && value >= thresholds.warn) return 'warn'
  if (thresholds.goodBelow != null && value <= thresholds.goodBelow) return 'good'
  return 'neutral'
}

// IoT device routing
const ELEC_POWER_DEVICES = new Set(['Compteur electrique TGBT', 'Serpont solaire', 'Compteur electrique solaire'])
const ELEC_SNR_DEVICE    = 'Compteur electrique TGBT'
const EAU_DEVICES        = new Set(['Serpont eau compteur new', 'Compteur eau ancien atelier'])
const EAU_SNR_DEVICE     = 'Serpont eau compteur new'
const INVALID_VALUE      = -2.1474836e9

function isValidValue(v) {
  return v != null && Math.abs(v - INVALID_VALUE) > 1e4
}

export const useRealtimeStore = defineStore('realtime', () => {
  let es = null

  // ── Connection state ──────────────────────────────────────────────
  const connected = ref(false)
  const error = ref(null)

  // ── Électricité live (IoT) ────────────────────────────────────────
  const elecLive = ref({
    puissance:    { value: null, prev: null },
    consommation: { value: 0 },
    snr:          null,
  })

  // ── Eau live (IoT) ───────────────────────────────────────────────
  const eauLive = ref({
    debit:        { value: null, prev: null },
    consommation: { value: 0 },
    snr:          null,
  })

  // ── Électricité ───────────────────────────────────────────────────
  const elec = ref({
    puissance:   { value: 480,  unit: 'kW',  prev: null },
    consommation:{ value: 460,  unit: 'kWh', prev: null },
    signal:      { value: 7.0,              prev: null },
  })

  const elecKpi = computed(() => ({
    puissance: {
      value: elec.value.puissance.value,
      unit: 'kW',
      trend: trend(elec.value.puissance.value, elec.value.puissance.prev),
      status: status(elec.value.puissance.value, { warn: 450, bad: 550 }),
      trendLabel: trendLabel(elec.value.puissance.value, elec.value.puissance.prev, 'kW'),
    },
    consommation: {
      value: elec.value.consommation.value,
      unit: 'kWh',
      trend: trend(elec.value.consommation.value, elec.value.consommation.prev),
      status: 'neutral',
      trendLabel: trendLabel(elec.value.consommation.value, elec.value.consommation.prev, 'kWh'),
    },
    signal: elec.value.signal.value,
  }))

  // ── Eau ───────────────────────────────────────────────────────────
  const eau = ref({
    debit:       { value: 22,   unit: 'L/s',  prev: null },
    consommation:{ value: 2100, unit: 'm³',   prev: null },
    qualite:     { value: 98,   unit: '/100', prev: null },
  })

  const eauKpi = computed(() => ({
    debit: {
      value: eau.value.debit.value,
      unit: 'L/s',
      trend: trend(eau.value.debit.value, eau.value.debit.prev),
      status: status(eau.value.debit.value, { warn: 30, bad: 40, goodBelow: 25 }),
      trendLabel: trendLabel(eau.value.debit.value, eau.value.debit.prev, 'L/s'),
    },
    consommation: {
      value: eau.value.consommation.value.toLocaleString('fr-FR'),
      unit: 'm³',
      trend: trend(eau.value.consommation.value, eau.value.consommation.prev),
      status: eau.value.consommation.value < eau.value.consommation.prev ? 'good' : 'neutral',
      trendLabel: trendLabel(eau.value.consommation.value, eau.value.consommation.prev, 'm³'),
    },
    qualite: {
      value: eau.value.qualite.value,
      unit: '/100',
      trend: trend(eau.value.qualite.value, eau.value.qualite.prev),
      status: status(eau.value.qualite.value, { warn: 80, bad: 60, goodBelow: 101 }),
      trendLabel: qualiteLabel(eau.value.qualite.value),
    },
  }))

  // ── Gaz ───────────────────────────────────────────────────────────
  const gaz = ref({
    pression:    { value: 5.8,  unit: 'bar',  prev: null },
    consommation:{ value: 1400, unit: 'm³',   prev: null },
    debit:       { value: 18.5, unit: 'm³/h', prev: null },
  })

  const gazKpi = computed(() => ({
    pression: {
      value: gaz.value.pression.value,
      unit: 'bar',
      trend: trend(gaz.value.pression.value, gaz.value.pression.prev),
      status: status(gaz.value.pression.value, { warn: 6.5, bad: 7.5 }),
      trendLabel: trendLabel(gaz.value.pression.value, gaz.value.pression.prev, 'bar'),
    },
    consommation: {
      value: gaz.value.consommation.value.toLocaleString('fr-FR'),
      unit: 'm³',
      trend: trend(gaz.value.consommation.value, gaz.value.consommation.prev),
      status: gaz.value.consommation.value < gaz.value.consommation.prev ? 'good' : 'neutral',
      trendLabel: trendLabel(gaz.value.consommation.value, gaz.value.consommation.prev, 'm³'),
    },
    debit: {
      value: gaz.value.debit.value,
      unit: 'm³/h',
      trend: trend(gaz.value.debit.value, gaz.value.debit.prev),
      status: status(gaz.value.debit.value, { warn: 22, bad: 28 }),
      trendLabel: trendLabel(gaz.value.debit.value, gaz.value.debit.prev, 'm³/h'),
    },
  }))

  // ── Helpers ───────────────────────────────────────────────────────
  function trendLabel(current, prev, unit) {
    if (prev == null) return 'Chargement…'
    const diff = current - prev
    if (diff === 0) return `= vs précédent`
    const sign = diff > 0 ? '+' : ''
    return `${sign}${diff} ${unit} vs précédent`
  }

  function qualiteLabel(v) {
    if (v >= 95) return 'Excellent'
    if (v >= 80) return 'Bon'
    if (v >= 60) return 'Moyen'
    return 'Mauvais'
  }

  // ── SSE handlers ─────────────────────────────────────────────────
  function applyEvent(domain, field, newValue) {
    const store = domain === 'electricite' ? elec
                : domain === 'eau'         ? eau
                : domain === 'gaz'         ? gaz
                : null
    if (!store || store.value[field] == null) return
    store.value[field].prev  = store.value[field].value
    store.value[field].value = newValue
  }

  // Expected SSE event format:
  //   event: electricite
  //   data: {"puissance": 492, "consommation": 461, "signal": 7.1}
  //
  //   event: eau
  //   data: {"debit": 23.5, "consommation": 2110, "qualite": 97}
  //
  //   event: gaz
  //   data: {"pression": 5.9, "consommation": 1405, "debit": 18.8}
  //
  // A generic "message" event with {domain, field, value} is also accepted.

  function handleDomainEvent(domain, data) {
    Object.entries(data).forEach(([field, value]) => {
      applyEvent(domain, field, value)
    })
  }

  // ── Connect / disconnect ──────────────────────────────────────────
  function connect() {
    if (es) return

    console.log("sse conencted")

    es = new EventSource(API_BASE_URL + SSE_URL)

    es.addEventListener('open', () => {
      connected.value = true
      error.value = null
    })

    es.addEventListener('message', (e) => {
      try {
        const msg = JSON.parse(e.data)
        console.log(e.data)
        // IoT device format: { device_name, value, snr, rssi, time, ... }
        if (msg.device_name) {
          if (ELEC_POWER_DEVICES.has(msg.device_name) && isValidValue(msg.value)) {
            elecLive.value.puissance.prev  = elecLive.value.puissance.value
            elecLive.value.puissance.value = Math.round(msg.value * 10) / 10
            elecLive.value.consommation.value += msg.value
          }
          if (msg.device_name === ELEC_SNR_DEVICE && msg.snr != null) {
            elecLive.value.snr = msg.snr
          }
          if (EAU_DEVICES.has(msg.device_name) && isValidValue(msg.value)) {
            const litres = msg.value * 10
            eauLive.value.debit.prev  = eauLive.value.debit.value
            eauLive.value.debit.value = Math.round(litres * 10) / 10
            eauLive.value.consommation.value += litres
          }
          if (msg.device_name === EAU_SNR_DEVICE && msg.snr != null) {
            eauLive.value.snr = msg.snr
          }
          return
        }

        // Legacy domain/field/value format
        const { domain, field, value } = msg
        applyEvent(domain, field, value)
      } catch {
        // ignore unparseable messages
      }
    })

    es.addEventListener('error', () => {
      connected.value = false
      error.value = 'Connexion SSE perdue — reconnexion en cours…'
    })

    // Typed domain events
    ;['electricite', 'eau', 'gaz'].forEach(domain => {
      es.addEventListener(domain, (e) => {
        try {
          handleDomainEvent(domain, JSON.parse(e.data))
        } catch {
          console.warn(`[realtime] parse error on event "${domain}"`, e.data)
        }
      })
    })
  }

  function disconnect() {
    es?.close()

     console.log("sse disconnected")
    es = null
    connected.value = false
  }

  return {
    connected, error,
    elecLive,
    eauLive,
    elec, elecKpi,
    eau,  eauKpi,
    gaz,  gazKpi,
    connect, disconnect,
  }
})
