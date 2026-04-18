<template>
  <div class="page theme-industriel">
    <div class="page-header">
      <h2 class="page-title">Capteurs industriels</h2>
      <p class="page-subtitle">Lignes de production et supervision usine</p>
    </div>
    <div class="grid">
      <ChartCard title="Charge des lignes" />
      <ChartCard title="Alertes capteurs" />
      <ChartCard title="Vibrations et pression" />
      <ChartCard title="Temps de fonctionnement" />
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

.theme-industriel {
  background: linear-gradient(160deg, #0a0a0a 0%, #1c1917 40%, #44403c 100%);
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
