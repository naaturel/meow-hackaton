<template>
  <div>
    <button class="hamburger" @click="toggle" :class="{ open: isOpen }">
      <span></span>
      <span></span>
      <span></span>
    </button>

    <div class="overlay" v-if="isOpen" @click="close" />

    <nav class="sidebar" :class="{ open: isOpen }">
      <div class="sidebar-header">Dashboard</div>

      <div class="nav-section-label">Energie</div>
      <RouterLink to="/electricite" class="nav-link" @click="close">Electricite</RouterLink>
      <RouterLink to="/gaz" class="nav-link" @click="close">Gaz</RouterLink>
      <RouterLink to="/temperature" class="nav-link" @click="close">Temperature</RouterLink>

      <div class="nav-section-label">Environnement</div>
      <RouterLink to="/eau" class="nav-link" @click="close">Eau</RouterLink>
      <RouterLink to="/vent" class="nav-link" @click="close">Vent</RouterLink>
      <RouterLink to="/air" class="nav-link" @click="close">Qualite de l'air</RouterLink>

      <div class="nav-section-label">Production</div>
      <RouterLink to="/industriel" class="nav-link" @click="close">Capteurs industriels</RouterLink>
    </nav>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const isOpen = ref(false)
const toggle = () => isOpen.value = !isOpen.value
const close = () => isOpen.value = false
</script>

<style scoped>
.hamburger {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 300;
  background: #1e1e2e;
  border: none;
  width: 64px;
  height: 64px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 6px;
  flex-shrink: 0;
  transition: left 0.3s ease;
}

.hamburger.open {
  left: 240px;
}

.hamburger span {
  display: block;
  width: 24px;
  height: 2px;
  background: #ccc;
  border-radius: 2px;
  transition: transform 0.3s ease, opacity 0.3s ease;
  transform-origin: center;
}

.hamburger.open span:nth-child(1) { transform: translateY(8px) rotate(45deg); }
.hamburger.open span:nth-child(2) { opacity: 0; transform: scaleX(0); }
.hamburger.open span:nth-child(3) { transform: translateY(-8px) rotate(-45deg); }

.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 100;
}

.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 240px;
  background: #1e1e2e;
  z-index: 200;
  transform: translateX(-240px);
  transition: transform 0.3s ease;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.sidebar.open { transform: translateX(0); }

.sidebar-header {
  height: 64px;
  padding-left: 72px;
  display: flex;
  align-items: center;
  color: #fff;
  font-size: 1.1rem;
  font-weight: 700;
  letter-spacing: 0.03em;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  flex-shrink: 0;
}

.nav-section-label {
  padding: 20px 20px 6px;
  font-size: 0.7rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: rgba(255, 255, 255, 0.35);
}

.nav-link {
  display: block;
  padding: 11px 20px;
  color: rgba(255, 255, 255, 0.6);
  text-decoration: none;
  font-size: 0.9rem;
  transition: background 0.15s, color 0.15s;
  border-left: 3px solid transparent;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.06);
  color: #fff;
}

.nav-link.router-link-active {
  color: #fff;
  border-left-color: #60a5fa;
  background: rgba(96, 165, 250, 0.1);
}
</style>
