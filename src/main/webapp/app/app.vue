<template>
  <div class="app">
    <Sidebar />
    <main class="content">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import Sidebar from './components/Sidebar.vue'
</script>

<style scoped>
.app {
  min-height: 100vh;
}

.content {
  min-height: 100vh;
}
</style>
