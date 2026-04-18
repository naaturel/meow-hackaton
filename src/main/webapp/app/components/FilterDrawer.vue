<template>
  <div class="drawer" :class="{ open: isOpen }">
    <div
      class="handle"
      @mousedown="onDragStart"
      @touchstart.passive="onDragStart"
      @click="onHandleClick"
    >
      <span class="handle-pill" />
      <div class="handle-live">
        <span class="live-dot"></span>
        <span class="live-label">LIVE</span>
      </div>
    </div>

    <div class="panel">
      <div class="controls">
        <div class="control-group">
          <p class="group-label">Période</p>
          <div class="btn-row">
            <button
              v-for="p in periods"
              :key="p.value"
              class="ctrl-btn"
              :class="{ active: filterStore.selectedPeriod === p.value }"
              @click="filterStore.setPeriod(p.value)"
            >
              {{ p.label }}
            </button>
          </div>
        </div>

        <div class="divider" />

        <div class="control-group">
          <p class="group-label">Historique</p>
          <div class="btn-row">
            <button
              v-for="n in [1, 2, 3, 4]"
              :key="n"
              class="ctrl-btn history-btn"
              :class="{ active: filterStore.historyLevels === n }"
              @click="filterStore.setHistoryLevels(n)"
              :title="historyTitles[n - 1]"
            >
              {{ n }}
            </button>
          </div>
        </div>
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
  { value: 'heure',   label: 'Heure' },
  { value: 'jour',    label: 'Jour' },
  { value: 'semaine', label: 'Semaine' },
  { value: 'mois',    label: 'Mois' },
]

const historyTitles = [
  'Période actuelle uniquement',
  'Actuel + période précédente',
  'Actuel + 2 périodes précédentes',
  'Actuel + 3 périodes précédentes',
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
  height: 40px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 16px;
  cursor: pointer;
  pointer-events: all;
  background: rgba(15, 15, 26, 0.92);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.07);
  user-select: none;
  transition: background 0.2s;
}

.handle:hover {
  background: rgba(255, 255, 255, 0.05);
}

.handle-live {
  display: flex;
  align-items: center;
  gap: 6px;
}

.live-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #22c55e;
  box-shadow: 0 0 6px #22c55e;
  animation: live-pulse 2s ease-in-out infinite;
}

@keyframes live-pulse {
  0%, 100% { opacity: 1; box-shadow: 0 0 6px #22c55e; }
  50%       { opacity: 0.5; box-shadow: 0 0 2px #22c55e; }
}

.live-label {
  font-size: 0.6rem;
  font-weight: 700;
  letter-spacing: 0.12em;
  color: #22c55e;
}

.handle-pill {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  width: 40px;
  height: 3px;
  border-radius: 2px;
  background: rgba(255, 255, 255, 0.25);
  transition: background 0.2s, width 0.2s;
}

.handle:hover .handle-pill {
  background: rgba(255, 255, 255, 0.55);
  width: 52px;
}

.panel {
  background: rgba(13, 13, 22, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.07);
  padding: 0 24px;
  pointer-events: all;
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.35s cubic-bezier(0.4, 0, 0.2, 1), padding 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  justify-content: center;
  width: 100%;
  box-sizing: border-box;
}

.drawer.open .panel {
  max-height: 120px;
  padding: 18px 24px;
}

.controls {
  display: flex;
  align-items: center;
  gap: 24px;
  width: 100%;
  box-sizing: border-box;
}

.control-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.group-label {
  margin: 0;
  font-size: 0.65rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  color: rgba(255, 255, 255, 0.3);
}

.btn-row {
  display: flex;
  gap: 6px;
}

.divider {
  width: 1px;
  height: 52px;
  background: rgba(255, 255, 255, 0.08);
}

.ctrl-btn {
  padding: 6px 16px;
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.04);
  color: rgba(255, 255, 255, 0.45);
  font-size: 0.82rem;
  font-family: inherit;
  cursor: pointer;
  transition: all 0.15s ease;
}

.ctrl-btn:hover {
  border-color: rgba(255, 255, 255, 0.3);
  color: rgba(255, 255, 255, 0.85);
  background: rgba(255, 255, 255, 0.08);
}

.ctrl-btn.active {
  background: rgba(99, 102, 241, 0.2);
  border-color: rgba(99, 102, 241, 0.6);
  color: #a5b4fc;
  font-weight: 600;
  box-shadow: 0 0 12px rgba(99, 102, 241, 0.15);
}

.history-btn {
  padding: 6px 12px;
  min-width: 38px;
  text-align: center;
}

.backdrop {
  position: fixed;
  inset: 0;
  z-index: 199;
  background: transparent;
}

@media (max-width: 640px) {
  .drawer.open .panel {
    max-height: 300px;
    padding: 14px 12px;
  }

  .controls {
    flex-direction: column;
    gap: 12px;
    width: 100%;
    max-width: 100%;
    overflow: hidden;
  }

  .divider {
    width: 100%;
    height: 1px;
  }

  .control-group {
    width: 100%;
  }

  .btn-row {
    flex-wrap: wrap;
    justify-content: center;
    width: 100%;
  }

  .ctrl-btn {
    padding: 6px 12px;
    font-size: 0.78rem;
  }
}
</style>
