<template>
  <div class="page theme-temperature">
    <div class="page-header">
      <h2 class="page-title">Température</h2>
      <p class="page-subtitle">Relevés thermiques et anomalies</p>
    </div>
    <div class="grid">
      <ChartCard title="Température moyenne (°C)" type="line" :data="currentMoyenne" />
      <ChartCard title="Température par zone (°C)" type="bar" :data="currentZones" />
      <ChartCard title="Écarts par rapport à la normale" type="bar" :data="currentEcarts" />
      <ChartCard title="Historique mensuel (°C)" type="line" :data="currentHistorique" />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import ChartCard from '../components/ChartCard.vue'
import { useFilterStore } from '../stores/filter.js'

const filterStore = useFilterStore()

const LABELS = {
  heure:   ['00h','02h','04h','06h','08h','10h','12h','14h','16h','18h','20h','22h'],
  jour:    ['Lun','Mar','Mer','Jeu','Ven','Sam','Dim'],
  semaine: ['Sem 1','Sem 2','Sem 3','Sem 4'],
  mois:    ['Jan','Fév','Mar','Avr','Mai','Jun','Jul','Aoû','Sep','Oct','Nov','Déc'],
}
const ZONES = ['Zone A','Zone B','Zone C','Zone D']

const moyenneData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: '°C', data: [12,11,10,10,11,14,17,19,21,20,18,15], borderColor:'#dc2626', backgroundColor:'rgba(220,38,38,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: '°C', data: [14,16,13,17,15,19,18], borderColor:'#dc2626', backgroundColor:'rgba(220,38,38,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: '°C', data: [13,15,17,16], borderColor:'#dc2626', backgroundColor:'rgba(220,38,38,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: '°C', data: [3,4,8,12,17,21,24,23,19,14,8,4], borderColor:'#dc2626', backgroundColor:'rgba(220,38,38,0.12)', fill:true, tension:0.4 }] },
}

const zonesData = {
  heure:   { labels: ZONES, datasets: [{ label: '°C', data: [18,22,15,20], backgroundColor:['#dc2626','#b91c1c','#7f1d1d','#ef4444'], borderRadius:6 }] },
  jour:    { labels: ZONES, datasets: [{ label: '°C', data: [16,20,13,18], backgroundColor:['#dc2626','#b91c1c','#7f1d1d','#ef4444'], borderRadius:6 }] },
  semaine: { labels: ZONES, datasets: [{ label: '°C', data: [15,19,12,17], backgroundColor:['#dc2626','#b91c1c','#7f1d1d','#ef4444'], borderRadius:6 }] },
  mois:    { labels: ZONES, datasets: [{ label: '°C', data: [12,16,9,14],  backgroundColor:['#dc2626','#b91c1c','#7f1d1d','#ef4444'], borderRadius:6 }] },
}

const ecartsData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'Écart (°C)', data: [1,-0.5,0,1.5,2,-1,0.5,2,3,1.5,-0.5,1], backgroundColor:'#ef4444', borderRadius:4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'Écart (°C)', data: [1.2,-0.8,0.5,2.1,-0.3,1.8,0.9], backgroundColor:'#ef4444', borderRadius:4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'Écart (°C)', data: [-0.5,1.2,2.0,0.8], backgroundColor:'#ef4444', borderRadius:4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'Écart (°C)', data: [-1,0.5,-0.5,1,2,1.5,3,2.5,1,-0.5,-1,0.5], backgroundColor:'#ef4444', borderRadius:4 }] },
}

const historiqueData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: '°C', data: [11,10,9,9,10,13,16,18,20,19,17,14], borderColor:'#ef4444', backgroundColor:'rgba(239,68,68,0.1)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: '°C', data: [13,15,12,16,14,18,17], borderColor:'#ef4444', backgroundColor:'rgba(239,68,68,0.1)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: '°C', data: [12,14,16,15], borderColor:'#ef4444', backgroundColor:'rgba(239,68,68,0.1)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: '°C', data: [2,3,7,11,16,20,23,22,18,13,7,3], borderColor:'#ef4444', backgroundColor:'rgba(239,68,68,0.1)', fill:true, tension:0.4 }] },
}

const currentMoyenne    = computed(() => moyenneData[filterStore.selectedPeriod])
const currentZones      = computed(() => zonesData[filterStore.selectedPeriod])
const currentEcarts     = computed(() => ecartsData[filterStore.selectedPeriod])
const currentHistorique = computed(() => historiqueData[filterStore.selectedPeriod])
</script>

<style scoped>
.page { min-height: 100vh; padding: 0; display: flex; flex-direction: column; }
.theme-temperature { background: linear-gradient(160deg, #1a0000 0%, #7f1d1d 40%, #dc2626 100%); }
.page-header { padding: 84px 28px 28px; }
.page-title { margin: 0 0 4px; font-size: 1.6rem; font-weight: 700; color: #fff; }
.page-subtitle { margin: 0; font-size: 0.85rem; color: rgba(255,255,255,0.6); }
.grid { flex: 1; display: grid; grid-template-columns: 1fr 1fr; grid-template-rows: 1fr 1fr; gap: 20px; padding: 0 28px 28px; }
</style>
