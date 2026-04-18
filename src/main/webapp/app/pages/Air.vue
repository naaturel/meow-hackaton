<template>
  <div class="page theme-air">
    <div class="page-header">
      <h2 class="page-title">Qualite de l'air</h2>
      <p class="page-subtitle">Particules, CO2 et indice de qualite</p>
    </div>
    <div class="grid">
      <ChartCard title="Indice de qualite (IQA)" />
      <ChartCard title="Concentration CO2" />
      <ChartCard title="Particules fines PM2.5" />
      <ChartCard title="Evolution sur 7 jours" />
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

.theme-air {
  background: linear-gradient(160deg, #001a0a 0%, #065f2e 40%, #16a34a 100%);
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
