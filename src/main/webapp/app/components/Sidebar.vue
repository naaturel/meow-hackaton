<template>
  <div>
    <button class="hamburger" @click="toggle" :class="{ open: isOpen }">
      <span></span>
      <span></span>
      <span></span>
    </button>

    <div class="overlay" v-if="isOpen" @click="close" />

    <nav class="sidebar" :class="{ open: isOpen }">
      <div class="sidebar-header">
        <div class="brand">
          <span class="brand-name">Dashboard</span>
        </div>
      </div>

      <div class="nav-body">
        <div class="nav-section">
          <RouterLink to="/" class="nav-link" :style="{ '--dot-color': '#6366f1' }" @click="close">
            <span class="dot" style="background:#6366f1"></span>
            <span>Vue d'ensemble</span>
          </RouterLink>
        </div>

        <div class="nav-section">
          <div class="nav-section-label">Énergie</div>
          <RouterLink to="/electricite" class="nav-link" :style="{ '--dot-color': '#f59e0b' }" @click="close">
            <span class="dot" style="background:#f59e0b"></span>
            <span>Électricité</span>
          </RouterLink>
          <RouterLink to="/gaz" class="nav-link" :style="{ '--dot-color': '#8b5cf6' }" @click="close">
            <span class="dot" style="background:#8b5cf6"></span>
            <span>Gaz</span>
          </RouterLink>
          <RouterLink to="/temperature" class="nav-link" :style="{ '--dot-color': '#ef4444' }" @click="close">
            <span class="dot" style="background:#ef4444"></span>
            <span>Température</span>
          </RouterLink>
        </div>

        <div class="nav-section">
          <div class="nav-section-label">Environnement</div>
          <RouterLink to="/eau" class="nav-link" :style="{ '--dot-color': '#3b82f6' }" @click="close">
            <span class="dot" style="background:#3b82f6"></span>
            <span>Eau</span>
          </RouterLink>
          <RouterLink to="/vent" class="nav-link" :style="{ '--dot-color': '#06b6d4' }" @click="close">
            <span class="dot" style="background:#06b6d4"></span>
            <span>Vent</span>
          </RouterLink>
          <RouterLink to="/air" class="nav-link" :style="{ '--dot-color': '#22c55e' }" @click="close">
            <span class="dot" style="background:#22c55e"></span>
            <span>Qualité de l'air</span>
          </RouterLink>
        </div>

        <div class="nav-section">
          <div class="nav-section-label">Production</div>
          <RouterLink to="/industriel" class="nav-link" :style="{ '--dot-color': '#94a3b8' }" @click="close">
            <span class="dot" style="background:#94a3b8"></span>
            <span>Capteurs industriels</span>
          </RouterLink>
        </div>
      </div>
    </nav>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const isOpen = ref(false)
onMounted(() => { if (window.innerWidth >= 1024) isOpen.value = true })
const toggle = () => isOpen.value = !isOpen.value
const close = () => isOpen.value = false
</script>

<style scoped>
.hamburger {
  position: fixed;
  top: 12px;
  left: 12px;
  z-index: 300;
  background: #1a1a2e;
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 10px;
  width: 40px;
  height: 40px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 5px;
  transition: left 0.3s ease, opacity 0.2s ease;
}

.hamburger.open {
  left: 256px;
}

.hamburger:hover {
  background: rgba(255, 255, 255, 0.08);
}

.hamburger span {
  display: block;
  width: 18px;
  height: 2px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 2px;
  transition: transform 0.3s ease, opacity 0.3s ease;
  transform-origin: center;
}

.hamburger.open span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
.hamburger.open span:nth-child(2) { opacity: 0; transform: scaleX(0); }
.hamburger.open span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 100;
  backdrop-filter: blur(2px);
}

.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 240px;
  background: #0f0f1a;
  border-right: 1px solid rgba(255, 255, 255, 0.06);
  z-index: 200;
  transform: translateX(-240px);
  transition: transform 0.3s ease;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.sidebar.open { transform: translateX(0); }

.sidebar-header {
  height: 64px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
  flex-shrink: 0;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
}

.brand-icon {
  font-size: 1.2rem;
  line-height: 1;
}

.brand-name {
  font-size: 1rem;
  font-weight: 700;
  color: #fff;
  letter-spacing: 0.02em;
}

.nav-body {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0 20px;
  scrollbar-width: none;
}

.nav-body::-webkit-scrollbar { display: none; }

.nav-section {
  margin-bottom: 4px;
}

.nav-section-label {
  padding: 16px 20px 6px;
  font-size: 0.65rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.12em;
  color: rgba(255, 255, 255, 0.25);
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  margin: 2px 8px;
  color: rgba(255, 255, 255, 0.55);
  text-decoration: none;
  font-size: 0.875rem;
  border-radius: 8px;
  transition: background 0.15s, color 0.15s;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.06);
  color: rgba(255, 255, 255, 0.9);
}

.nav-link.router-link-active {
  background: color-mix(in srgb, var(--dot-color) 15%, transparent);
  color: var(--dot-color);
  font-weight: 600;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
  opacity: 0.7;
  transition: opacity 0.15s;
}

.nav-link:hover .dot,
.nav-link.router-link-active .dot {
  opacity: 1;
}

@media (max-width: 640px) {
  .sidebar {
    width: 100vw;
    transform: translateX(-100vw);
  }

  .hamburger.open {
    left: calc(100vw - 52px);
  }

  .nav-link {
    padding: 13px 16px;
    font-size: 1rem;
  }
}
</style>
