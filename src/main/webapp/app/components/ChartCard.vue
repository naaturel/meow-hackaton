<template>
  <div class="chart-card">
    <h3 class="chart-title">{{ title }}</h3>
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
  plugins: { legend: { labels: { color: '#555' } } },
  ...props.options,
}))
</script>

<style scoped>
.chart-card {
  background: rgba(255, 255, 255, 0.92);
  border-radius: 12px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  backdrop-filter: blur(4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.chart-title {
  margin: 0;
  font-size: 0.9rem;
  font-weight: 600;
  color: #444;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.chart-body {
  flex: 1;
  min-height: 180px;
  position: relative;
}

.chart-placeholder {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.04);
  border-radius: 8px;
  border: 2px dashed rgba(0, 0, 0, 0.1);
}
</style>
