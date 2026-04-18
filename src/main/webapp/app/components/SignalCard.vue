<template>
  <div class="signal-card">
    <div class="signal-top">
      <span class="signal-label">FIABILITÉ DU SIGNAL</span>
      <span class="live-dot"></span>
    </div>
    <div class="signal-body">
      <div class="signal-meta">
        <div class="signal-value-row">
          <span class="signal-value">{{ currentVal }}</span>
          <span class="signal-unit">dB</span>
        </div>
        <span class="signal-status">Signal fort</span>
      </div>
      <div class="canvas-wrap" ref="wrapRef">
        <canvas ref="canvasRef" class="ecg-canvas"></canvas>
        <div class="tick-row">
          <span v-for="tick in tickLabels" :key="tick" class="tick-label">{{ tick }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'

const props = defineProps({
  snrValue: { type: Number, default: null },
})

const canvasRef = ref(null)
const wrapRef = ref(null)
const currentVal = ref('—')

const BUFFER = 120
const INTERVAL = 80 // ms — scroll speed between real data points

const tickLabels = ['', '', '', '', '', '']

let buffer = []
let latestSnr = null
let rafId = null
let lastTick = 0
let logW = 0
let logH = 0

watch(() => props.snrValue, (v) => {
  if (v != null) {
    latestSnr = v
    currentVal.value = v.toFixed(1)
  }
})

function initBuffer() {
  buffer = []
  // Start at a neutral SNR so wiggle isn't multiplying by 0
  for (let i = 0; i < BUFFER; i++) buffer.push(8)
}

function draw() {
  const canvas = canvasRef.value
  if (!canvas || logW === 0) return
  const ctx = canvas.getContext('2d')
  const dpr = window.devicePixelRatio || 1

  ctx.clearRect(0, 0, logW * dpr, logH * dpr)

  ctx.save()
  ctx.scale(dpr, dpr)

  const w = logW
  const h = logH

  // Subtle horizontal grid lines
  ctx.strokeStyle = 'rgba(245,158,11,0.07)'
  ctx.lineWidth = 1
  for (let i = 1; i < 4; i++) {
    ctx.beginPath()
    ctx.moveTo(0, (h / 4) * i)
    ctx.lineTo(w, (h / 4) * i)
    ctx.stroke()
  }

  // Fade mask: left edge fades out old data
  const grad = ctx.createLinearGradient(0, 0, w * 0.12, 0)
  grad.addColorStop(0, 'rgba(245,158,11,0)')
  grad.addColorStop(1, 'rgba(245,158,11,1)')

  // Dynamic Y range: zoom in on actual buffer values so ±1% wiggle is visible
  const bufMin = Math.min(...buffer)
  const bufMax = Math.max(...buffer)
  const spread = Math.max(0.4, bufMax - bufMin)
  const pad    = spread * 1.2
  const MIN    = bufMin - pad
  const MAX    = bufMax + pad
  const RANGE  = MAX - MIN

  // Glow pass (wider, transparent)
  ctx.beginPath()
  ctx.shadowColor = 'rgba(245,158,11,0.55)'
  ctx.shadowBlur = 8
  ctx.strokeStyle = 'rgba(245,158,11,0.35)'
  ctx.lineWidth = 4
  ctx.lineJoin = 'round'
  ctx.lineCap = 'round'
  buffer.forEach((v, i) => {
    const x = (i / (BUFFER - 1)) * w
    const y = h * 0.85 - ((v - MIN) / RANGE) * (h * 0.7)
    if (i === 0) ctx.moveTo(x, y)
    else ctx.lineTo(x, y)
  })
  ctx.stroke()
  ctx.shadowBlur = 0

  // Main line
  ctx.beginPath()
  ctx.strokeStyle = '#f59e0b'
  ctx.lineWidth = 2
  ctx.lineJoin = 'round'
  ctx.lineCap = 'round'
  buffer.forEach((v, i) => {
    const x = (i / (BUFFER - 1)) * w
    const y = h * 0.85 - ((v - MIN) / RANGE) * (h * 0.7)
    if (i === 0) ctx.moveTo(x, y)
    else ctx.lineTo(x, y)
  })
  ctx.stroke()

  // Scan cursor at the right edge
  const cx = w - 2
  ctx.strokeStyle = 'rgba(245,158,11,0.5)'
  ctx.lineWidth = 1.5
  ctx.setLineDash([3, 4])
  ctx.beginPath()
  ctx.moveTo(cx, 0)
  ctx.lineTo(cx, h)
  ctx.stroke()
  ctx.setLineDash([])

  // Dot at current position
  const lastV = buffer[buffer.length - 1]
  const dotY = h * 0.85 - ((lastV - MIN) / RANGE) * (h * 0.7)
  ctx.beginPath()
  ctx.arc(cx, dotY, 3.5, 0, Math.PI * 2)
  ctx.fillStyle = '#fde68a'
  ctx.shadowColor = '#f59e0b'
  ctx.shadowBlur = 10
  ctx.fill()
  ctx.shadowBlur = 0

  ctx.restore()
}

function frame(now) {
  if (now - lastTick >= INTERVAL) {
    const base = latestSnr !== null ? latestSnr : buffer[buffer.length - 1]
    // ±1% wiggle on the plotted line only — display value stays clean
    const wiggle = base * 0.01 * (Math.random() * 2 - 1)
    buffer.shift()
    buffer.push(base + wiggle)
    lastTick = now
    draw()
  }
  rafId = requestAnimationFrame(frame)
}

onMounted(() => {
  initBuffer()
  const canvas = canvasRef.value
  const wrap = wrapRef.value
  if (!canvas || !wrap) return

  const dpr = window.devicePixelRatio || 1
  const rect = wrap.getBoundingClientRect()
  logW = rect.width
  logH = rect.height - 18 // leave space for tick row

  canvas.width = Math.round(logW * dpr)
  canvas.height = Math.round(logH * dpr)
  canvas.style.width = logW + 'px'
  canvas.style.height = logH + 'px'

  draw()
  rafId = requestAnimationFrame(frame)
})

onBeforeUnmount(() => {
  if (rafId) cancelAnimationFrame(rafId)
})
</script>

<style scoped>
.signal-card {
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

.signal-card:hover {
  transform: translateY(-2px);
  background: rgba(255, 255, 255, 0.07);
}

.signal-top {
  background: linear-gradient(to right, color-mix(in srgb, #f59e0b 10%, transparent), transparent 70%);
  margin: -18px -22px 0;
  padding: 12px 22px 10px;
  border-radius: 13px 13px 0 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.signal-label {
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
  50% { opacity: 0.3; }
}

.signal-body {
  display: flex;
  gap: 14px;
  align-items: center;
  flex: 1;
  min-height: 0;
}

.signal-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex-shrink: 0;
}

.signal-value-row {
  display: flex;
  align-items: baseline;
  gap: 3px;
}

.signal-value {
  font-size: 1.9rem;
  font-weight: 800;
  color: #fff;
  line-height: 1;
  letter-spacing: -0.03em;
  font-variant-numeric: tabular-nums;
}

.signal-unit {
  font-size: 0.8rem;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.4);
}

.signal-status {
  font-size: 0.65rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: #22c55e;
}

.canvas-wrap {
  flex: 1;
  height: 70px;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.ecg-canvas {
  display: block;
  flex: 1;
}

.tick-row {
  display: flex;
  justify-content: space-between;
  padding: 0 2px;
  height: 18px;
}

.tick-label {
  font-size: 0.6rem;
  color: rgba(255, 255, 255, 0.2);
  font-variant-numeric: tabular-nums;
  position: relative;
}

.tick-label::before {
  content: '|';
  display: block;
  text-align: center;
  color: rgba(245, 158, 11, 0.25);
  font-size: 0.55rem;
  margin-bottom: 1px;
}
</style>
