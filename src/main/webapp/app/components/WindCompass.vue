<template>
  <div class="wind-compass-card">
    <div class="wc-top">
      <span class="wc-label">DIRECTION DU VENT</span>
      <span class="live-dot"></span>
    </div>
    <div class="wc-body">
      <div class="wc-meta">
        <span class="wc-dir">{{ dirLabel }}</span>
        <span class="wc-sub">{{ speedLabel }}</span>
      </div>
      <div class="wc-svg-wrap">
        <svg viewBox="0 0 120 120" class="wc-svg" xmlns="http://www.w3.org/2000/svg">
          <!-- Outer ring -->
          <circle cx="60" cy="60" r="54" fill="rgba(255,255,255,0.03)" stroke="rgba(255,255,255,0.1)" stroke-width="1"/>
          <!-- Inner dashed ring -->
          <circle cx="60" cy="60" r="36" fill="none" stroke="rgba(255,255,255,0.06)" stroke-width="1" stroke-dasharray="3 5"/>

          <!-- Cardinal tick marks -->
          <line x1="60" y1="6"   x2="60" y2="14"  stroke="rgba(255,255,255,0.55)" stroke-width="2"/>
          <line x1="60" y1="106" x2="60" y2="114" stroke="rgba(255,255,255,0.2)"  stroke-width="1"/>
          <line x1="6"  y1="60"  x2="14" y2="60"  stroke="rgba(255,255,255,0.2)"  stroke-width="1"/>
          <line x1="106" y1="60" x2="114" y2="60" stroke="rgba(255,255,255,0.2)"  stroke-width="1"/>
          <!-- Inter-cardinal ticks -->
          <line x1="17.6" y1="17.6" x2="22.8" y2="22.8" stroke="rgba(255,255,255,0.15)" stroke-width="1"/>
          <line x1="97.2" y1="17.6" x2="102.4" y2="22.8" stroke="rgba(255,255,255,0.15)" stroke-width="1"/>
          <line x1="17.6" y1="102.4" x2="22.8" y2="97.2" stroke="rgba(255,255,255,0.15)" stroke-width="1"/>
          <line x1="97.2" y1="102.4" x2="102.4" y2="97.2" stroke="rgba(255,255,255,0.15)" stroke-width="1"/>

          <!-- Direction labels -->
          <text x="60"  y="4.5" text-anchor="middle" dominant-baseline="auto"  fill="rgba(255,255,255,0.85)" font-size="8.5" font-weight="700" font-family="Inter,sans-serif">N</text>
          <text x="60"  y="119" text-anchor="middle" dominant-baseline="auto"  fill="rgba(255,255,255,0.3)"  font-size="7.5" font-family="Inter,sans-serif">S</text>
          <text x="116" y="63.5" text-anchor="middle" dominant-baseline="auto" fill="rgba(255,255,255,0.3)"  font-size="7.5" font-family="Inter,sans-serif">E</text>
          <text x="4"   y="63.5" text-anchor="middle" dominant-baseline="auto" fill="rgba(255,255,255,0.3)"  font-size="7.5" font-family="Inter,sans-serif">O</text>

          <!-- Animated arrow group — points UP at 0° = North -->
          <g :style="`transform: rotate(${smoothDeg}deg); transform-origin: 60px 60px; transition: transform 1.4s cubic-bezier(0.25,0.46,0.45,0.94)`">
            <!-- Tip (north half of needle) -->
            <polygon points="60,16 55.5,52 60,58 64.5,52" fill="#06b6d4"/>
            <!-- Tail (south half) -->
            <polygon points="60,104 55.5,68 60,58 64.5,68" fill="rgba(255,255,255,0.18)"/>
            <!-- Center cap -->
            <circle cx="60" cy="60" r="5"   fill="#0e7490"/>
            <circle cx="60" cy="60" r="2.5" fill="#e0f2fe"/>
          </g>
        </svg>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useSimulatorStore } from '../stores/simulator.js'

const sim = useSimulatorStore()

// Cumulative angle to avoid CSS transition spinning the wrong way
let rawDeg = sim.vent.dirDeg
const smoothDeg = ref(rawDeg)

watch(() => sim.vent.dirDeg, (newDeg) => {
  const cur = rawDeg % 360
  let delta = ((newDeg - cur) % 360 + 360) % 360
  if (delta > 180) delta -= 360   // take the short way around
  rawDeg += delta
  smoothDeg.value = rawDeg
})

const dirLabel = computed(() => sim.degToDir(sim.vent.dirDeg))

const speedLabel = computed(() => `${sim.vent.vitesse} km/h`)
</script>

<style scoped>
.wind-compass-card {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 14px;
  padding: 18px 22px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  position: relative;
  overflow: hidden;
  transition: transform 0.2s ease, background 0.2s ease;
}

.wind-compass-card:hover {
  transform: translateY(-2px);
  background: rgba(255, 255, 255, 0.07);
}

.wc-top {
  background: linear-gradient(to right, color-mix(in srgb, #06b6d4 10%, transparent), transparent 70%);
  margin: -18px -22px 0;
  padding: 12px 22px 10px;
  border-radius: 13px 13px 0 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.wc-label {
  font-size: 0.68rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: rgba(255, 255, 255, 0.38);
}

.live-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #22c55e;
  box-shadow: 0 0 6px #22c55e;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50%       { opacity: 0.3; }
}

.wc-body {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
}

.wc-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex-shrink: 0;
}

.wc-dir {
  font-size: 2rem;
  font-weight: 800;
  color: #fff;
  line-height: 1;
  letter-spacing: -0.03em;
}

.wc-sub {
  font-size: 0.72rem;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.4);
}

.wc-svg-wrap {
  flex: 1;
  display: flex;
  justify-content: flex-end;
}

.wc-svg {
  width: 80px;
  height: 80px;
}
</style>
