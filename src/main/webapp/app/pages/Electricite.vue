<template>
  <div class="page theme-electricite">
    <div class="page-header">
      <h2 class="page-title">Électricité</h2>
      <p class="page-subtitle">Consommation et production électrique</p>
    </div>
    <div class="kpi-row">
      <KpiCard
        label="Puissance actuelle"
        :value="puissanceDisplay"
        unit="kW"
        :trend="puissanceTrend"
        :status="puissanceStatus"
        :trendLabel="puissanceTrendLabel"
      />
      <KpiCard
        label="Consommation (jour)"
        :value="consoDisplay"
        unit="kWh"
        trend="flat"
        status="neutral"
        trendLabel="Cumul session"
      />
      <SignalCard :snrValue="rt.elecLive.snr" />
    </div>
    <div class="grid">
      <ChartCard title="Consommation totale (kWh)" type="line" :data="currentConsommation" />
      <ChartCard title="Pic de consommation" type="bar" :data="currentPic" />
      <div class="heatmap-card">
        <div class="heatmap-header">
          <span class="heatmap-title">CONSOMMATION PAR HEURE &amp; JOUR</span>
          <span class="live-dot"></span>
        </div>
        <div class="heatmap-legend">
          <span class="legend-label">Faible</span>
          <div class="legend-bar"></div>
          <span class="legend-label">Élevée</span>
        </div>
        <div class="heatmap-body">
          <ElectriciteHeatmap />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import ChartCard from '../components/ChartCard.vue'
import KpiCard from '../components/KpiCard.vue'
import SignalCard from '../components/SignalCard.vue'
import ElectriciteHeatmap from '../components/charts/ElectriciteHeatmap.vue'
import { useFilterStore } from '../stores/filter.js'
import { useRealtimeStore } from '../stores/realtime.js'
import { buildHistoricalData } from '../composables/useChartHistory.js'

const filterStore = useFilterStore()
const rt = useRealtimeStore()

// ── Puissance ────────────────────────────────────────────────────────
const puissanceDisplay = computed(() => {
  const v = rt.elecLive.puissance.value
  return v !== null ? v.toFixed(0) : '—'
})

const puissanceTrend = computed(() => {
  const { value, prev } = rt.elecLive.puissance
  if (value === null || prev === null) return 'flat'
  return value > prev ? 'up' : value < prev ? 'down' : 'flat'
})

const puissanceStatus = computed(() => {
  const v = rt.elecLive.puissance.value
  if (v === null) return 'neutral'
  if (v >= 1500) return 'bad'
  if (v >= 1000) return 'warn'
  return 'good'
})

const puissanceTrendLabel = computed(() => {
  const { value, prev } = rt.elecLive.puissance
  if (value === null) return 'En attente…'
  if (prev === null) return 'Première mesure'
  const pct = ((value - prev) / Math.abs(prev) * 100).toFixed(1)
  const sign = pct > 0 ? '+' : ''
  return `${sign}${pct}%  ·  réf: ${prev.toFixed(0)} kW`
})

// ── Consommation ──────────────────────────────────────────────────────
const consoDisplay = computed(() => {
  const v = rt.elecLive.consommation.value
  return v > 0 ? v.toLocaleString('fr-FR', { maximumFractionDigits: 0 }) : '0'
})

const LABELS = {
  heure:   ['00h','02h','04h','06h','08h','10h','12h','14h','16h','18h','20h','22h'],
  jour:    ['Lun','Mar','Mer','Jeu','Ven','Sam','Dim'],
  semaine: ['Sem 1','Sem 2','Sem 3','Sem 4'],
  mois:    ['Jan','Fév','Mar','Avr','Mai','Jun','Jul','Aoû','Sep','Oct','Nov','Déc'],
}

const consommationData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'kWh', data: [80,60,55,70,120,280,350,340,310,360,480,300], borderColor:'#f5a623', backgroundColor:'rgba(245,166,35,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'kWh', data: [420,380,450,410,390,310,280], borderColor:'#f5a623', backgroundColor:'rgba(245,166,35,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'kWh', data: [2800,3100,2950,3200], borderColor:'#f5a623', backgroundColor:'rgba(245,166,35,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'MWh', data: [1800,1650,1500,1350,1200,1100,1050,1080,1250,1450,1650,1900], borderColor:'#f5a623', backgroundColor:'rgba(245,166,35,0.12)', fill:true, tension:0.4 }] },
}

const picData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'kWh', data: [55,40,35,60,140,310,380,360,330,390,510,320], backgroundColor:'#f5a623', borderRadius:6 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'kWh', data: [480,350,420,395,440,260,210], backgroundColor:'#f5a623', borderRadius:6 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'kWh', data: [520,490,510,480], backgroundColor:'#f5a623', borderRadius:6 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'MWh', data: [210,195,180,165,145,130,125,130,155,175,200,220], backgroundColor:'#f5a623', borderRadius:6 }] },
}

const p = () => filterStore.selectedPeriod
const h = () => filterStore.historyLevels

const currentConsommation = computed(() => buildHistoricalData(consommationData[p()], h(), p(), 'line'))
const currentPic          = computed(() => buildHistoricalData(picData[p()],          h(), p(), 'bar'))
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

.theme-electricite { --accent: #f59e0b; }

.theme-electricite::before {
  content: '';
  position: absolute;
  top: -120px;
  left: 50%;
  transform: translateX(-50%);
  width: 700px;
  height: 400px;
  background: radial-gradient(ellipse, rgba(245, 158, 11, 0.28) 0%, transparent 70%);
  pointer-events: none;
}

.page-header { position: relative; padding: 84px 28px 28px; }
.page-title { margin: 0 0 4px; font-size: 1.8rem; font-weight: 700; color: #f59e0b; letter-spacing: -0.01em; display: flex; align-items: center; gap: 12px; }
.page-title::before { content: ''; width: 12px; height: 12px; border-radius: 50%; background: #22c55e; flex-shrink: 0; animation: dot-pulse 2s ease-in-out infinite; }
.page-subtitle { margin: 0; font-size: 0.85rem; color: rgba(255, 255, 255, 0.45); }

@keyframes dot-pulse {
  0%, 100% { opacity: 1; box-shadow: 0 0 0 0 rgba(34,197,94,0.4); }
  50% { opacity: 0.6; box-shadow: 0 0 0 4px rgba(34,197,94,0); }
}

.grid {
  position: relative;
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr auto;
  gap: 20px;
  padding: 0 28px 28px;
}

/* ── Heatmap card ── */
.heatmap-card {
  grid-column: 1 / -1;
  background: rgba(255, 255, 255, 0.82);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  border-radius: 16px;
  padding: 22px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.18), 0 1px 0 rgba(255,255,255,0.8) inset;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  overflow: hidden;
}

.heatmap-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 36px rgba(0, 0, 0, 0.25), 0 1px 0 rgba(255,255,255,0.8) inset;
}

.heatmap-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(to right, color-mix(in srgb, #f59e0b 12%, transparent), transparent 70%);
  margin: -22px -22px 0;
  padding: 14px 22px 12px;
  border-radius: 15px 15px 0 0;
}

.heatmap-title {
  font-size: 0.72rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.09em;
  color: rgba(0, 0, 0, 0.4);
}

.live-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #22c55e;
  box-shadow: 0 0 6px #22c55e;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}

.heatmap-legend {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0 4px;
}

.legend-label {
  font-size: 0.65rem;
  font-weight: 600;
  color: rgba(0, 0, 0, 0.35);
  text-transform: uppercase;
  letter-spacing: 0.06em;
  white-space: nowrap;
}

.legend-bar {
  flex: 1;
  height: 8px;
  border-radius: 6px;
  background: linear-gradient(
    to right,
    rgb(40, 20, 5),
    rgb(120, 60, 10),
    rgb(180, 95, 10),
    rgb(220, 130, 10),
    rgb(245, 158, 11)
  );
  opacity: 0.85;
}

.heatmap-body {
  flex: 1;
  min-height: 200px;
  height: 230px;
}
</style>
