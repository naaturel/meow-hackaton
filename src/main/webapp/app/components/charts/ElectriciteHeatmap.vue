<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'
import { Chart, registerables } from 'chart.js'
import { MatrixController, MatrixElement } from 'chartjs-chart-matrix'

Chart.register(...registerables, MatrixController, MatrixElement)

const props = defineProps({ previewMode: { type: Boolean, default: false } })

const chartRef = ref(null)
let chartInstance = null

// plain vars used inside Chart.js callbacks (not Vue reactive)
let _inPreview = false
let _revealCount = 0
let _previewData = []
let _revealTimer = null

const heures = ['00h','02h','04h','06h','08h','10h','12h','14h','16h','18h','20h','22h']
const jours  = ['Dim','Sam','Ven','Jeu','Mer','Mar','Lun']

// Base consumption profile per hour index (0=00h, 1=02h, ...)
const hourProfile = [60, 45, 40, 42, 70, 160, 310, 380, 340, 300, 280, 290, 310, 330, 310, 290, 320, 410, 490, 460, 380, 280, 190, 110]
// 12 labels, each covering 2 hours → average the 2 hours
const hourBase = heures.map((_, hi) => (hourProfile[hi * 2] + hourProfile[hi * 2 + 1]) / 2)

// Day multipliers (Mon=1.0 reference, weekends lower)
const dayMult = {
  Lun: 1.00, Mar: 0.98, Mer: 1.02, Jeu: 0.99,
  Ven: 1.05, Sam: 0.72, Dim: 0.58,
}

const matrixData = []
jours.forEach((jour) => {
  heures.forEach((heure, hi) => {
    const base = hourBase[hi] * (dayMult[jour] ?? 1)
    const noise = 0.92 + Math.random() * 0.16
    matrixData.push({ x: heure, y: jour, v: Math.round(base * noise) })
  })
})

const MIN_V = 35
const MAX_V = 520

function getPurpleColor(value) {
  const ratio = Math.max(0, Math.min(1, (value - MIN_V) / (MAX_V - MIN_V)))
  if (ratio < 0.3) {
    const t = ratio / 0.3
    return `rgba(${Math.round(50 + t * 50)}, ${Math.round(10 + t * 20)}, ${Math.round(90 + t * 60)}, 0.9)`
  } else if (ratio < 0.65) {
    const t = (ratio - 0.3) / 0.35
    return `rgba(${Math.round(100 + t * 80)}, ${Math.round(30 + t * 40)}, ${Math.round(150 + t * 60)}, 0.92)`
  } else {
    const t = (ratio - 0.65) / 0.35
    return `rgba(${Math.round(180 + t * 47)}, ${Math.round(70 + t * 69)}, ${Math.round(210 + t * 36)}, 0.95)`
  }
}

function getAmberColor(value) {
  const ratio = Math.max(0, Math.min(1, (value - MIN_V) / (MAX_V - MIN_V)))
  if (ratio < 0.25) {
    // Very dark amber-brown
    const t = ratio / 0.25
    return `rgba(${Math.round(40 + t * 80)}, ${Math.round(20 + t * 40)}, ${Math.round(5 + t * 5)}, 0.9)`
  } else if (ratio < 0.6) {
    const t = (ratio - 0.25) / 0.35
    return `rgba(${Math.round(120 + t * 100)}, ${Math.round(60 + t * 60)}, ${Math.round(10)}, 0.92)`
  } else {
    const t = (ratio - 0.6) / 0.4
    return `rgba(${Math.round(220 + t * 25)}, ${Math.round(120 + t * 38)}, ${Math.round(10 + t * 5)}, 0.95)`
  }
}

watch(() => props.previewMode, (val) => {
  if (!chartInstance) return
  if (val) {
    _previewData = matrixData.map(cell => ({
      ...cell,
      v: Math.round(cell.v * (0.85 + Math.random() * 0.30)),
    }))
    _revealCount = 0
    _inPreview = true
    chartInstance.data.datasets[0].data = _previewData
    chartInstance.update('none')
    const total = _previewData.length
    const interval = 2800 / total
    _revealTimer = setInterval(() => {
      _revealCount++
      chartInstance.update('none')
      if (_revealCount >= total) {
        clearInterval(_revealTimer)
        _revealTimer = null
      }
    }, interval)
  } else {
    if (_revealTimer) { clearInterval(_revealTimer); _revealTimer = null }
    _inPreview = false
    _revealCount = 0
    chartInstance.data.datasets[0].data = matrixData
    chartInstance.update()
  }
})

onMounted(() => {
  chartInstance = new Chart(chartRef.value, {
    type: 'matrix',
    data: {
      datasets: [{
        label: 'Consommation (kWh)',
        data: matrixData,
        backgroundColor: (ctx) => {
          if (_inPreview) {
            if (ctx.dataIndex >= _revealCount) return 'transparent'
            return getPurpleColor(_previewData[ctx.dataIndex]?.v ?? 0)
          }
          return getAmberColor(ctx.dataset.data[ctx.dataIndex]?.v ?? 0)
        },
        borderColor: (ctx) => _inPreview ? 'rgba(167,139,250,0.5)' : 'rgba(255,255,255,0.85)',
        borderWidth: 2,
        borderRadius: 4,
        width: (ctx) => {
          const area = ctx.chart.chartArea
          return area ? (area.width / heures.length) - 5 : 40
        },
        height: (ctx) => {
          const area = ctx.chart.chartArea
          return area ? (area.height / jours.length) - 5 : 28
        },
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: { duration: 600, easing: 'easeOutQuart' },
      plugins: {
        legend: { display: false },
        tooltip: {
          backgroundColor: 'rgba(19,19,31,0.92)',
          borderColor: 'rgba(245,158,11,0.3)',
          borderWidth: 1,
          titleColor: 'rgba(255,255,255,0.7)',
          bodyColor: '#f59e0b',
          padding: 10,
          callbacks: {
            title: (ctx) => {
              const d = ctx[0].dataset.data[ctx[0].dataIndex]
              return `${d.y}  ·  ${d.x}`
            },
            label: (ctx) => ` ${ctx.dataset.data[ctx.dataIndex].v} kWh`,
          },
        },
      },
      scales: {
        x: {
          type: 'category',
          labels: heures,
          position: 'bottom',
          grid: { display: false },
          border: { display: false },
          ticks: {
            color: 'rgba(0,0,0,0.45)',
            font: { size: 11, family: 'Inter' },
            maxRotation: 0,
          },
        },
        y: {
          type: 'category',
          labels: jours,
          offset: true,
          grid: { display: false },
          border: { display: false },
          ticks: {
            color: 'rgba(0,0,0,0.45)',
            font: { size: 11, family: 'Inter' },
          },
        },
      },
    },
  })
})

onBeforeUnmount(() => {
  if (_revealTimer) clearInterval(_revealTimer)
  chartInstance?.destroy()
})
</script>

<template>
  <div style="position: relative; width: 100%; height: 100%;">
    <canvas ref="chartRef" role="img" aria-label="Heatmap consommation électrique par heure et jour"></canvas>
  </div>
</template>
