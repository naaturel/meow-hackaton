<template>
  <div class="chart-card">
    <div class="card-header">
      <span class="card-title">{{ title }}</span>
      <span class="live-dot"></span>
    </div>
    <div class="chart-body" :class="{ 'chart-body--doughnut': type === 'doughnut' }">
      <LineChart v-if="type === 'line'" :data="data" :options="mergedOptions" />
      <BarChart v-else-if="type === 'bar'" :data="data" :options="mergedOptions" />
      <DoughnutChart v-else-if="type === 'doughnut'" :data="data" :options="mergedOptions" />
      <div v-else class="chart-placeholder" />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import LineChart from './charts/LineChart.vue'
import BarChart from './charts/BarChart.vue'
import DoughnutChart from './charts/DoughnutChart.vue'

const props = defineProps({
  title: String,
  type: String,
  data: Object,
  options: { type: Object, default: () => ({}) },
})

const mergedOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: { legend: { labels: { color: 'rgba(0,0,0,0.55)', font: { family: 'Inter', size: 12 } } } },
  ...props.options,
}))
</script>

<style scoped>
.chart-card {
  position: relative;
  background: rgba(255, 255, 255, 0.82);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  border-radius: 16px;
  padding: 22px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.18), 0 1px 0 rgba(255,255,255,0.8) inset;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  overflow: hidden;
}


.chart-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 36px rgba(0, 0, 0, 0.25), 0 1px 0 rgba(255,255,255,0.8) inset;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(to right, color-mix(in srgb, var(--accent, #22c55e) 12%, transparent), transparent 70%);
  margin: -22px -22px 0;
  padding: 14px 22px 12px;
  border-radius: 15px 15px 0 0;
}

.card-title {
  font-size: 0.72rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.09em;
  color: rgba(0, 0, 0, 0.4);
}

.live-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #22c55e;
  box-shadow: 0 0 6px #22c55e;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}

.chart-body {
  position: relative;
  height: 160px;
  width: 100%;
}

.chart-body--doughnut {
  height: 240px;
}

.chart-body :deep(div) {
  height: 100% !important;
  width: 100% !important;
}

.chart-body :deep(canvas) {
  max-width: 100% !important;
}

.chart-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 8px;
}

@media (max-width: 1024px) {
  .chart-body { height: 140px; }
}

@media (max-width: 640px) {
  .chart-card { padding: 16px; }
  .chart-body { height: 120px; }
}
</style>
