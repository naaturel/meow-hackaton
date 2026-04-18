<template>
  <div class="chart-card">
    <div class="card-header">
      <span class="card-title">{{ title }}</span>
      <span class="live-dot"></span>
    </div>
    <div class="metric-row">
      <span class="metric-value">—</span>
      <span class="metric-badge trend-up">↑ —%</span>
    </div>
    <div class="chart-area">
      <div class="bars">
        <div class="bar" v-for="h in barHeights" :key="h.id" :style="{ height: h.pct + '%' }"></div>
      </div>
      <div class="chart-line"></div>
    </div>
  </div>
</template>

<script setup>
defineProps({ title: String })

const barHeights = [
  { id: 1, pct: 45 }, { id: 2, pct: 62 }, { id: 3, pct: 38 },
  { id: 4, pct: 75 }, { id: 5, pct: 55 }, { id: 6, pct: 83 },
  { id: 7, pct: 60 }, { id: 8, pct: 70 }, { id: 9, pct: 48 },
  { id: 10, pct: 90 }, { id: 11, pct: 65 }, { id: 12, pct: 78 },
]
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
  cursor: default;
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

.metric-row {
  display: flex;
  align-items: baseline;
  gap: 10px;
}

.metric-value {
  font-size: 2rem;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.9);
  letter-spacing: -0.02em;
  line-height: 1;
}

.metric-badge {
  font-size: 0.75rem;
  font-weight: 600;
  padding: 3px 8px;
  border-radius: 20px;
}

.trend-up {
  background: rgba(34, 197, 94, 0.15);
  color: #22c55e;
}

.chart-area {
  flex: 1;
  min-height: 100px;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.bars {
  display: flex;
  align-items: flex-end;
  gap: 4px;
  height: 90px;
  padding-bottom: 1px;
}

.bar {
  flex: 1;
  border-radius: 3px 3px 0 0;
  background: var(--accent, rgba(255, 255, 255, 0.15));
  opacity: 0.35;
  transition: opacity 0.2s;
}

.chart-card:hover .bar {
  opacity: 0.55;
}

.bar:last-child {
  opacity: 0.9;
}

.chart-line {
  height: 1px;
  background: rgba(255, 255, 255, 0.08);
  width: 100%;
}
</style>
