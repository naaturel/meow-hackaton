<template>
  <div class="page theme-gaz">
    <div class="page-header">
      <h2 class="page-title">🔥 Gaz</h2>
      <p class="page-subtitle">Pression, debit et consommation gaz</p>
    </div>
    <div class="grid">
      <ChartCard title="Consommation totale" />
      <ChartCard title="Pression reseau" />
      <ChartCard title="Repartition par zone" />
      <ChartCard title="Evolution mensuelle" />
    </div>
  </div>
</template>

<script setup>
import ChartCard from '../components/ChartCard.vue'
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #13131f;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

.theme-gaz {
  --accent: #8b5cf6;
}

.theme-gaz::before {
  content: '';
  position: absolute;
  top: -120px;
  left: 50%;
  transform: translateX(-50%);
  width: 700px;
  height: 400px;
  background: radial-gradient(ellipse, rgba(139, 92, 246, 0.18) 0%, transparent 70%);
  pointer-events: none;
}

.page-header {
  position: relative;
  padding: 84px 28px 28px;
}

.page-title {
  margin: 0 0 4px;
  font-size: 1.8rem;
  font-weight: 700;
  color: #8b5cf6;
  letter-spacing: -0.01em;
}

.page-subtitle {
  margin: 0;
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.45);
}

.grid {
  position: relative;
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 20px;
  padding: 0 28px 28px;
}

.grid :deep(.chart-card) {
  border-left: 3px solid #8b5cf6;
}
</style>
