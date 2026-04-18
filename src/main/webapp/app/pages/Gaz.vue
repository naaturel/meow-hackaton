<template>
  <div class="page theme-gaz">
    <div class="page-header">
      <h2 class="page-title">Gaz</h2>
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
  display: flex;
  flex-direction: column;
}

.theme-gaz {
  background: linear-gradient(160deg, #0e0018 0%, #3b0764 40%, #7c3aed 100%);
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
