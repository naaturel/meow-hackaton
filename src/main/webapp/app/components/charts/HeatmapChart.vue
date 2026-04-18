<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { Chart, registerables } from 'chart.js'
import { MatrixController, MatrixElement } from 'chartjs-chart-matrix'

Chart.register(...registerables, MatrixController, MatrixElement)

const chartRef = ref(null)
let chartInstance = null

const jours = ['Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam', 'Dim']
const semaines = ['S1', 'S2', 'S3', 'S4', 'S5']

const matrixData = []
semaines.forEach((semaine, si) => {
  jours.forEach((jour, ji) => {
    matrixData.push({
      x: semaine,
      y: jour,
      v: Math.round(20 + Math.random() * 80)
    })
  })
})

const getColor = (value) => {
  const ratio = (value - 20) / 80
  const r = Math.round(55 + ratio * 180)
  const g = Math.round(130 - ratio * 80)
  const b = Math.round(200 - ratio * 160)
  return `rgb(${r},${g},${b})`
}

onMounted(() => {
  chartInstance = new Chart(chartRef.value, {
    type: 'matrix',
    data: {
      datasets: [{
        label: 'Consommation (m³)',
        data: matrixData,
        backgroundColor: (ctx) => getColor(ctx.dataset.data[ctx.dataIndex]?.v ?? 0),
        borderColor: 'transparent',
        borderWidth: 2,
        width: (ctx) => {
          const area = ctx.chart.chartArea
          return area ? area.width / semaines.length - 4 : 30
        },
        height: (ctx) => {
          const area = ctx.chart.chartArea
          return area ? area.height / jours.length - 4 : 30
        },
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            title: (ctx) => `${ctx[0].dataset.data[ctx[0].dataIndex].y} — ${ctx[0].dataset.data[ctx[0].dataIndex].x}`,
            label: (ctx) => ` ${ctx.dataset.data[ctx.dataIndex].v} m³`
          }
        }
      },
      scales: {
        x: {
          type: 'category',
          labels: semaines,
          grid: { display: false },
          ticks: { color: '#888780', font: { size: 11 } }
        },
        y: {
          type: 'category',
          labels: jours,
          offset: true,
          grid: { display: false },
          ticks: { color: '#888780', font: { size: 11 } }
        }
      }
    }
  })
})

onBeforeUnmount(() => chartInstance?.destroy())
</script>

<template>
  <div style="position: relative; width: 100%; height: 200px;">
    <canvas ref="chartRef" role="img" aria-label="Heatmap de consommation d'eau par semaine"></canvas>
  </div>
</template>