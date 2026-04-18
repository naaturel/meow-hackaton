<template>
  <div class="page theme-industriel">
    <div class="page-header">
      <h2 class="page-title">Capteurs industriels</h2>
      <p class="page-subtitle">Lignes de production et supervision usine</p>
    </div>
    <div class="grid">
      <ChartCard title="Charge des lignes (%)" type="bar" :data="currentCharge" />
      <ChartCard title="Alertes capteurs" type="bar" :data="currentAlertes" />
      <ChartCard title="Vibrations et pression" type="line" :data="currentVibrations" />
      <ChartCard title="Temps de fonctionnement" type="doughnut" :data="currentFonctionnement" />
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
const LIGNES = ['Ligne A','Ligne B','Ligne C','Ligne D']

const chargeData = {
  heure:   { labels: LIGNES, datasets: [{ label: '%', data: [78,85,60,92], backgroundColor:['#78716c','#57534e','#44403c','#292524'], borderRadius:6 }] },
  jour:    { labels: LIGNES, datasets: [{ label: '%', data: [82,88,65,88], backgroundColor:['#78716c','#57534e','#44403c','#292524'], borderRadius:6 }] },
  semaine: { labels: LIGNES, datasets: [{ label: '%', data: [80,86,62,90], backgroundColor:['#78716c','#57534e','#44403c','#292524'], borderRadius:6 }] },
  mois:    { labels: LIGNES, datasets: [{ label: '%', data: [79,84,61,89], backgroundColor:['#78716c','#57534e','#44403c','#292524'], borderRadius:6 }] },
}

const alertesData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'Alertes', data: [0,0,1,0,2,1,3,2,1,4,2,1], backgroundColor:'#ef4444', borderRadius:4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'Alertes', data: [3,2,4,1,5,1,0], backgroundColor:'#ef4444', borderRadius:4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'Alertes', data: [8,12,7,10], backgroundColor:'#ef4444', borderRadius:4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'Alertes', data: [22,18,25,15,12,8,6,9,14,20,24,28], backgroundColor:'#ef4444', borderRadius:4 }] },
}

const vibrationsData = {
  heure:   { labels: LABELS.heure,   datasets: [{ label: 'mm/s', data: [1.2,1.0,0.9,1.1,1.5,2.2,2.8,2.6,2.4,2.9,3.2,2.0], borderColor:'#a8a29e', backgroundColor:'rgba(168,162,158,0.15)', fill:true, tension:0.4 }] },
  jour:    { labels: LABELS.jour,    datasets: [{ label: 'mm/s', data: [1.8,1.6,2.0,2.4,2.2,1.2,1.0], borderColor:'#a8a29e', backgroundColor:'rgba(168,162,158,0.15)', fill:true, tension:0.4 }] },
  semaine: { labels: LABELS.semaine, datasets: [{ label: 'mm/s', data: [1.9,2.3,2.0,2.5], borderColor:'#a8a29e', backgroundColor:'rgba(168,162,158,0.15)', fill:true, tension:0.4 }] },
  mois:    { labels: LABELS.mois,    datasets: [{ label: 'mm/s', data: [2.4,2.2,2.0,1.9,1.8,1.6,1.5,1.6,1.8,2.0,2.2,2.5], borderColor:'#a8a29e', backgroundColor:'rgba(168,162,158,0.12)', fill:true, tension:0.4 }] },
}

const fonctionnementData = {
  heure:   { labels: ['En marche','Maintenance','Arrêt','Veille'], datasets: [{ data:[72,8,5,15], backgroundColor:['#44403c','#78716c','#ef4444','#a8a29e'], borderWidth:0 }] },
  jour:    { labels: ['En marche','Maintenance','Arrêt','Veille'], datasets: [{ data:[75,6,4,15], backgroundColor:['#44403c','#78716c','#ef4444','#a8a29e'], borderWidth:0 }] },
  semaine: { labels: ['En marche','Maintenance','Arrêt','Veille'], datasets: [{ data:[70,10,5,15], backgroundColor:['#44403c','#78716c','#ef4444','#a8a29e'], borderWidth:0 }] },
  mois:    { labels: ['En marche','Maintenance','Arrêt','Veille'], datasets: [{ data:[68,12,6,14], backgroundColor:['#44403c','#78716c','#ef4444','#a8a29e'], borderWidth:0 }] },
}

const currentCharge          = computed(() => chargeData[filterStore.selectedPeriod])
const currentAlertes         = computed(() => alertesData[filterStore.selectedPeriod])
const currentVibrations      = computed(() => vibrationsData[filterStore.selectedPeriod])
const currentFonctionnement  = computed(() => fonctionnementData[filterStore.selectedPeriod])
</script>

<style scoped>
.page { min-height: 100vh; padding: 0; display: flex; flex-direction: column; }
.theme-industriel { background: linear-gradient(160deg, #0a0a0a 0%, #1c1917 40%, #44403c 100%); }
.page-header { padding: 84px 28px 28px; }
.page-title { margin: 0 0 4px; font-size: 1.6rem; font-weight: 700; color: #fff; }
.page-subtitle { margin: 0; font-size: 0.85rem; color: rgba(255,255,255,0.6); }
.grid { flex: 1; display: grid; grid-template-columns: 1fr 1fr; grid-template-rows: 1fr 1fr; gap: 20px; padding: 0 28px 28px; }
</style>
