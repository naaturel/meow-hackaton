<template>
  <div class="page theme-eau">
    <div class="page-header">
      <h2 class="page-title">Eau</h2>
      <p class="page-subtitle">Consommation et qualité de l'eau</p>
    </div>
    <div class="kpi-row">
      <KpiCard
        label="Débit actuel"
        :value="debitDisplay"
        unit="L"
        :trend="debitTrend"
        :status="debitStatus"
        :trendLabel="debitTrendLabel"
      />
      <KpiCard
        label="Consommation (jour)"
        :value="consoDisplay"
        unit="L"
        trend="flat"
        status="neutral"
        trendLabel="Cumul session"
      />
      <SignalCard :snrValue="rt.eauLive.snr" />
    </div>
    <div class="grid">
      <ChartCard title="Consommation totale (m³)" type="line" :data="currentConsommation" />
      <ChartCard title="Débit moyen (L/s)" type="line" :data="currentDebit" />
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
          <EauHeatmap />
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
import EauHeatmap from '../components/charts/EauHeatmap.vue'
import { useFilterStore } from '../stores/filter.js'
import { useRealtimeStore } from '../stores/realtime.js'
import { buildHistoricalData } from '../composables/useChartHistory.js'

const filterStore = useFilterStore()
const rt = useRealtimeStore()

// ── Débit ────────────────────────────────────────────────────────────
const debitDisplay = computed(() => {
  const v = rt.eauLive.debit.value
  return v !== null ? v.toFixed(0) : '—'
})

const debitTrend = computed(() => {
  const { value, prev } = rt.eauLive.debit
  if (value === null || prev === null) return 'flat'
  return value > prev ? 'up' : value < prev ? 'down' : 'flat'
})

const debitStatus = computed(() => {
  const v = rt.eauLive.debit.value
  if (v === null) return 'neutral'
  if (v >= 400) return 'bad'
  if (v >= 250) return 'warn'
  return 'good'
})

const debitTrendLabel = computed(() => {
  const { value, prev } = rt.eauLive.debit
  if (value === null) return 'En attente…'
  if (prev === null) return 'Première mesure'
  const pct = ((value - prev) / Math.abs(prev) * 100).toFixed(1)
  const sign = pct > 0 ? '+' : ''
  return `${sign}${pct}%  ·  réf: ${prev.toFixed(0)} L`
})

// ── Consommation ─────────────────────────────────────────────────────
const consoDisplay = computed(() => {
  const v = rt.eauLive.consommation.value
  return v > 0 ? v.toLocaleString('fr-FR', { maximumFractionDigits: 0 }) : '0'
})

// ── Charts statiques ─────────────────────────────────────────────────
const LABELS = {
  heure:   ['00h','02h','04h','06h','08h','10h','12h','14h','16h','18h','20h','22h'],
  jour:    ['Lun','Mar','Mer','Jeu','Ven','Sam','Dim'],
  semaine: ['Sem 1','Sem 2','Sem 3','Sem 4'],
  mois:    ['Jan','Fév','Mar','Avr','Mai','Jun','Jul','Aoû','Sep','Oct','Nov','Déc'],
}

const consommationData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'm³', data: [120,90,75,80,150,280,350,320,300,310,280,200], borderColor:'#3b82f6', backgroundColor:'rgba(59,130,246,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'm³', data: [1800,1650,1900,2100,2000,1400,1200], borderColor:'#3b82f6', backgroundColor:'rgba(59,130,246,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'm³', data: [12500,13200,11800,12900], borderColor:'#3b82f6', backgroundColor:'rgba(59,130,246,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'm³', data: [52000,48000,50000,55000,60000,65000,70000,68000,60000,54000,50000,48000], borderColor:'#3b82f6', backgroundColor:'rgba(59,130,246,0.12)', fill:true, tension:0.4 }] },
}

const debitData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'L/s', data: [8,6,5,6,10,18,22,20,19,20,18,13], borderColor:'#1d4ed8', backgroundColor:'rgba(29,78,216,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'L/s', data: [12,10,14,16,15,9,8], borderColor:'#1d4ed8', backgroundColor:'rgba(29,78,216,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'L/s', data: [11,13,10,12], borderColor:'#1d4ed8', backgroundColor:'rgba(29,78,216,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'L/s', data: [10,9,10,11,12,13,14,13,12,11,10,9], borderColor:'#1d4ed8', backgroundColor:'rgba(29,78,216,0.12)', fill:true, tension:0.4 }] },
}

const p = () => filterStore.selectedPeriod
const h = () => filterStore.historyLevels

const currentConsommation = computed(() => buildHistoricalData(consommationData[p()], h(), p(), 'line'))
const currentDebit        = computed(() => buildHistoricalData(debitData[p()],        h(), p(), 'line'))
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

.theme-eau { --accent: #3b82f6; }

.theme-eau::before {
  content: '';
  position: absolute;
  top: -120px;
  left: 50%;
  transform: translateX(-50%);
  width: 700px;
  height: 400px;
  background: radial-gradient(ellipse, rgba(59, 130, 246, 0.28) 0%, transparent 70%);
  pointer-events: none;
}

.page-header { position: relative; padding: 84px 28px 28px; }
.page-title { margin: 0 0 4px; font-size: 1.8rem; font-weight: 700; color: #3b82f6; letter-spacing: -0.01em; display: flex; align-items: center; gap: 12px; }
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
  background: linear-gradient(to right, color-mix(in srgb, #3b82f6 12%, transparent), transparent 70%);
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
    rgb(5, 20, 50),
    rgb(20, 60, 130),
    rgb(40, 130, 210),
    rgb(59, 130, 246)
  );
  opacity: 0.85;
}

.heatmap-body {
  flex: 1;
  min-height: 200px;
  height: 230px;
}
</style>
