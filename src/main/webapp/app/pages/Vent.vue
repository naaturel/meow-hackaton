<template>
  <div class="page theme-vent">
    <div class="page-header">
      <h2 class="page-title">Vent</h2>
      <p class="page-subtitle">Vitesse, direction et rafales</p>
    </div>
    <div class="grid">
      <ChartCard title="Vitesse moyenne (km/h)" type="line" :data="currentVitesse" />
      <ChartCard title="Rose des vents" type="doughnut" :data="currentDirection" />
      <ChartCard title="Rafales maximales (km/h)" type="bar" :data="currentRafales" />
      <ChartCard title="Évolution sur la période" type="line" :data="currentEvolution" />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import ChartCard from '../components/ChartCard.vue'
import { useFilterStore } from '../stores/filter.js'
import { buildHistoricalData } from '../composables/useChartHistory.js'

const filterStore = useFilterStore()

const LABELS = {
  heure:   ['00h','02h','04h','06h','08h','10h','12h','14h','16h','18h','20h','22h'],
  jour:    ['Lun','Mar','Mer','Jeu','Ven','Sam','Dim'],
  semaine: ['Sem 1','Sem 2','Sem 3','Sem 4'],
  mois:    ['Jan','Fév','Mar','Avr','Mai','Jun','Jul','Aoû','Sep','Oct','Nov','Déc'],
}

const vitesseData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'km/h', data: [18,15,12,14,20,28,35,32,30,38,42,25], borderColor:'#0ea5e9', backgroundColor:'rgba(14,165,233,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'km/h', data: [22,18,30,25,35,40,28], borderColor:'#0ea5e9', backgroundColor:'rgba(14,165,233,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'km/h', data: [24,31,27,35], borderColor:'#0ea5e9', backgroundColor:'rgba(14,165,233,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'km/h', data: [38,35,30,25,20,18,16,17,22,28,33,40], borderColor:'#0ea5e9', backgroundColor:'rgba(14,165,233,0.12)', fill:true, tension:0.4 }] },
}

const directionData = {
  heure:   { labels: ['N','NE','E','SE','S','SO','O','NO'], datasets: [{ data:[15,8,5,10,20,18,12,12], backgroundColor:['#0ea5e9','#38bdf8','#7dd3fc','#bae6fd','#0284c7','#0369a1','#075985','#0c4a6e'], borderWidth:0 }] },
  jour:    { labels: ['N','NE','E','SE','S','SO','O','NO'], datasets: [{ data:[12,6,8,14,22,20,10,8],  backgroundColor:['#0ea5e9','#38bdf8','#7dd3fc','#bae6fd','#0284c7','#0369a1','#075985','#0c4a6e'], borderWidth:0 }] },
  semaine: { labels: ['N','NE','E','SE','S','SO','O','NO'], datasets: [{ data:[10,5,6,12,25,22,12,8],  backgroundColor:['#0ea5e9','#38bdf8','#7dd3fc','#bae6fd','#0284c7','#0369a1','#075985','#0c4a6e'], borderWidth:0 }] },
  mois:    { labels: ['N','NE','E','SE','S','SO','O','NO'], datasets: [{ data:[14,7,9,13,18,16,14,9],  backgroundColor:['#0ea5e9','#38bdf8','#7dd3fc','#bae6fd','#0284c7','#0369a1','#075985','#0c4a6e'], borderWidth:0 }] },
}

const rafalesData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'km/h', data: [32,28,22,25,38,55,68,60,55,72,80,48], backgroundColor:'#0ea5e9', borderRadius:6 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'km/h', data: [45,38,62,52,70,85,58], backgroundColor:'#0ea5e9', borderRadius:6 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'km/h', data: [55,70,60,80], backgroundColor:'#0ea5e9', borderRadius:6 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'km/h', data: [75,70,60,50,40,35,30,32,45,58,65,80], backgroundColor:'#0ea5e9', borderRadius:6 }] },
}

const evolutionData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'km/h', data: [16,13,10,12,18,26,33,30,28,36,40,23], borderColor:'#38bdf8', backgroundColor:'rgba(56,189,248,0.1)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'km/h', data: [20,16,28,23,33,38,26], borderColor:'#38bdf8', backgroundColor:'rgba(56,189,248,0.1)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'km/h', data: [22,29,25,33], borderColor:'#38bdf8', backgroundColor:'rgba(56,189,248,0.1)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'km/h', data: [36,33,28,23,18,16,14,15,20,26,31,38], borderColor:'#38bdf8', backgroundColor:'rgba(56,189,248,0.1)', fill:true, tension:0.4 }] },
}

const p = () => filterStore.selectedPeriod
const h = () => filterStore.historyLevels

const currentVitesse   = computed(() => buildHistoricalData(vitesseData[p()],   h(), p(), 'line'))
const currentDirection = computed(() => buildHistoricalData(directionData[p()], h(), p(), 'doughnut'))
const currentRafales   = computed(() => buildHistoricalData(rafalesData[p()],   h(), p(), 'bar'))
const currentEvolution = computed(() => buildHistoricalData(evolutionData[p()], h(), p(), 'line'))
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

.theme-vent {
  --accent: #06b6d4;
}

.theme-vent::before {
  content: '';
  position: absolute;
  top: -120px;
  left: 50%;
  transform: translateX(-50%);
  width: 700px;
  height: 400px;
  background: radial-gradient(ellipse, rgba(6, 182, 212, 0.18) 0%, transparent 70%);
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
  color: #06b6d4;
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
  border-left: 3px solid #06b6d4;
}
</style>
