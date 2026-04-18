<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { Chart, registerables } from 'chart.js'
import { MatrixController, MatrixElement } from 'chartjs-chart-matrix'

Chart.register(...registerables, MatrixController, MatrixElement)

const chartRef = ref(null)
let chartInstance = null

const heures = ['00h','02h','04h','06h','08h','10h','12h','14h','16h','18h','20h','22h']
const jours  = ['Dim','Sam','Ven','Jeu','Mer','Mar','Lun']

// Water consumption profile (liters per 2h slot)
const hourProfile = [10,8,7,8,35,190,280,260,200,160,140,145,155,170,145,135,155,225,260,240,185,125,70,30]
const hourBase = heures.map((_, hi) => (hourProfile[hi * 2] + hourProfile[hi * 2 + 1]) / 2)

const dayMult = {
  Lun: 1.00, Mar: 0.97, Mer: 1.03, Jeu: 0.99,
  Ven: 1.04, Sam: 0.68, Dim: 0.55,
}

const matrixData = []
jours.forEach((jour) => {
  heures.forEach((heure, hi) => {
    const base = hourBase[hi] * (dayMult[jour] ?? 1)
    const noise = 0.90 + Math.random() * 0.20
    matrixData.push({ x: heure, y: jour, v: Math.round(base * noise) })
  })
})

const MIN_V = 5
const MAX_V = 280

function getBlueColor(value) {
  const ratio = Math.max(0, Math.min(1, (value - MIN_V) / (MAX_V - MIN_V)))
  if (ratio < 0.25) {
    const t = ratio / 0.25
    return `rgba(${Math.round(5 + t * 15)}, ${Math.round(20 + t * 40)}, ${Math.round(50 + t * 80)}, 0.9)`
  } else if (ratio < 0.6) {
    const t = (ratio - 0.25) / 0.35
    return `rgba(${Math.round(20 + t * 20)}, ${Math.round(60 + t * 70)}, ${Math.round(130 + t * 80)}, 0.92)`
  } else {
    const t = (ratio - 0.6) / 0.4
    return `rgba(${Math.round(40 + t * 19)}, ${Math.round(130 + t * 0)}, ${Math.round(210 + t * 36)}, 0.95)`
  }
}

onMounted(() => {
  chartInstance = new Chart(chartRef.value, {
    type: 'matrix',
    data: {
      datasets: [{
        label: 'Consommation (L)',
        data: matrixData,
        backgroundColor: (ctx) => getBlueColor(ctx.dataset.data[ctx.dataIndex]?.v ?? 0),
        borderColor: 'rgba(255,255,255,0.85)',
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
          backgroundColor: 'rgba(10,20,50,0.92)',
          borderColor: 'rgba(59,130,246,0.4)',
          borderWidth: 1,
          titleColor: 'rgba(255,255,255,0.75)',
          bodyColor: '#60a5fa',
          padding: 10,
          callbacks: {
            title: (ctx) => {
              const d = ctx[0].dataset.data[ctx[0].dataIndex]
              return `${d.y}  ·  ${d.x}`
            },
            label: (ctx) => ` ${ctx.dataset.data[ctx.dataIndex].v} L`,
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

onBeforeUnmount(() => chartInstance?.destroy())
</script>

<template>
  <div style="position: relative; width: 100%; height: 100%;">
    <canvas ref="chartRef" role="img" aria-label="Heatmap consommation eau par heure et jour"></canvas>
  </div>
</template>
