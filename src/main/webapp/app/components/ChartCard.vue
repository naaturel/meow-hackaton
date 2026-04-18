<template>
  <div class="chart-card">
    <div class="card-header">
      <span class="card-title">{{ title }}</span>
      <span class="live-dot"></span>
    </div>
    <div class="chart-body">
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
  plugins: { legend: { labels: { color: 'rgba(255,255,255,0.5)' } } },
  ...props.options,
}))
</script>

<style scoped>
.chart-card {
  background: #1a1a2e;
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  padding: 22px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.5);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.chart-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.6);
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
  color: rgba(255, 255, 255, 0.4);
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
  flex: 1;
  min-height: 180px;
  position: relative;
}

.chart-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 8px;
}
</style>
