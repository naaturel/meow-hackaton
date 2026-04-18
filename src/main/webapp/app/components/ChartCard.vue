<template>
  <div class="chart-card">
    <h3 class="chart-title">{{ title }}</h3>
    <div class="chart-placeholder"></div>
  </div>
</template>

<script setup>
defineProps({ title: String })
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

.chart-placeholder {
  flex: 1;
  min-height: 180px;
  background: rgba(0, 0, 0, 0.04);
  border-radius: 8px;
  border: 2px dashed rgba(0, 0, 0, 0.1);
}
</style>
