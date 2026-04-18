<template>
  <div class="page theme-temperature">
    <div class="page-header">
      <div class="page-header-top">
        <div>
          <h2 class="page-title">Température</h2>
          <p class="page-subtitle">Relevés thermiques et anomalies</p>
        </div>

        <!--
          Bouton qui ouvre la modale de personnalisation.
          Désactivé tant que les données ne sont pas chargées.
        -->
        <button
          class="pdf-btn"
          @click="showCustomizer = true"
          :disabled="loading"
          title="Personnaliser et télécharger le rapport PDF"
        >
       
          <span>{{ generating ? 'Génération...' : 'Rapport PDF' }}</span>
        </button>
      </div>
    </div>

    <div v-if="loading" class="loading">Chargement des données...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <template v-else>
      <div class="kpi-row">
        <KpiCard label="Température actuelle" :value="kpi.current" unit="°C" trend="up" status="warn" :trendLabel="`${kpi.ecartNormale > 0 ? '+' : ''}${kpi.ecartNormale}°C vs normale`" />
        <KpiCard label="Température max aujourd'hui" :value="kpi.maxToday" unit="°C" trend="up" status="bad" trendLabel="Max du jour" />
        <KpiCard label="Amplitude journalière" :value="kpi.amplitude" unit="°C" trend="flat" status="neutral" :trendLabel="`${kpi.minToday}°C → ${kpi.maxToday}°C`" />
      </div>
      <div class="grid">
        <ChartCard title="Température moyenne (°C)" type="line" :data="currentMoyenne" />
        <ChartCard title="Température Max/Min (°C)" type="bar" :data="currentZones" />
        <ChartCard title="Écarts par rapport à la normale" type="bar" :data="currentEcarts" />
        <ChartCard title="Historique mensuel (°C)" type="line" :data="currentHistorique" />
      </div>
    </template>

    <!--
      ══════════════════════════════════════════════
      MODALE DE PERSONNALISATION
      v-model        → contrôle l'affichage (showCustomizer)
      v-model:options → synchronise l'objet reportOptions
      @confirm       → déclenche la génération PDF
      ══════════════════════════════════════════════
    -->
    <ReportCustomizer
      v-model="showCustomizer"
      v-model:options="reportOptions"
      @confirm="onConfirmPDF"
    />

    <!--
      ══════════════════════════════════════════════
      ZONE RAPPORT PDF — invisible à l'écran
      Positionnée hors viewport (left: -9999px).
      html2canvas la capture quand on génère le PDF.
      Son contenu s'adapte dynamiquement aux options
      choisies dans la modale.
      ══════════════════════════════════════════════
    -->
    <div ref="reportRef" class="pdf-report">

      <!-- En-tête du PDF : logo + nom client + date -->
      <div class="pdf-header" :style="{ borderColor: reportOptions.accentColor }">
        <div class="pdf-header-left">
          <!--
            Logo : affiché uniquement si un fichier a été uploadé.
            On utilise directement le base64 comme src.
          -->
          <img
            v-if="reportOptions.logoBase64"
            :src="reportOptions.logoBase64"
            class="pdf-logo-img"
            alt="Logo client"
          />
          <!-- Icône par défaut si pas de logo -->
          <span v-else class="pdf-logo-default">🌡️</span>

          <div>
            <!--
              Si un nom de client est renseigné, on l'affiche.
              Sinon, on affiche le titre générique.
            -->
            <h1 class="pdf-title" :style="{ color: reportOptions.accentColor }">
              {{ reportOptions.clientName || 'Rapport Thermique' }}
            </h1>
            <p class="pdf-subtitle">
              {{ reportOptions.clientSubtitle || 'Relevés météorologiques — Liège, Belgique' }}
            </p>
          </div>
        </div>

        <div class="pdf-header-right">
          <p>Généré le {{ generatedDate }}</p>
          <p>Analyse sur {{ reportOptions.period }} jours</p>
        </div>
      </div>

      <!-- ── KPIs (optionnel) ── -->
      <section v-if="reportOptions.showKpis" class="pdf-section">
        <h2 class="pdf-section-title" :style="{ borderColor: reportOptions.accentColor + '55' }">
           Indicateurs du jour
        </h2>
        <div class="pdf-kpi-grid">
          <div class="pdf-kpi">
            <span class="pdf-kpi-label">Température moyenne</span>
            <span class="pdf-kpi-value" :style="{ color: reportOptions.accentColor }">
              {{ kpi.current }}<small>°C</small>
            </span>
            <span class="pdf-kpi-sub" :class="kpi.ecartNormale > 0 ? 'warn' : 'ok'">
              {{ kpi.ecartNormale > 0 ? '+' : '' }}{{ kpi.ecartNormale }}°C vs normale
            </span>
          </div>
          <div class="pdf-kpi">
            <span class="pdf-kpi-label">Température maximale</span>
            <span class="pdf-kpi-value red">{{ kpi.maxToday }}<small>°C</small></span>
            <span class="pdf-kpi-sub">Max du jour</span>
          </div>
          <div class="pdf-kpi">
            <span class="pdf-kpi-label">Température minimale</span>
            <span class="pdf-kpi-value blue">{{ kpi.minToday }}<small>°C</small></span>
            <span class="pdf-kpi-sub">Min du jour</span>
          </div>
          <div class="pdf-kpi">
            <span class="pdf-kpi-label">Amplitude journalière</span>
            <span class="pdf-kpi-value">{{ kpi.amplitude }}<small>°C</small></span>
            <span class="pdf-kpi-sub">{{ kpi.minToday }}°C → {{ kpi.maxToday }}°C</span>
          </div>
        </div>
      </section>

      <!-- ── Graphiques (optionnel) ── -->
      <template v-if="reportOptions.showCharts">
        <section class="pdf-section">
          <h2 class="pdf-section-title" :style="{ borderColor: reportOptions.accentColor + '55' }">
             Température moyenne — {{ reportOptions.period }} derniers jours
          </h2>
          <div class="pdf-chart-box"><canvas ref="pdfChartMoyenne"></canvas></div>
        </section>

        <section class="pdf-section">
          <h2 class="pdf-section-title" :style="{ borderColor: reportOptions.accentColor + '55' }">
             Max / Min — {{ reportOptions.period }} derniers jours
          </h2>
          <div class="pdf-chart-box"><canvas ref="pdfChartZones"></canvas></div>
        </section>

        <section class="pdf-section">
          <h2 class="pdf-section-title" :style="{ borderColor: reportOptions.accentColor + '55' }">
             Écarts vs normale — {{ reportOptions.period }} derniers jours
          </h2>
          <div class="pdf-chart-box"><canvas ref="pdfChartEcarts"></canvas></div>
        </section>
      </template>

      <!-- ── Tableau récapitulatif (optionnel) ── -->
      <section v-if="reportOptions.showTable" class="pdf-section">
        <h2 class="pdf-section-title" :style="{ borderColor: reportOptions.accentColor + '55' }">
           Récapitulatif — {{ reportOptions.period }} derniers jours
        </h2>
        <table class="pdf-table">
          <thead>
            <tr :style="{ background: reportOptions.accentColor }">
              <th>Date</th><th>Moy. (°C)</th><th>Max (°C)</th>
              <th>Min (°C)</th><th>Amplitude</th><th>Écart normale</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, i) in pdfTableData" :key="i" :class="i % 2 === 0 ? 'even' : ''">
              <td>{{ row.date }}</td>
              <td>{{ row.mean }}</td>
              <td class="red">{{ row.max }}</td>
              <td class="blue">{{ row.min }}</td>
              <td>{{ row.amplitude }}</td>
              <td :class="row.ecart > 0 ? 'warn' : 'ok'">
                {{ row.ecart > 0 ? '+' : '' }}{{ row.ecart }}
              </td>
            </tr>
          </tbody>
        </table>
      </section>

      <!-- ── Notes libres (optionnel) ── -->
      <section v-if="reportOptions.showNotes && reportOptions.notes" class="pdf-section">
        <h2 class="pdf-section-title" :style="{ borderColor: reportOptions.accentColor + '55' }">
           Notes
        </h2>
        <div class="pdf-notes">{{ reportOptions.notes }}</div>
      </section>

      <!-- Pied de page -->
      <div class="pdf-footer">
        <span>Source : Open-Meteo API — open-meteo.com</span>
        <!--
          Si un nom client est renseigné, on l'affiche dans le footer
          pour renforcer l'identité du document.
        -->
        <span v-if="reportOptions.clientName">
          Rapport destiné à {{ reportOptions.clientName }}
        </span>
        <span v-else>Document généré automatiquement</span>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import ChartCard from '../components/ChartCard.vue'
import KpiCard from '../components/KpiCard.vue'
import ReportCustomizer from './ReportCustomizer.vue' // ← nouveau composant
import { useFilterStore } from '../stores/filter.js'
import { buildHistoricalData } from '../composables/useChartHistory.js'
import jsPDF from 'jspdf'
import html2canvas from 'html2canvas'
import Chart from 'chart.js/auto'

const filterStore = useFilterStore()
const loading     = ref(true)
const error       = ref(null)
const generating  = ref(false)

// Contrôle l'affichage de la modale
const showCustomizer = ref(false)

/**
 * reportOptions — objet centralisé des préférences du rapport.
 * C'est la "source de vérité" partagée entre la modale et le template PDF.
 * Toutes les valeurs ont une valeur par défaut sensée.
 */
const reportOptions = ref({
  clientName:     '',          // Nom du client (vide = titre générique)
  clientSubtitle: '',          // Ligne secondaire
  logoBase64:     '',          // Image encodée en base64 (vide = icône par défaut)
  period:         '7',         // Nombre de jours à analyser
  showKpis:       true,        // Inclure la section KPIs
  showCharts:     true,        // Inclure les graphiques
  showTable:      true,        // Inclure le tableau
  showNotes:      false,       // Inclure une zone de notes
  notes:          '',          // Contenu des notes libres
  accentColor:    '#dc2626',   // Couleur principale du PDF
})

// ── Refs DOM pour les éléments du rapport PDF ──
const reportRef       = ref(null)
const pdfChartMoyenne = ref(null)
const pdfChartZones   = ref(null)
const pdfChartEcarts  = ref(null)

// ── Données brutes API ──
const hourlyLabels = ref([])
const hourlyTemps  = ref([])
const dailyLabels  = ref([])
const dailyMax     = ref([])
const dailyMin     = ref([])
const dailyMean    = ref([])

// ── KPIs ──
const kpi = ref({ current: '--', maxToday: '--', minToday: '--', amplitude: '--', ecartNormale: '--' })

// Normales mensuelles pour Liège
const normales = [3, 4, 8, 12, 16, 19, 21, 21, 17, 13, 7, 4]

// Date affichée dans le PDF, calculée à l'ouverture de la page
const generatedDate = new Date().toLocaleDateString('fr-BE', {
  day: '2-digit', month: 'long', year: 'numeric', hour: '2-digit', minute: '2-digit'
})

// ── Fetch Open-Meteo (inchangé) ──
async function fetchMeteo() {
  loading.value = true
  error.value = null
  try {
    const url = new URL('https://api.open-meteo.com/v1/forecast')
    url.searchParams.set('latitude',  '50.6337')
    url.searchParams.set('longitude', '5.5675')
    url.searchParams.set('hourly',    'temperature_2m')
    url.searchParams.set('daily',     'temperature_2m_max,temperature_2m_min,temperature_2m_mean')
    url.searchParams.set('past_days', '92')
    url.searchParams.set('forecast_days', '1')
    url.searchParams.set('timezone',  'auto')

    const res = await fetch(url)
    if (!res.ok) throw new Error(`Erreur API: ${res.status}`)
    const data = await res.json()

    hourlyLabels.value = data.hourly.time.map(t => t.slice(11, 16))
    hourlyTemps.value  = data.hourly.temperature_2m

    dailyLabels.value = data.daily.time.map(t =>
      new Date(t).toLocaleDateString('fr-BE', { day: '2-digit', month: 'short' })
    )
    dailyMax.value  = data.daily.temperature_2m_max
    dailyMin.value  = data.daily.temperature_2m_min
    dailyMean.value = data.daily.temperature_2m_mean

    const last  = dailyMax.value.length - 1
    const maxT  = Math.round(dailyMax.value[last])
    const minT  = Math.round(dailyMin.value[last])
    const meanT = Math.round(dailyMean.value[last])
    const month = new Date().getMonth()

    kpi.value = {
      current:      meanT,
      maxToday:     maxT,
      minToday:     minT,
      amplitude:    maxT - minT,
      ecartNormale: Math.round((meanT - normales[month]) * 10) / 10,
    }
  } catch (e) {
    error.value = `Impossible de charger les données météo: ${e.message}`
  } finally {
    loading.value = false
  }
}

// ── Computed existants (inchangés) ──
const p = () => filterStore.selectedPeriod
const h = () => filterStore.historyLevels
function lastN(arr, n) { return arr.slice(-n) }

const moyenneData = computed(() => {
  const period = p()
  if (period === 'heure') {
    return { labels: lastN(hourlyLabels.value, 24), datasets: [{ label: '°C', data: lastN(hourlyTemps.value, 24), borderColor: '#dc2626', backgroundColor: 'rgba(220,38,38,0.15)', fill: true, tension: 0.4 }] }
  }
  if (period === 'jour') {
    return { labels: lastN(dailyLabels.value, 7), datasets: [{ label: '°C', data: lastN(dailyMean.value, 7).map(v => Math.round(v * 10) / 10), borderColor: '#dc2626', backgroundColor: 'rgba(220,38,38,0.15)', fill: true, tension: 0.4 }] }
  }
  if (period === 'semaine') {
    const weeks = [0, 1, 2, 3].map(i => {
      const chunk = dailyMean.value.slice(-(28 - i * 7), -(21 - i * 7) || undefined)
      return Math.round(chunk.reduce((a, b) => a + b, 0) / (chunk.length || 1) * 10) / 10
    })
    return { labels: ['Sem -3', 'Sem -2', 'Sem -1', 'Sem actuelle'], datasets: [{ label: '°C', data: weeks, borderColor: '#dc2626', backgroundColor: 'rgba(220,38,38,0.15)', fill: true, tension: 0.4 }] }
  }
  const byMonth = {}
  dailyLabels.value.forEach((label, i) => {
    const key = label.slice(3)
    if (!byMonth[key]) byMonth[key] = []
    byMonth[key].push(dailyMean.value[i])
  })
  const labels = Object.keys(byMonth)
  return { labels, datasets: [{ label: '°C', data: labels.map(k => Math.round(byMonth[k].reduce((a, b) => a + b, 0) / byMonth[k].length * 10) / 10), borderColor: '#dc2626', backgroundColor: 'rgba(220,38,38,0.12)', fill: true, tension: 0.4 }] }
})

const zonesData = computed(() => {
  const n = p() === 'heure' ? 1 : p() === 'jour' ? 7 : p() === 'semaine' ? 28 : 92
  return {
    labels: lastN(dailyLabels.value, n === 1 ? 1 : n),
    datasets: [
      { label: 'Max °C', data: lastN(dailyMax.value, n).map(v => Math.round(v * 10) / 10), backgroundColor: '#dc2626', borderRadius: 6 },
      { label: 'Min °C', data: lastN(dailyMin.value, n).map(v => Math.round(v * 10) / 10), backgroundColor: '#7f1d1d', borderRadius: 6 },
    ],
  }
})

const ecartsData = computed(() => {
  const period = p()
  const n = period === 'heure' ? 24 : period === 'jour' ? 7 : period === 'semaine' ? 28 : 92
  if (period === 'heure') {
    const month = new Date().getMonth()
    return { labels: lastN(hourlyLabels.value, 24), datasets: [{ label: 'Écart (°C)', data: lastN(hourlyTemps.value, 24).map(v => Math.round((v - normales[month]) * 10) / 10), backgroundColor: '#ef4444', borderRadius: 4 }] }
  }
  return {
    labels: lastN(dailyLabels.value, n),
    datasets: [{ label: 'Écart (°C)', data: lastN(dailyMean.value, n).map((v, i) => {
      const d = new Date(); d.setDate(d.getDate() - (n - 1 - i))
      return Math.round((v - normales[d.getMonth()]) * 10) / 10
    }), backgroundColor: '#ef4444', borderRadius: 4 }]
  }
})

const currentHistorique = computed(() => buildHistoricalData(moyenneData.value, h(), p(), 'line'))
const currentMoyenne    = computed(() => buildHistoricalData(moyenneData.value, h(), p(), 'line'))
const currentZones      = computed(() => buildHistoricalData(zonesData.value,   h(), p(), 'bar'))
const currentEcarts     = computed(() => buildHistoricalData(ecartsData.value,  h(), p(), 'bar'))

/**
 * pdfTableData
 * Calcule les lignes du tableau en tenant compte de la période
 * choisie dans reportOptions.period (ex: '7', '30', '90').
 */
const pdfTableData = computed(() => {
  // On convertit la période string en nombre
  const n = Math.min(parseInt(reportOptions.value.period), dailyMax.value.length)
  return lastN(dailyLabels.value, n).map((date, i) => {
    const offset = dailyMax.value.length - n + i
    const max  = Math.round(dailyMax.value[offset]  * 10) / 10
    const min  = Math.round(dailyMin.value[offset]  * 10) / 10
    const mean = Math.round(dailyMean.value[offset] * 10) / 10
    const d = new Date(); d.setDate(d.getDate() - (n - 1 - i))
    return {
      date,
      mean,
      max,
      min,
      amplitude: Math.round((max - min) * 10) / 10,
      ecart:     Math.round((mean - normales[d.getMonth()]) * 10) / 10,
    }
  })
})

// ── Instances Chart.js pour le PDF (séparées des charts de la page) ──
let cMoyenne = null, cZones = null, cEcarts = null

/**
 * buildPdfCharts()
 * Crée (ou recrée) les 3 graphiques dans la zone PDF cachée.
 * Utilise la période de reportOptions pour adapter les données.
 * La couleur d'accentuation est appliquée dynamiquement.
 */
function buildPdfCharts() {
  // On détruit les instances précédentes pour éviter les fuites mémoire
  if (cMoyenne) cMoyenne.destroy()
  if (cZones)   cZones.destroy()
  if (cEcarts)  cEcarts.destroy()

  const n      = Math.min(parseInt(reportOptions.value.period), dailyMax.value.length)
  const color  = reportOptions.value.accentColor // Couleur choisie par l'utilisateur
  const labels = lastN(dailyLabels.value, n)

  // Options communes aux 3 graphiques
  const baseOpts = (yLabel) => ({
    responsive: true,
    animation: false, // Désactivé pour la capture html2canvas
    plugins: {
      legend: { labels: { color: '#1e293b', font: { size: 11 } } }
    },
    scales: {
      x: { ticks: { color: '#475569', font: { size: 9 } }, grid: { color: '#e2e8f0' } },
      y: {
        ticks: { color: '#475569', font: { size: 9 } },
        grid:  { color: '#e2e8f0' },
        title: { display: true, text: yLabel, color: '#64748b', font: { size: 9 } }
      }
    }
  })

  // Graphique 1 : Température moyenne — couleur personnalisable
  cMoyenne = new Chart(pdfChartMoyenne.value, {
    type: 'line',
    data: {
      labels,
      datasets: [{
        label: 'Moy. (°C)',
        data: lastN(dailyMean.value, n).map(v => Math.round(v * 10) / 10),
        borderColor: color,
        backgroundColor: color + '22', // Hex opacity 13%
        fill: true,
        tension: 0.4,
        pointRadius: n <= 14 ? 4 : 2, // Points plus petits si beaucoup de données
        pointBackgroundColor: color,
      }]
    },
    options: baseOpts('Température (°C)')
  })

  // Graphique 2 : Max / Min — Max en couleur d'accent, Min en bleu
  cZones = new Chart(pdfChartZones.value, {
    type: 'bar',
    data: {
      labels,
      datasets: [
        { label: 'Max (°C)', data: lastN(dailyMax.value, n).map(v => Math.round(v * 10) / 10), backgroundColor: color, borderRadius: 4 },
        { label: 'Min (°C)', data: lastN(dailyMin.value, n).map(v => Math.round(v * 10) / 10), backgroundColor: '#3b82f6', borderRadius: 4 },
      ]
    },
    options: baseOpts('Température (°C)')
  })

  // Graphique 3 : Écarts — positif = couleur accent, négatif = bleu
  cEcarts = new Chart(pdfChartEcarts.value, {
    type: 'bar',
    data: {
      labels,
      datasets: [{
        label: 'Écart (°C)',
        data: lastN(dailyMean.value, n).map((v, i) => {
          const d = new Date(); d.setDate(d.getDate() - (n - 1 - i))
          return Math.round((v - normales[d.getMonth()]) * 10) / 10
        }),
        // Couleur conditionnelle : rouge si chaud, bleu si froid
        backgroundColor: (ctx) => ctx.raw >= 0 ? color : '#3b82f6',
        borderRadius: 4,
      }]
    },
    options: baseOpts('Écart (°C)')
  })
}

/**
 * onConfirmPDF()
 * Appelée quand le client clique sur "Générer le PDF" dans la modale.
 * 1. Ferme la modale
 * 2. Construit les graphiques PDF avec les options choisies
 * 3. Attend que Chart.js ait fini de rendre (setTimeout 300ms)
 * 4. Capture la zone rapport avec html2canvas
 * 5. Génère le PDF multi-pages avec jsPDF
 */
async function onConfirmPDF() {
  showCustomizer.value = false // Ferme la modale
  generating.value = true
  try {
    // Si la section graphiques est activée, on les construit
    if (reportOptions.value.showCharts) {
      buildPdfCharts()
      // Pause pour laisser Chart.js terminer le rendu canvas
      await new Promise(r => setTimeout(r, 300))
    }

    // Capture de la zone PDF (hors écran)
    const canvas = await html2canvas(reportRef.value, {
      scale: 2,           // Résolution x2 pour un PDF net
      useCORS: true,      // Nécessaire si le logo vient d'une URL externe
      backgroundColor: '#ffffff',
      logging: false,
    })

    const imgData = canvas.toDataURL('image/png')
    const pdf     = new jsPDF('p', 'mm', 'a4')
    const pdfW    = pdf.internal.pageSize.getWidth()
    const pdfH    = pdf.internal.pageSize.getHeight()
    const imgH    = (canvas.height * pdfW) / canvas.width

    // Découpage automatique en plusieurs pages si le contenu est long
    let yPos = 0, remaining = imgH
    while (remaining > 0) {
      pdf.addImage(imgData, 'PNG', 0, -yPos, pdfW, imgH)
      remaining -= pdfH
      yPos      += pdfH
      if (remaining > 0) pdf.addPage()
    }

    // Nom de fichier avec nom client si disponible
    const clientSlug = reportOptions.value.clientName
      ? `-${reportOptions.value.clientName.replace(/\s+/g, '_').toLowerCase()}`
      : ''
    const dateStr = new Date().toISOString().slice(0, 10)
    pdf.save(`rapport-temperature${clientSlug}-${dateStr}.pdf`)

  } catch (e) {
    error.value = `Erreur PDF : ${e.message}`
  } finally {
    generating.value = false
  }
}

onMounted(fetchMeteo)
</script>

<style scoped>
/* ── Styles existants (inchangés) ── */
.page {
  min-height: 100vh;
  background: #13131f;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}
.theme-temperature { --accent: #ef4444; }
.theme-temperature::before {
  content: '';
  position: absolute;
  top: -120px;
  left: 50%;
  transform: translateX(-50%);
  width: 700px;
  height: 400px;
  background: radial-gradient(ellipse, rgba(239, 68, 68, 0.28) 0%, transparent 70%);
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
  color: #ef4444;
  letter-spacing: -0.01em;
  display: flex;
  align-items: center;
  gap: 12px;
}
.page-title::before {
  content: '';
  width: 12px; height: 12px;
  border-radius: 50%;
  background: #22c55e;
  flex-shrink: 0;
  animation: dot-pulse 2s ease-in-out infinite;
}
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

/* ── Nouveau : header avec bouton ── */
.page-header-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}
.pdf-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
  background: rgba(239, 68, 68, 0.12);
  border: 1px solid rgba(239, 68, 68, 0.35);
  border-radius: 8px;
  color: #ef4444;
  font-size: 0.88rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s, border-color 0.2s;
  white-space: nowrap;
}
.pdf-btn:hover:not(:disabled) {
  background: rgba(239, 68, 68, 0.24);
  border-color: rgba(239, 68, 68, 0.6);
}
.pdf-btn:disabled { opacity: 0.45; cursor: not-allowed; }

/* ── Zone rapport PDF cachée hors écran ── */
.pdf-report {
  position: absolute;
  left: -9999px;
  top: 0;
  width: 794px; /* Largeur A4 à 96 dpi */
  background: #ffffff;
  padding: 40px 48px;
  font-family: 'Segoe UI', Arial, sans-serif;
  color: #1e293b;
  box-sizing: border-box;
  pointer-events: none;
}

/* ── En-tête PDF ── */
.pdf-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 3px solid; /* Couleur appliquée via :style */
  padding-bottom: 18px;
  margin-bottom: 28px;
}
.pdf-header-left  { display: flex; align-items: center; gap: 14px; }
.pdf-logo-img     { height: 48px; max-width: 130px; object-fit: contain; }
.pdf-logo-default { font-size: 2.2rem; }
.pdf-title        { margin: 0; font-size: 1.5rem; font-weight: 700; }
.pdf-subtitle     { margin: 3px 0 0; font-size: 0.8rem; color: #64748b; }
.pdf-header-right { text-align: right; font-size: 0.78rem; color: #94a3b8; }
.pdf-header-right p { margin: 2px 0; }

/* ── Sections ── */
.pdf-section { margin-bottom: 28px; }
.pdf-section-title {
  font-size: 0.92rem;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 12px;
  padding-bottom: 6px;
  border-bottom: 2px solid; /* Couleur via :style */
}

/* ── KPIs ── */
.pdf-kpi-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}
.pdf-kpi {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 14px 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 3px;
}
.pdf-kpi-label { font-size: 0.67rem; color: #64748b; }
.pdf-kpi-value { font-size: 1.8rem; font-weight: 700; color: #1e293b; line-height: 1; }
.pdf-kpi-value small { font-size: 0.85rem; font-weight: 400; color: #94a3b8; }
.pdf-kpi-sub   { font-size: 0.67rem; color: #64748b; }
.pdf-kpi-value.red  { color: #dc2626; }
.pdf-kpi-value.blue { color: #3b82f6; }

/* ── Graphiques ── */
.pdf-chart-box {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 16px;
  height: 190px;
}
.pdf-chart-box canvas { max-height: 158px; }

/* ── Tableau ── */
.pdf-table { width: 100%; border-collapse: collapse; font-size: 0.79rem; }
.pdf-table th { color: #fff; padding: 9px 10px; text-align: left; font-size: 0.74rem; }
.pdf-table td { padding: 8px 10px; border-bottom: 1px solid #e2e8f0; }
.pdf-table .even { background: #f8fafc; }
.pdf-table .red  { color: #dc2626; font-weight: 600; }
.pdf-table .blue { color: #3b82f6; font-weight: 600; }
.pdf-table .warn { color: #f59e0b; font-weight: 600; }
.pdf-table .ok   { color: #22c55e; font-weight: 600; }

/* ── Notes ── */
.pdf-notes {
  background: #fffbeb;
  border-left: 4px solid #f59e0b;
  border-radius: 0 6px 6px 0;
  padding: 14px 16px;
  font-size: 0.83rem;
  color: #78350f;
  line-height: 1.6;
  white-space: pre-wrap;
}

/* ── Pied de page PDF ── */
.pdf-footer {
  display: flex;
  justify-content: space-between;
  font-size: 0.7rem;
  color: #94a3b8;
  border-top: 1px solid #e2e8f0;
  padding-top: 14px;
  margin-top: 10px;
}

.warn { color: #f59e0b; }
.ok   { color: #22c55e; }
</style>