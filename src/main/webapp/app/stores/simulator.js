import { ref } from 'vue'
import { defineStore } from 'pinia'

function clamp(v, lo, hi) { return Math.max(lo, Math.min(hi, v)) }

// Small random step biased toward 0 (mean-reverting)
function step(v, target, jitter, lo, hi) {
  const pull = (target - v) * 0.08        // gentle pull toward target
  const noise = (Math.random() - 0.5) * jitter * 2
  return clamp(Math.round((v + pull + noise) * 10) / 10, lo, hi)
}

const DIRS_8 = ['N','NE','E','SE','S','SO','O','NO']
function degToDir(deg) {
  return DIRS_8[Math.round(((deg % 360) + 360) % 360 / 45) % 8]
}

export const useSimulatorStore = defineStore('simulator', () => {
  // ── Température ──────────────────────────────────────────────────
  // April Liège: ~12°C mean, realistic range 8–18°C
  const temp = ref({ current: 13.5, max: 16.2, min: 9.8, initialized: false })

  function initTemp(apiCurrent) {
    if (!temp.value.initialized && typeof apiCurrent === 'number') {
      temp.value.current     = apiCurrent
      temp.value.max         = Math.round((apiCurrent + 3.5) * 10) / 10
      temp.value.min         = Math.round((apiCurrent - 3.5) * 10) / 10
      temp.value.initialized = true
    }
  }

  // ── Vent ─────────────────────────────────────────────────────────
  // Starting: moderate westerly wind, SO direction
  const vent = ref({ vitesse: 32, rafale: 54, dirDeg: 225 })

  // ── Air ──────────────────────────────────────────────────────────
  const air = ref({ iqa: 42, co2: 415, pm25: 8.0 })

  // ── Industriel ───────────────────────────────────────────────────
  const indus = ref({ alertes: 2, taux: 72 })

  // ── Simulation timers ────────────────────────────────────────────
  // Temperature: every 15s
  const tTemp = setInterval(() => {
    const c = step(temp.value.current, 13.0, 0.4, 5, 32)
    temp.value.current = c
    if (c > temp.value.max) temp.value.max = c
    if (c < temp.value.min) temp.value.min = c
  }, 15_000)

  // Wind: every 2.5s — compass visibly animates
  const tVent = setInterval(() => {
    const v = Math.round(clamp(vent.value.vitesse + (Math.random() - 0.48) * 3, 8, 90))
    vent.value.vitesse = v
    vent.value.rafale = Math.round(clamp(v * (1.55 + Math.random() * 0.4), v + 2, 120))
    vent.value.dirDeg = ((vent.value.dirDeg + (Math.random() * 12 - 6)) + 360) % 360
  }, 2_500)

  // Air: every 20s
  const tAir = setInterval(() => {
    air.value.iqa  = Math.round(clamp(air.value.iqa  + (Math.random() - 0.5) * 4,  15, 150))
    air.value.co2  = Math.round(clamp(air.value.co2  + (Math.random() - 0.5) * 2, 385, 450))
    air.value.pm25 = Math.round(clamp(air.value.pm25 + (Math.random() - 0.5) * 1,   2,  35) * 10) / 10
  }, 20_000)

  // Industrial: every 12s
  const tIndus = setInterval(() => {
    indus.value.taux = Math.round(clamp(indus.value.taux + (Math.random() - 0.5) * 2, 55, 98))
    if (Math.random() < 0.3) {
      indus.value.alertes = Math.round(clamp(indus.value.alertes + (Math.random() > 0.55 ? 1 : -1), 0, 8))
    }
  }, 12_000)

  function stopAll() {
    clearInterval(tTemp)
    clearInterval(tVent)
    clearInterval(tAir)
    clearInterval(tIndus)
  }

  return { temp, vent, air, indus, initTemp, stopAll, degToDir }
})
