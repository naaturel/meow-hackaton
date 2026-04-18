<template>
  <div class="kpi-card">
    <div class="kpi-label">{{ label }}</div>
    <div class="kpi-value">
      {{ value }}<span v-if="unit" class="kpi-unit">{{ unit }}</span>
    </div>
    <div v-if="trendLabel" class="kpi-trend" :class="status">
      <span v-if="trend === 'up'">↑</span>
      <span v-else-if="trend === 'down'">↓</span>
      <span v-else>→</span>
      {{ trendLabel }}
    </div>
  </div>
</template>

<script setup>
defineProps({
  label: String,
  value: [String, Number],
  unit: String,
  trend: { type: String, default: 'flat' },
  trendLabel: String,
  status: { type: String, default: 'neutral' },
})
</script>

<style scoped>
.kpi-card {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 14px;
  padding: 18px 22px;
  display: flex;
  flex-direction: column;
  gap: 6px;
  position: relative;
  overflow: hidden;
  transition: transform 0.2s ease, background 0.2s ease;
}

.kpi-card::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--accent, #22c55e), transparent 70%);
}

.kpi-card:hover {
  transform: translateY(-2px);
  background: rgba(255, 255, 255, 0.07);
}

.kpi-label {
  font-size: 0.68rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: rgba(255, 255, 255, 0.38);
}

.kpi-value {
  font-size: 2rem;
  font-weight: 800;
  color: #fff;
  line-height: 1;
  letter-spacing: -0.03em;
}

.kpi-unit {
  font-size: 0.85rem;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.45);
  margin-left: 4px;
  letter-spacing: 0;
}

.kpi-trend {
  font-size: 0.75rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: 2px;
}

.kpi-trend.good    { color: #22c55e; }
.kpi-trend.warn    { color: #f59e0b; }
.kpi-trend.bad     { color: #ef4444; }
.kpi-trend.neutral { color: rgba(255, 255, 255, 0.38); }
</style>
