<script setup>
import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)

defineProps({
  data: { type: Object, required: true },
  options: { type: Object, default: () => ({}) },
})
</script>

<template>
  <Bar :data="data" :options="options" />
</template>
