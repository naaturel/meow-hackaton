<template>
  <div class="page theme-electricite">
    <div class="page-header">
      <h2 class="page-title">Électricité</h2>
      <p class="page-subtitle">Consommation et production électrique</p>
    </div>
    <div class="grid">
      <ChartCard title="Consommation totale (kWh)" type="line" :data="currentConsommation" />
      <ChartCard title="Pic de consommation" type="bar" :data="currentPic" />
      <ChartCard title="Répartition par zone" type="doughnut" :data="currentRepartition" />
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

const repartitionData = {
  heure:   { labels: ['Résidentiel','Industriel','Commercial','Transport'], datasets: [{ data:[40,28,22,10], backgroundColor:['#f5a623','#c97d10','#7c4a00','#3d2400'], borderWidth:0 }] },
  jour:    { labels: ['Résidentiel','Industriel','Commercial','Transport'], datasets: [{ data:[38,30,22,10], backgroundColor:['#f5a623','#c97d10','#7c4a00','#3d2400'], borderWidth:0 }] },
  semaine: { labels: ['Résidentiel','Industriel','Commercial','Transport'], datasets: [{ data:[35,33,24,8],  backgroundColor:['#f5a623','#c97d10','#7c4a00','#3d2400'], borderWidth:0 }] },
  mois:    { labels: ['Résidentiel','Industriel','Commercial','Transport'], datasets: [{ data:[36,31,23,10], backgroundColor:['#f5a623','#c97d10','#7c4a00','#3d2400'], borderWidth:0 }] },
}

const evolutionData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'kWh', data: [75,58,52,68,115,275,345,335,305,355,475,295], borderColor:'#f5a623', backgroundColor:'rgba(245,166,35,0.1)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'kWh', data: [410,370,440,400,380,300,270], borderColor:'#f5a623', backgroundColor:'rgba(245,166,35,0.1)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'kWh', data: [2750,3050,2900,3150], borderColor:'#f5a623', backgroundColor:'rgba(245,166,35,0.1)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'MWh', data: [1780,1630,1480,1330,1180,1080,1030,1060,1230,1430,1630,1880], borderColor:'#f5a623', backgroundColor:'rgba(245,166,35,0.1)', fill:true, tension:0.4 }] },
}

const p = () => filterStore.selectedPeriod
const h = () => filterStore.historyLevels

const currentConsommation = computed(() => buildHistoricalData(consommationData[p()], h(), p(), 'line'))
const currentPic          = computed(() => buildHistoricalData(picData[p()],          h(), p(), 'bar'))
const currentRepartition  = computed(() => buildHistoricalData(repartitionData[p()],  h(), p(), 'doughnut'))
const currentEvolution    = computed(() => buildHistoricalData(evolutionData[p()],    h(), p(), 'line'))
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
  background: radial-gradient(ellipse, rgba(245, 158, 11, 0.18) 0%, transparent 70%);
  pointer-events: none;
}

.page-header { position: relative; padding: 84px 28px 28px; }
.page-title { margin: 0 0 4px; font-size: 1.8rem; font-weight: 700; color: #f59e0b; letter-spacing: -0.01em; display: flex; align-items: center; gap: 12px; }
.page-title::before { content: ''; width: 12px; height: 12px; border-radius: 50%; background: #22c55e; flex-shrink: 0; animation: dot-pulse 2s ease-in-out infinite; }
.page-subtitle { margin: 0; font-size: 0.85rem; color: rgba(255, 255, 255, 0.45); }

.grid {
  position: relative;
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 20px;
  padding: 0 28px 28px;
}

.grid :deep(.chart-card) { border-left: 3px solid #f59e0b; }
</style>
