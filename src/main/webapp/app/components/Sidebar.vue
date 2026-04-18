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
          <span class="brand-icon">⚡</span>
          <span class="brand-name">Dashboard</span>
        </div>
      </div>

      <div class="nav-body">
        <div class="nav-section">
          <div class="nav-section-label">Énergie</div>
          <RouterLink to="/electricite" class="nav-link" @click="handleNavClick('/electricite')">
            <span class="dot" style="background:#f59e0b"></span>
            <span>Électricité</span>
          </RouterLink>
          <RouterLink to="/gaz" class="nav-link" @click="handleNavClick('/gaz')">
            <span class="dot" style="background:#8b5cf6"></span>
            <span>Gaz</span>
          </RouterLink>
          <RouterLink to="/temperature" class="nav-link" @click="close">
            <span class="dot" style="background:#ef4444"></span>
            <span>Température</span>
          </RouterLink>
        </div>

        <div class="nav-section">
          <div class="nav-section-label">Environnement</div>
          <RouterLink to="/eau" class="nav-link" @click="handleNavClick('/eau')">
            <span class="dot" style="background:#3b82f6"></span>
            <span>Eau</span>
          </RouterLink>
          <RouterLink to="/vent" class="nav-link" @click="handleNavClick('/vent')">
            <span class="dot" style="background:#06b6d4"></span>
            <span>Vent</span>
          </RouterLink>
          <RouterLink to="/air" class="nav-link" @click="close">
            <span class="dot" style="background:#22c55e"></span>
            <span>Qualité de l'air</span>
          </RouterLink>
        </div>

        <div class="nav-section">
          <div class="nav-section-label">Production</div>
          <RouterLink to="/industriel" class="nav-link" @click="close">
            <span class="dot" style="background:#94a3b8"></span>
            <span>Capteurs industriels</span>
          </RouterLink>
        </div>
      </div>
    </nav>

    <!-- Meme Security Overlay -->
    <Teleport to="body">
      <div v-if="activeVideo" class="meme-overlay" @click="closeVideo">
        <video :src="activeVideo" autoplay loop playsinline class="meme-video" @click.stop />
        <button class="meme-close" @click="closeVideo">✕</button>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const isOpen = ref(false)
const toggle = () => (isOpen.value = !isOpen.value)
const close = () => (isOpen.value = false)

// Meme Security
const videoMap = {
  '/eau':        '/content/videos/video_jcvd.mp4',
  '/electricite':'/content/videos/pikachu.mp4',
  '/vent':       '/content/videos/vent.mp4',
  '/gaz':        '/content/videos/gaz.mp4',
}

const clickCounts = ref({})
const activeVideo = ref(null)

function handleNavClick(route) {
  close()
  clickCounts.value[route] = (clickCounts.value[route] || 0) + 1
  if (clickCounts.value[route] >= 6 && videoMap[route]) {
    activeVideo.value = videoMap[route]
    clickCounts.value[route] = 0
  }
}

function closeVideo() {
  activeVideo.value = null
}
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
  background: rgba(255, 255, 255, 0.08);
  color: #fff;
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

/* Meme Security */
.meme-overlay {
  position: fixed;
  inset: 0;
  z-index: 9999;
  background: #000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.meme-video {
  width: 100vw;
  height: 100vh;
  object-fit: cover;
}

.meme-close {
  position: fixed;
  top: 16px;
  right: 16px;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  font-size: 1.2rem;
  cursor: pointer;
  z-index: 10000;
  transition: background 0.15s;
}

.meme-close:hover {
  background: rgba(255, 255, 255, 0.2);
}
</style>