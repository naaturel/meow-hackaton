<template>
  <div class="page theme-industriel">
    <div class="page-header">
      <h2 class="page-title">Capteurs industriels</h2>
      <p class="page-subtitle">Lignes de production et supervision usine</p>
    </div>
    <div class="kpi-row">
      <KpiCard
        label="Lignes actives"
        value="3/4"
        unit=""
        trend="flat"
        status="warn"
        trendLabel="Ligne C en maintenance"
      />
      <KpiCard
        label="Alertes actives"
        :value="sim.indus.alertes"
        unit=""
        :trend="sim.indus.alertes > 2 ? 'up' : 'flat'"
        :status="sim.indus.alertes > 4 ? 'bad' : sim.indus.alertes > 1 ? 'warn' : 'good'"
        :trendLabel="sim.indus.alertes === 0 ? 'Aucune alerte' : `${sim.indus.alertes} alerte${sim.indus.alertes > 1 ? 's' : ''} active${sim.indus.alertes > 1 ? 's' : ''}`"
      />
      <KpiCard
        label="Taux de fonctionnement"
        :value="sim.indus.taux"
        unit="%"
        :trend="sim.indus.taux >= 80 ? 'up' : 'down'"
        :status="sim.indus.taux >= 85 ? 'good' : sim.indus.taux >= 70 ? 'warn' : 'bad'"
        :trendLabel="sim.indus.taux >= 80 ? 'Objectif atteint' : `Objectif : 80%`"
      />
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
import KpiCard from '../components/KpiCard.vue'
import { useFilterStore } from '../stores/filter.js'
import { useSimulatorStore } from '../stores/simulator.js'
import { buildHistoricalData } from '../composables/useChartHistory.js'

const filterStore = useFilterStore()
const sim = useSimulatorStore()

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

const p = () => filterStore.selectedPeriod
const h = () => filterStore.historyLevels

const currentCharge         = computed(() => buildHistoricalData(chargeData[p()],         h(), p(), 'bar'))
const currentAlertes        = computed(() => buildHistoricalData(alertesData[p()],        h(), p(), 'bar'))
const currentVibrations     = computed(() => buildHistoricalData(vibrationsData[p()],     h(), p(), 'line'))
const currentFonctionnement = computed(() => buildHistoricalData(fonctionnementData[p()], h(), p(), 'doughnut'))
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

.theme-industriel {
  --accent: #94a3b8;
}

.theme-industriel::before {
  content: '';
  position: absolute;
  top: -120px;
  left: 50%;
  transform: translateX(-50%);
  width: 700px;
  height: 400px;
  background: radial-gradient(ellipse, rgba(148, 163, 184, 0.15) 0%, transparent 70%);
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
  color: #94a3b8;
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
