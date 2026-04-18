import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useFilterStore = defineStore('filter', () => {
  const selectedPeriod = ref('jour')
  const historyLevels = ref(1)

  function setPeriod(period) {
    selectedPeriod.value = period
  }

  function setHistoryLevels(n) {
    historyLevels.value = n
  }

  return { selectedPeriod, historyLevels, setPeriod, setHistoryLevels }
})
