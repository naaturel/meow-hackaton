<template>
  <div class="page theme-temperature">
    <div class="page-header">
      <h2 class="page-title">Température</h2>
      <p class="page-subtitle">Relevés thermiques et anomalies</p>
    </div>

    <div v-if="loading" class="loading">Chargement des données...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <template v-else>
      <div class="kpi-row">
        <KpiCard label="Température actuelle" :value="kpi.current" unit="°C" trend="up" status="warn" :trendLabel="`${kpi.ecartNormale > 0 ? '+' : ''}${kpi.ecartNormale}°C vs normale`" />
        <KpiCard label="Température max aujourd'hui" :value="kpi.maxToday" unit="°C" trend="up" status="bad" trendLabel="Max du jour" />
        <KpiCard label="Amplitude journalière" :value="kpi.amplitude" unit="°C" trend="flat" status="neutral" :trendLabel="`${kpi.minToday}°C → ${kpi.maxToday}°C`" />
      </div>
      <div class="grid">
        <ChartCard title="Température moyenne (°C)" type="line" :data="currentMoyenne" />
        <ChartCard title="Température Max/Min (°C)" type="bar" :data="currentZones" />
        <ChartCard title="Écarts par rapport à la normale" type="bar" :data="currentEcarts" />
        <ChartCard title="Historique mensuel (°C)" type="line" :data="currentHistorique" />
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import ChartCard from '../components/ChartCard.vue'
import KpiCard from '../components/KpiCard.vue'
import { useFilterStore } from '../stores/filter.js'
import { buildHistoricalData } from '../composables/useChartHistory.js'

const filterStore = useFilterStore()
const loading = ref(true)
const error = ref(null)

// --- Données brutes API ---
const hourlyLabels = ref([])
const hourlyTemps  = ref([])
const dailyLabels  = ref([])
const dailyMax     = ref([])
const dailyMin     = ref([])
const dailyMean    = ref([])

// --- KPIs ---
const kpi = ref({ current: '--', maxToday: '--', minToday: '--', amplitude: '--', ecartNormale: '--' })

// --- Fetch Open-Meteo ---
async function fetchMeteo() {
  loading.value = true
  error.value = null
  try {
    const url = new URL('https://api.open-meteo.com/v1/forecast')
    url.searchParams.set('latitude', '50.6337')
    url.searchParams.set('longitude', '5.5675')
    url.searchParams.set('hourly', 'temperature_2m')
    url.searchParams.set('daily', 'temperature_2m_max,temperature_2m_min,temperature_2m_mean')
    url.searchParams.set('past_days', '92')
    url.searchParams.set('forecast_days', '1')
    url.searchParams.set('timezone', 'auto')

    const res = await fetch(url)
    if (!res.ok) throw new Error(`Erreur API: ${res.status}`)
    const data = await res.json()

    // Hourly
    hourlyLabels.value = data.hourly.time.map(t => t.slice(11, 16)) // "HH:MM"
    hourlyTemps.value  = data.hourly.temperature_2m

    // Daily
    dailyLabels.value = data.daily.time.map(t => {
      const d = new Date(t)
      return d.toLocaleDateString('fr-BE', { day: '2-digit', month: 'short' })
    })
    dailyMax.value  = data.daily.temperature_2m_max
    dailyMin.value  = data.daily.temperature_2m_min
    dailyMean.value = data.daily.temperature_2m_mean

    // KPIs — valeurs du jour (dernier daily)
    const last = dailyMax.value.length - 1
    const maxT = Math.round(dailyMax.value[last])
    const minT = Math.round(dailyMin.value[last])
    const meanT = Math.round(dailyMean.value[last])

    // Normale approximative pour Liège selon le mois
    const month = new Date().getMonth()
    const normales = [3, 4, 8, 12, 16, 19, 21, 21, 17, 13, 7, 4]
    const normale = normales[month]

    kpi.value = {
      current:      meanT,
      maxToday:     maxT,
      minToday:     minT,
      amplitude:    maxT - minT,
      ecartNormale: Math.round((meanT - normale) * 10) / 10,
    }
  } catch (e) {
    error.value = `Impossible de charger les données météo: ${e.message}`
  } finally {
    loading.value = false
  }
}

// --- Computed chart data selon la période sélectionnée ---
const p = () => filterStore.selectedPeriod
const h = () => filterStore.historyLevels

// Helpers pour extraire les N derniers éléments
function lastN(arr, n) { return arr.slice(-n) }

const moyenneData = computed(() => {
  const period = p()
  if (period === 'heure') {
    // Dernières 24h — données horaires
    const labels = lastN(hourlyLabels.value, 24)
    const data   = lastN(hourlyTemps.value, 24)
    return { labels, datasets: [{ label: '°C', data, borderColor: '#dc2626', backgroundColor: 'rgba(220,38,38,0.15)', fill: true, tension: 0.4 }] }
  }
  if (period === 'jour') {
    // 7 derniers jours
    const labels = lastN(dailyLabels.value, 7)
    const data   = lastN(dailyMean.value, 7).map(v => Math.round(v * 10) / 10)
    return { labels, datasets: [{ label: '°C', data, borderColor: '#dc2626', backgroundColor: 'rgba(220,38,38,0.15)', fill: true, tension: 0.4 }] }
  }
  if (period === 'semaine') {
    // 4 dernières semaines — moyenne par semaine
    const weeks = [0, 1, 2, 3].map(i => {
      const chunk = dailyMean.value.slice(-(28 - i * 7), -(21 - i * 7) || undefined)
      const avg = chunk.reduce((a, b) => a + b, 0) / (chunk.length || 1)
      return Math.round(avg * 10) / 10
    })
    return { labels: ['Sem -3', 'Sem -2', 'Sem -1', 'Sem actuelle'], datasets: [{ label: '°C', data: weeks, borderColor: '#dc2626', backgroundColor: 'rgba(220,38,38,0.15)', fill: true, tension: 0.4 }] }
  }
  // mois — moyenne mensuelle sur les 3 derniers mois
  const byMonth = {}
  dailyLabels.value.forEach((label, i) => {
    const key = label.slice(3) // "Mois Année" sans le jour
    if (!byMonth[key]) byMonth[key] = []
    byMonth[key].push(dailyMean.value[i])
  })
  const labels = Object.keys(byMonth)
  const data   = labels.map(k => Math.round(byMonth[k].reduce((a, b) => a + b, 0) / byMonth[k].length * 10) / 10)
  return { labels, datasets: [{ label: '°C', data, borderColor: '#dc2626', backgroundColor: 'rgba(220,38,38,0.12)', fill: true, tension: 0.4 }] }
})

const zonesData = computed(() => {
  const period = p()
  const n = period === 'heure' ? 1 : period === 'jour' ? 7 : period === 'semaine' ? 28 : 92
  const labels = lastN(dailyLabels.value, n === 1 ? 1 : n)
  const maxD   = lastN(dailyMax.value, n).map(v => Math.round(v * 10) / 10)
  const minD   = lastN(dailyMin.value, n).map(v => Math.round(v * 10) / 10)
  return {
    labels,
    datasets: [
      { label: 'Max °C', data: maxD, backgroundColor: '#dc2626', borderRadius: 6 },
      { label: 'Min °C', data: minD, backgroundColor: '#7f1d1d', borderRadius: 6 },
    ],
  }
})

const ecartsData = computed(() => {
  const normales = [3, 4, 8, 12, 16, 19, 21, 21, 17, 13, 7, 4]
  const period = p()
  const n = period === 'heure' ? 24 : period === 'jour' ? 7 : period === 'semaine' ? 28 : 92

  if (period === 'heure') {
    const labels = lastN(hourlyLabels.value, 24)
    const month  = new Date().getMonth()
    const data   = lastN(hourlyTemps.value, 24).map(v => Math.round((v - normales[month]) * 10) / 10)
    return { labels, datasets: [{ label: 'Écart (°C)', data, backgroundColor: '#ef4444', borderRadius: 4 }] }
  }

  const labels = lastN(dailyLabels.value, n)
  const data   = lastN(dailyMean.value, n).map((v, i) => {
    // On calcule le mois pour chaque point
    const date  = new Date()
    date.setDate(date.getDate() - (n - 1 - i))
    const month = date.getMonth()
    return Math.round((v - normales[month]) * 10) / 10
  })
  return { labels, datasets: [{ label: 'Écart (°C)', data, backgroundColor: '#ef4444', borderRadius: 4 }] }
})

const currentHistorique = computed(() => buildHistoricalData(moyenneData.value, h(), p(), 'line'))
const currentMoyenne    = computed(() => buildHistoricalData(moyenneData.value, h(), p(), 'line'))
const currentZones      = computed(() => buildHistoricalData(zonesData.value,   h(), p(), 'bar'))
const currentEcarts     = computed(() => buildHistoricalData(ecartsData.value,  h(), p(), 'bar'))

onMounted(fetchMeteo)
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #13131f;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

.theme-temperature {
  --accent: #ef4444;
}

.theme-temperature::before {
  content: '';
  position: absolute;
  top: -120px;
  left: 50%;
  transform: translateX(-50%);
  width: 700px;
  height: 400px;
  background: radial-gradient(ellipse, rgba(239, 68, 68, 0.28) 0%, transparent 70%);
  pointer-events: none;
}

.page-header {
  position: relative;
  padding: 84px 28px 28px;
}

.page-title {
  margin: 0 0 4px;
  font-size: 1.8rem;
  font-weight: 700;
  color: #ef4444;
  letter-spacing: -0.01em;
  display: flex;
  align-items: center;
  gap: 12px;
}
.page-title::before { content: ''; width: 12px; height: 12px; border-radius: 50%; background: #22c55e; flex-shrink: 0; animation: dot-pulse 2s ease-in-out infinite; }

.page-subtitle {
  margin: 0;
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.45);
}

.grid {
  position: relative;
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 20px;
  padding: 0 28px 28px;
}

.grid :deep(.chart-card) {
}
</style>
