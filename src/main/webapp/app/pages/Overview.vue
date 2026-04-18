<template>
  <div class="page theme-overview">
    <div class="page-header">
      <h2 class="page-title">Vue d'ensemble</h2>
      <p class="page-subtitle">Résumé en temps réel de tous les capteurs</p>
    </div>

    <div class="overview-grid">
      <RouterLink
        v-for="cat in categories"
        :key="cat.path"
        :to="cat.path"
        class="cat-card"
        :style="{ '--accent': cat.color }"
      >
        <div class="cat-top">
          <span class="cat-dot" :style="{ background: cat.color }"></span>
          <span class="cat-name">{{ cat.name }}</span>
          <span class="cat-status" :class="cat.status">{{ cat.statusLabel }}</span>
        </div>
        <div class="cat-metric">{{ cat.value }}<span class="cat-unit"> {{ cat.unit }}</span></div>
        <div class="cat-label">{{ cat.metricLabel }}</div>
        <div class="cat-trend" :class="cat.trendStatus">
          <span v-if="cat.trend === 'up'">↑</span>
          <span v-else-if="cat.trend === 'down'">↓</span>
          <span v-else>→</span>
          {{ cat.trendLabel }}
        </div>
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
const categories = [
  {
    path: '/electricite',
    name: 'Électricité',
    color: '#f59e0b',
    value: '480',
    unit: 'kW',
    metricLabel: 'Puissance actuelle',
    trend: 'up',
    trendLabel: '+8% vs hier',
    trendStatus: 'warn',
    status: 'active',
    statusLabel: 'Actif',
  },
  {
    path: '/gaz',
    name: 'Gaz',
    color: '#8b5cf6',
    value: '5.8',
    unit: 'bar',
    metricLabel: 'Pression réseau',
    trend: 'flat',
    trendLabel: 'Stable',
    trendStatus: 'neutral',
    status: 'active',
    statusLabel: 'Actif',
  },
  {
    path: '/temperature',
    name: 'Température',
    color: '#ef4444',
    value: '19',
    unit: '°C',
    metricLabel: 'Température actuelle',
    trend: 'up',
    trendLabel: '+3°C vs normale',
    trendStatus: 'warn',
    status: 'warn',
    statusLabel: 'Anomalie',
  },
  {
    path: '/eau',
    name: 'Eau',
    color: '#3b82f6',
    value: '22',
    unit: 'L/s',
    metricLabel: 'Débit actuel',
    trend: 'down',
    trendLabel: '-5% vs hier',
    trendStatus: 'good',
    status: 'active',
    statusLabel: 'Normal',
  },
  {
    path: '/vent',
    name: 'Vent',
    color: '#06b6d4',
    value: '42',
    unit: 'km/h',
    metricLabel: 'Vitesse moyenne',
    trend: 'up',
    trendLabel: '+12% vs hier',
    trendStatus: 'warn',
    status: 'warn',
    statusLabel: 'Rafales',
  },
  {
    path: '/air',
    name: "Qualité de l'air",
    color: '#22c55e',
    value: '42',
    unit: 'IQA',
    metricLabel: 'Indice qualité de l\'air',
    trend: 'down',
    trendLabel: 'Bon niveau',
    trendStatus: 'good',
    status: 'active',
    statusLabel: 'Bon',
  },
  {
    path: '/industriel',
    name: 'Industriel',
    color: '#94a3b8',
    value: '3/4',
    unit: '',
    metricLabel: 'Lignes actives',
    trend: 'flat',
    trendLabel: '2 alertes actives',
    trendStatus: 'warn',
    status: 'warn',
    statusLabel: '2 alertes',
  },
]
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

.theme-overview {
  --accent: #6366f1;
}

.theme-overview::before {
  content: '';
  position: absolute;
  top: -120px;
  left: 50%;
  transform: translateX(-50%);
  width: 700px;
  height: 400px;
  background: radial-gradient(ellipse, rgba(99, 102, 241, 0.18) 0%, transparent 70%);
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
  color: #6366f1;
  letter-spacing: -0.01em;
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-title::before {
  content: '';
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #22c55e;
  flex-shrink: 0;
  animation: dot-pulse 2s ease-in-out infinite;
}

.page-subtitle {
  margin: 0;
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.45);
}

.overview-grid {
  position: relative;
  flex: 1;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  padding: 0 28px 28px;
  align-content: start;
}

.cat-card {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  padding: 22px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  position: relative;
  overflow: hidden;
  text-decoration: none;
  transition: transform 0.2s ease, background 0.2s ease, box-shadow 0.2s ease;
}

.cat-card::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--accent), transparent 70%);
}

.cat-card:hover {
  transform: translateY(-3px);
  background: rgba(255, 255, 255, 0.07);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

.cat-top {
  display: flex;
  align-items: center;
  gap: 8px;
}

.cat-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.cat-name {
  font-size: 0.78rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.09em;
  color: rgba(255, 255, 255, 0.5);
  flex: 1;
}

.cat-status {
  font-size: 0.65rem;
  font-weight: 700;
  padding: 2px 8px;
  border-radius: 20px;
  letter-spacing: 0.06em;
}

.cat-status.active  { background: rgba(34, 197, 94, 0.15); color: #22c55e; }
.cat-status.warn    { background: rgba(245, 158, 11, 0.15); color: #f59e0b; }
.cat-status.bad     { background: rgba(239, 68, 68, 0.15);  color: #ef4444; }

.cat-metric {
  font-size: 2.4rem;
  font-weight: 800;
  color: #fff;
  letter-spacing: -0.03em;
  line-height: 1;
}

.cat-unit {
  font-size: 1rem;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.4);
}

.cat-label {
  font-size: 0.78rem;
  color: rgba(255, 255, 255, 0.4);
}

.cat-trend {
  font-size: 0.75rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: 4px;
}

.cat-trend.good    { color: #22c55e; }
.cat-trend.warn    { color: #f59e0b; }
.cat-trend.bad     { color: #ef4444; }
.cat-trend.neutral { color: rgba(255, 255, 255, 0.35); }

@media (max-width: 1024px) {
  .overview-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 640px) {
  .overview-grid { grid-template-columns: 1fr; padding: 0 12px 12px; }
}
</style>
