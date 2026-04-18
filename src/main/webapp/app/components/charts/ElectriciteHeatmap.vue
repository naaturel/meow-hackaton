<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { Chart, registerables } from 'chart.js'
import { MatrixController, MatrixElement } from 'chartjs-chart-matrix'

Chart.register(...registerables, MatrixController, MatrixElement)

const chartRef = ref(null)
let chartInstance = null

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

onMounted(() => {
  chartInstance = new Chart(chartRef.value, {
    type: 'matrix',
    data: {
      datasets: [{
        label: 'Consommation (kWh)',
        data: matrixData,
        backgroundColor: (ctx) => getAmberColor(ctx.dataset.data[ctx.dataIndex]?.v ?? 0),
        borderColor: 'rgba(19,19,31,0.6)',
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
            color: 'rgba(255,255,255,0.35)',
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
            color: 'rgba(255,255,255,0.35)',
            font: { size: 11, family: 'Inter' },
          },
        },
      },
    },
  })
})

onBeforeUnmount(() => chartInstance?.destroy())
</script>

<template>
  <div style="position: relative; width: 100%; height: 100%;">
    <canvas ref="chartRef" role="img" aria-label="Heatmap consommation électrique par heure et jour"></canvas>
  </div>
</template>
