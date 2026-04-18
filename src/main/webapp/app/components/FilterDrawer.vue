<template>
  <div class="drawer" :class="{ open: isOpen }">
    <div
      class="handle"
      @mousedown="onDragStart"
      @touchstart.passive="onDragStart"
      @click="onHandleClick"
    >
      <span class="handle-pill" />
    </div>

    <div class="panel">
      <p class="panel-label">Période</p>
      <div class="period-buttons">
        <button
          v-for="p in periods"
          :key="p.value"
          class="period-btn"
          :class="{ active: filterStore.selectedPeriod === p.value }"
          @click="filterStore.setPeriod(p.value)"
        >
          {{ p.label }}
        </button>
      </div>
    </div>
  </div>

  <div v-if="isOpen" class="backdrop" @click="isOpen = false" />
</template>

<script setup>
import { ref } from 'vue'
import { useFilterStore } from '../stores/filter.js'

const filterStore = useFilterStore()
const isOpen = ref(false)

const periods = [
  { value: 'heure', label: 'Heure' },
  { value: 'jour', label: 'Jour' },
  { value: 'semaine', label: 'Semaine' },
  { value: 'mois', label: 'Mois' },
]

let dragStartY = 0
let isDragIntent = false

function onDragStart(e) {
  dragStartY = e.touches ? e.touches[0].clientY : e.clientY
  isDragIntent = false

  const onMove = (ev) => {
    const y = ev.touches ? ev.touches[0].clientY : ev.clientY
    if (Math.abs(y - dragStartY) > 8) isDragIntent = true
  }

  const onEnd = (ev) => {
    const y = ev.changedTouches ? ev.changedTouches[0].clientY : ev.clientY
    const delta = y - dragStartY
    if (isDragIntent) {
      if (delta > 20) isOpen.value = true
      else if (delta < -20) isOpen.value = false
    }
    window.removeEventListener('mousemove', onMove)
    window.removeEventListener('mouseup', onEnd)
    window.removeEventListener('touchmove', onMove)
    window.removeEventListener('touchend', onEnd)
  }

  window.addEventListener('mousemove', onMove)
  window.addEventListener('mouseup', onEnd)
  window.addEventListener('touchmove', onMove, { passive: true })
  window.addEventListener('touchend', onEnd)
}

function onHandleClick() {
  if (!isDragIntent) isOpen.value = !isOpen.value
}
</script>

<style scoped>
.drawer {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 200;
  display: flex;
  flex-direction: column;
  pointer-events: none;
}

.handle {
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  pointer-events: all;
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(8px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  user-select: none;
}

.handle-pill {
  width: 36px;
  height: 4px;
  border-radius: 2px;
  background: rgba(255, 255, 255, 0.4);
  transition: background 0.2s;
}

.handle:hover .handle-pill {
  background: rgba(255, 255, 255, 0.7);
}

.panel {
  background: rgba(20, 20, 30, 0.85);
  backdrop-filter: blur(16px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 0 24px;
  pointer-events: all;
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s ease, padding 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.drawer.open .panel {
  max-height: 120px;
  padding: 16px 24px;
}

.panel-label {
  margin: 0 0 10px;
  font-size: 0.7rem;
  font-weight: 600;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: rgba(255, 255, 255, 0.4);
}

.period-buttons {
  display: flex;
  gap: 8px;
}

.period-btn {
  padding: 6px 18px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  background: transparent;
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.15s ease;
}

.period-btn:hover {
  border-color: rgba(255, 255, 255, 0.5);
  color: #fff;
}

.period-btn.active {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.7);
  color: #fff;
  font-weight: 600;
}

.backdrop {
  position: fixed;
  inset: 0;
  z-index: 199;
  background: transparent;
}
</style>
