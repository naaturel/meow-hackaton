const LEVEL_MULTIPLIERS = [1.0, 0.88, 0.78, 0.70]
const JITTER = [1.00, 0.97, 1.03, 0.96, 1.04, 0.98, 1.02, 0.95, 1.05, 0.99, 1.01, 0.96]

const BORDER_OPACITIES = [1.0, 0.60, 0.40, 0.22]
const FILL_OPACITIES   = [0.15, 0.08, 0.04, 0.02]
const BAR_OPACITIES    = [1.0, 0.55, 0.38, 0.22]

const HISTORY_LABELS = {
  heure:   ["H actuelle", "H -1",      "H -2",      "H -3"],
  jour:    ["Aujourd'hui","Hier",       "J -2",       "J -3"],
  semaine: ["Cette sem.", "Sem. -1",   "Sem. -2",   "Sem. -3"],
  mois:    ["Ce mois",    "Mois -1",   "Mois -2",   "Mois -3"],
}

function hexToRgb(hex) {
  const clean = hex.replace('#', '')
  return [
    parseInt(clean.slice(0, 2), 16),
    parseInt(clean.slice(2, 4), 16),
    parseInt(clean.slice(4, 6), 16),
  ]
}

function parseColor(color) {
  if (!color || typeof color !== 'string') return [150, 150, 150]
  if (color.startsWith('#')) return hexToRgb(color)
  const m = color.match(/rgba?\((\d+),\s*(\d+),\s*(\d+)/)
  if (m) return [+m[1], +m[2], +m[3]]
  return [150, 150, 150]
}

function shiftValues(data, level) {
  return data.map((v, i) => {
    if (typeof v !== 'number') return v
    const shifted = v * LEVEL_MULTIPLIERS[level] * JITTER[i % JITTER.length]
    return Number.isInteger(v) ? Math.round(shifted) : Math.round(shifted * 10) / 10
  })
}

export function buildHistoricalData(baseData, historyLevels, period, chartType) {
  if (historyLevels === 1 || chartType === 'doughnut') return baseData

  const baseDataset = baseData.datasets[0]
  const isArrayBg = Array.isArray(baseDataset.backgroundColor)
  if (isArrayBg) return baseData  // zone charts: pas de sens d'empiler

  const labels = HISTORY_LABELS[period] ?? ['Actuel', '-1', '-2', '-3']
  const baseColor = parseColor(chartType === 'line' ? baseDataset.borderColor : baseDataset.backgroundColor)
  const [r, g, b] = baseColor

  const datasets = []
  for (let i = 0; i < historyLevels; i++) {
    const data = i === 0 ? baseDataset.data : shiftValues(baseDataset.data, i)

    if (chartType === 'line') {
      datasets.push({
        ...baseDataset,
        label: labels[i],
        data,
        borderColor: `rgba(${r},${g},${b},${BORDER_OPACITIES[i]})`,
        backgroundColor: `rgba(${r},${g},${b},${FILL_OPACITIES[i]})`,
      })
    } else {
      datasets.push({
        ...baseDataset,
        label: labels[i],
        data,
        backgroundColor: `rgba(${r},${g},${b},${BAR_OPACITIES[i]})`,
      })
    }
  }

  return { labels: baseData.labels, datasets }
}
