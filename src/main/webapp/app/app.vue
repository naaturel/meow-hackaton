<template>
  <div class="app">
    <FilterDrawer />
    <Sidebar />
    <ChatBot />
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
import { onMounted, onBeforeUnmount } from 'vue'
import Sidebar from './components/Sidebar.vue'
import FilterDrawer from './components/FilterDrawer.vue'
import ChatBot from './components/ChatBot.vue'
import { useRealtimeStore } from './stores/realtime.js'

const realtime = useRealtimeStore()
onMounted(() => realtime.connect())
onBeforeUnmount(() => realtime.disconnect())
</script>

<style scoped>
.app {
  min-height: 100vh;
}

.content {
  min-height: 100vh;
  padding-top: 64px;
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
