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
  plugins: { legend: { labels: { color: 'rgba(0,0,0,0.6)' } } },
  ...props.options,
}))
</script>

<style scoped>
.chart-card {
  background: #f4f4f8;
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 16px;
  padding: 22px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.chart-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 28px rgba(0, 0, 0, 0.16);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-title {
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: rgba(0, 0, 0, 0.45);
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
  height: 200px;
  width: 100%;
}

.chart-body--doughnut {
  height: 320px;
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
  .chart-body {
    height: 180px;
  }
}

@media (max-width: 640px) {
  .chart-card {
    padding: 16px;
  }

  .chart-body {
    height: 160px;
  }
}
</style>
