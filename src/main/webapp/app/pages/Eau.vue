<template>
  <div class="page theme-eau">
    <div class="page-header">
      <h2 class="page-title">Eau</h2>
      <p class="page-subtitle">Consommation et qualité de l'eau</p>
    </div>
    <div class="grid">
      <ChartCard title="Consommation totale (m³)" type="line" :data="currentConsommation" />
      <ChartCard title="Débit moyen (L/s)" type="line" :data="currentDebit" />
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
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'm³', data: [120,90,75,80,150,280,350,320,300,310,280,200], borderColor:'#1a6fbf', backgroundColor:'rgba(26,111,191,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'm³', data: [1800,1650,1900,2100,2000,1400,1200], borderColor:'#1a6fbf', backgroundColor:'rgba(26,111,191,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'm³', data: [12500,13200,11800,12900], borderColor:'#1a6fbf', backgroundColor:'rgba(26,111,191,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'm³', data: [52000,48000,50000,55000,60000,65000,70000,68000,60000,54000,50000,48000], borderColor:'#1a6fbf', backgroundColor:'rgba(26,111,191,0.12)', fill:true, tension:0.4 }] },
}

const debitData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'L/s', data: [8,6,5,6,10,18,22,20,19,20,18,13], borderColor:'#0c2d5e', backgroundColor:'rgba(12,45,94,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'L/s', data: [12,10,14,16,15,9,8], borderColor:'#0c2d5e', backgroundColor:'rgba(12,45,94,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'L/s', data: [11,13,10,12], borderColor:'#0c2d5e', backgroundColor:'rgba(12,45,94,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'L/s', data: [10,9,10,11,12,13,14,13,12,11,10,9], borderColor:'#0c2d5e', backgroundColor:'rgba(12,45,94,0.12)', fill:true, tension:0.4 }] },
}

const repartitionData = {
  heure:   { labels: ['Résidentiel','Industriel','Agriculture','Commercial'], datasets: [{ data:[40,25,20,15], backgroundColor:['#1a6fbf','#0c2d5e','#38bdf8','#0ea5e9'], borderWidth:0 }] },
  jour:    { labels: ['Résidentiel','Industriel','Agriculture','Commercial'], datasets: [{ data:[42,22,22,14], backgroundColor:['#1a6fbf','#0c2d5e','#38bdf8','#0ea5e9'], borderWidth:0 }] },
  semaine: { labels: ['Résidentiel','Industriel','Agriculture','Commercial'], datasets: [{ data:[38,28,20,14], backgroundColor:['#1a6fbf','#0c2d5e','#38bdf8','#0ea5e9'], borderWidth:0 }] },
  mois:    { labels: ['Résidentiel','Industriel','Agriculture','Commercial'], datasets: [{ data:[35,20,32,13], backgroundColor:['#1a6fbf','#0c2d5e','#38bdf8','#0ea5e9'], borderWidth:0 }] },
}

const evolutionData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'm³', data: [115,88,72,78,145,272,340,310,290,300,272,195], backgroundColor:'#1a6fbf', borderRadius:6 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'm³', data: [1750,1600,1850,2050,1950,1350,1150], backgroundColor:'#1a6fbf', borderRadius:6 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'm³', data: [12200,12900,11500,12600], backgroundColor:'#1a6fbf', borderRadius:6 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'm³', data: [50000,46000,48000,53000,58000,63000,68000,66000,58000,52000,48000,46000], backgroundColor:'#1a6fbf', borderRadius:6 }] },
}

const p = () => filterStore.selectedPeriod
const h = () => filterStore.historyLevels

const currentConsommation = computed(() => buildHistoricalData(consommationData[p()], h(), p(), 'line'))
const currentDebit        = computed(() => buildHistoricalData(debitData[p()],        h(), p(), 'line'))
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

.grid {
  position: relative;
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 20px;
  padding: 0 28px 28px;
}

</style>
