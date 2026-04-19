<template>
  <div class="chart-card">
    <div class="card-header">
      <span class="card-title">{{ title }}</span>
      <div class="header-right">
        <button
          v-if="previewState !== null"
          class="preview-btn"
          :class="previewState"
          :disabled="previewState === 'loading'"
          @click="emit('togglePreview')"
        >
          <template v-if="previewState === 'idle'">Prédire</template>
          <template v-else-if="previewState === 'loading'"><span class="thinking-text">Thinking</span><span class="thinking-dots">...</span></template>
          <template v-else>Réinitialiser</template>
        </button>
        <span class="live-dot"></span>
      </div>
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
  previewState: { type: String, default: null },
})

const emit = defineEmits(['togglePreview'])

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

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.preview-btn {
  font-size: 0.6rem;
  font-weight: 700;
  letter-spacing: 0.07em;
  text-transform: uppercase;
  padding: 3px 10px;
  border-radius: 20px;
  border: 1px solid rgba(99, 102, 241, 0.4);
  background: rgba(99, 102, 241, 0.1);
  color: #6366f1;
  cursor: pointer;
  transition: background 0.2s, color 0.2s, border-color 0.2s, opacity 0.2s;
  white-space: nowrap;
}

.preview-btn:hover:not(:disabled) {
  background: rgba(99, 102, 241, 0.2);
  border-color: rgba(99, 102, 241, 0.7);
}

.preview-btn.loading {
  opacity: 0.6;
  cursor: default;
  border-color: rgba(99, 102, 241, 0.3);
}

.preview-btn.preview {
  border-color: rgba(167, 139, 250, 0.5);
  background: rgba(167, 139, 250, 0.12);
  color: #a78bfa;
}

.preview-btn.preview:hover {
  background: rgba(239, 68, 68, 0.1);
  border-color: rgba(239, 68, 68, 0.4);
  color: #ef4444;
}

.thinking-dots {
  display: inline-block;
  animation: blink-dots 1.2s steps(4, end) infinite;
  overflow: hidden;
  vertical-align: bottom;
  width: 1.6em;
}

@keyframes blink-dots {
  0%   { width: 0; }
  33%  { width: 0.5em; }
  66%  { width: 1em; }
  100% { width: 1.6em; }
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
