<template>
  <div class="app">
    <FilterDrawer />
    <Sidebar />
    <main class="content">
      <RouterView v-slot="{ Component }">
        <Transition name="fade" mode="out-in">
          <component :is="Component" :key="$route.path" />
        </Transition>
      </RouterView>
    </main>
  </div>
</template>

<script setup>
import Sidebar from './components/Sidebar.vue'
import FilterDrawer from './components/FilterDrawer.vue'
</script>

<style scoped>
.app {
  min-height: 100vh;
}

.content {
  min-height: 100vh;
  padding-top: 64px;
}

@media (min-width: 1024px) {
  .content {
    margin-left: 240px;
    padding-top: 48px;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.18s ease, transform 0.18s ease;
}

.fade-enter-from {
  opacity: 0;
  transform: translateY(6px);
}

.fade-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}
</style>
