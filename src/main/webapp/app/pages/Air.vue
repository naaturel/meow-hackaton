<template>
  <div class="page theme-air">
    <div class="page-header">
      <h2 class="page-title">Qualité de l'air</h2>
      <p class="page-subtitle">Particules, CO2 et indice de qualité</p>
    </div>
    <div class="grid">
      <ChartCard title="Indice de qualité de l'air (IQA)" type="line" :data="currentIqa" />
      <ChartCard title="Concentration CO2 (ppm)" type="line" :data="currentCo2" />
      <ChartCard title="Particules fines PM2.5 (µg/m³)" type="bar" :data="currentPm25" />
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

const iqaData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'IQA', data: [42,38,35,40,55,72,85,90,88,75,60,48], borderColor:'#16a34a', backgroundColor:'rgba(22,163,74,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'IQA', data: [55,48,62,70,65,40,38], borderColor:'#16a34a', backgroundColor:'rgba(22,163,74,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'IQA', data: [52,65,70,58], borderColor:'#16a34a', backgroundColor:'rgba(22,163,74,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'IQA', data: [45,48,55,60,65,70,75,72,65,58,50,42], borderColor:'#16a34a', backgroundColor:'rgba(22,163,74,0.12)', fill:true, tension:0.4 }] },
}

const co2Data = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'ppm', data: [395,390,388,392,410,430,450,445,440,425,415,400], borderColor:'#65a30d', backgroundColor:'rgba(101,163,13,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'ppm', data: [410,405,420,435,425,398,392], borderColor:'#65a30d', backgroundColor:'rgba(101,163,13,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'ppm', data: [408,418,425,412], borderColor:'#65a30d', backgroundColor:'rgba(101,163,13,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'ppm', data: [400,402,408,415,420,425,428,426,418,412,405,398], borderColor:'#65a30d', backgroundColor:'rgba(101,163,13,0.12)', fill:true, tension:0.4 }] },
}

const pm25Data = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'µg/m³', data: [8,6,5,7,12,18,22,25,24,20,15,10], backgroundColor:'#16a34a', borderRadius:6 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'µg/m³', data: [14,12,18,22,20,8,7], backgroundColor:'#16a34a', borderRadius:6 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'µg/m³', data: [15,20,22,17], backgroundColor:'#16a34a', borderRadius:6 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'µg/m³', data: [18,20,16,12,10,8,7,8,11,15,18,20], backgroundColor:'#16a34a', borderRadius:6 }] },
}

const evolutionData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'IQA', data: [40,36,33,38,52,68,80,86,84,72,58,45], borderColor:'#4ade80', backgroundColor:'rgba(74,222,128,0.1)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'IQA', data: [52,45,58,66,62,38,35], borderColor:'#4ade80', backgroundColor:'rgba(74,222,128,0.1)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'IQA', data: [49,62,67,55], borderColor:'#4ade80', backgroundColor:'rgba(74,222,128,0.1)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'IQA', data: [42,45,52,57,62,67,72,69,62,55,47,39], borderColor:'#4ade80', backgroundColor:'rgba(74,222,128,0.1)', fill:true, tension:0.4 }] },
}

const p = () => filterStore.selectedPeriod
const h = () => filterStore.historyLevels

const currentIqa       = computed(() => buildHistoricalData(iqaData[p()],       h(), p(), 'line'))
const currentCo2       = computed(() => buildHistoricalData(co2Data[p()],       h(), p(), 'line'))
const currentPm25      = computed(() => buildHistoricalData(pm25Data[p()],      h(), p(), 'bar'))
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

.theme-air {
  --accent: #22c55e;
}

.theme-air::before {
  content: '';
  position: absolute;
  top: -120px;
  left: 50%;
  transform: translateX(-50%);
  width: 700px;
  height: 400px;
  background: radial-gradient(ellipse, rgba(34, 197, 94, 0.28) 0%, transparent 70%);
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
  color: #22c55e;
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
