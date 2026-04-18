<template>
  <div class="page theme-electricite">
    <div class="page-header">
      <h2 class="page-title">Electricite</h2>
      <p class="page-subtitle">Consommation et production electrique</p>
    </div>
    <div class="grid">
      <ChartCard title="Consommation totale" />
      <ChartCard title="Pic de consommation" />
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
  padding: 0;
  display: flex;
  flex-direction: column;
}

.theme-electricite {
  background: linear-gradient(160deg, #1a1200 0%, #7c4a00 40%, #c97d10 100%);
}

.page-header {
  padding: 84px 28px 28px;
}

.page-title {
  margin: 0 0 4px;
  font-size: 1.6rem;
  font-weight: 700;
  color: #fff;
}

.page-subtitle {
  margin: 0;
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.6);
}

.grid {
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 20px;
  padding: 0 28px 28px;
}
</style>
