<template>
  <div class="page theme-gaz">
    <div class="page-header">
      <h2 class="page-title">Gaz</h2>
      <p class="page-subtitle">Pression, débit et consommation gaz</p>
    </div>
    <div class="grid">
      <ChartCard title="Consommation totale (m³)" type="line" :data="currentConsommation" />
      <ChartCard title="Pression réseau (bar)" type="line" :data="currentPression" />
      <ChartCard title="Répartition par zone" type="doughnut" :data="currentRepartition" />
      <ChartCard title="Évolution sur la période" type="bar" :data="currentEvolution" />
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
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'm³', data: [95,80,70,85,120,180,210,195,185,190,200,150], borderColor:'#7c3aed', backgroundColor:'rgba(124,58,237,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'm³', data: [1400,1250,1350,1500,1450,900,800], borderColor:'#7c3aed', backgroundColor:'rgba(124,58,237,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'm³', data: [9800,10500,9200,10100], borderColor:'#7c3aed', backgroundColor:'rgba(124,58,237,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'm³', data: [42000,38000,32000,22000,14000,9000,7500,8000,15000,26000,35000,44000], borderColor:'#7c3aed', backgroundColor:'rgba(124,58,237,0.12)', fill:true, tension:0.4 }] },
}

const pressionData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'bar', data: [4.2,4.0,3.9,4.1,4.5,5.2,5.8,5.6,5.4,5.5,5.7,4.8], borderColor:'#a855f7', backgroundColor:'rgba(168,85,247,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'bar', data: [4.8,4.5,5.0,5.3,5.1,4.2,4.0], borderColor:'#a855f7', backgroundColor:'rgba(168,85,247,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'bar', data: [4.6,5.1,4.8,5.2], borderColor:'#a855f7', backgroundColor:'rgba(168,85,247,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'bar', data: [5.5,5.3,5.0,4.8,4.5,4.2,4.0,4.1,4.5,5.0,5.2,5.6], borderColor:'#a855f7', backgroundColor:'rgba(168,85,247,0.12)', fill:true, tension:0.4 }] },
}

const repartitionData = {
  heure:   { labels: ['Résidentiel','Industriel','Chauffage','Commercial'], datasets: [{ data:[35,28,25,12], backgroundColor:['#7c3aed','#3b0764','#a855f7','#6d28d9'], borderWidth:0 }] },
  jour:    { labels: ['Résidentiel','Industriel','Chauffage','Commercial'], datasets: [{ data:[38,25,26,11], backgroundColor:['#7c3aed','#3b0764','#a855f7','#6d28d9'], borderWidth:0 }] },
  semaine: { labels: ['Résidentiel','Industriel','Chauffage','Commercial'], datasets: [{ data:[40,22,28,10], backgroundColor:['#7c3aed','#3b0764','#a855f7','#6d28d9'], borderWidth:0 }] },
  mois:    { labels: ['Résidentiel','Industriel','Chauffage','Commercial'], datasets: [{ data:[42,20,30,8],  backgroundColor:['#7c3aed','#3b0764','#a855f7','#6d28d9'], borderWidth:0 }] },
}

const evolutionData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'm³', data: [92,78,68,82,116,174,204,190,180,184,194,145], backgroundColor:'#7c3aed', borderRadius:6 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'm³', data: [1360,1210,1310,1460,1410,870,770], backgroundColor:'#7c3aed', borderRadius:6 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'm³', data: [9500,10200,8900,9800], backgroundColor:'#7c3aed', borderRadius:6 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'm³', data: [40000,36000,30000,20000,12000,7500,6000,6500,13000,24000,33000,42000], backgroundColor:'#7c3aed', borderRadius:6 }] },
}

const p = () => filterStore.selectedPeriod
const h = () => filterStore.historyLevels

const currentConsommation = computed(() => buildHistoricalData(consommationData[p()], h(), p(), 'line'))
const currentPression     = computed(() => buildHistoricalData(pressionData[p()],     h(), p(), 'line'))
const currentRepartition  = computed(() => buildHistoricalData(repartitionData[p()],  h(), p(), 'doughnut'))
const currentEvolution    = computed(() => buildHistoricalData(evolutionData[p()],    h(), p(), 'bar'))
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

.theme-gaz { --accent: #8b5cf6; }

.theme-gaz::before {
  content: '';
  position: absolute;
  top: -120px;
  left: 50%;
  transform: translateX(-50%);
  width: 700px;
  height: 400px;
  background: radial-gradient(ellipse, rgba(139, 92, 246, 0.18) 0%, transparent 70%);
  pointer-events: none;
}

.page-header { position: relative; padding: 84px 28px 28px; }
.page-title { margin: 0 0 4px; font-size: 1.8rem; font-weight: 700; color: #8b5cf6; letter-spacing: -0.01em; display: flex; align-items: center; gap: 12px; }
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

.grid :deep(.chart-card) { border-left: 3px solid #8b5cf6; }
</style>
