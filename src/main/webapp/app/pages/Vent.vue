<template>
  <div class="page theme-vent">
    <div class="page-header">
      <h2 class="page-title">Vent</h2>
      <p class="page-subtitle">Vitesse, direction et rafales</p>
    </div>
    <div class="grid">
      <ChartCard title="Vitesse moyenne" />
      <ChartCard title="Rose des vents" />
      <ChartCard title="Rafales maximales" />
      <ChartCard title="Evolution sur 24h" />
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

.theme-vent {
  background: linear-gradient(160deg, #001a1f 0%, #065666 40%, #0ea5e9 100%);
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
