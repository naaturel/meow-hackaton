import { createRouter, createWebHashHistory } from 'vue-router'
import Overview from '../pages/Overview.vue'
import Electricite from '../pages/Electricite.vue'
import Eau from '../pages/Eau.vue'
import Gaz from '../pages/Gaz.vue'
import Vent from '../pages/Vent.vue'
import Air from '../pages/Air.vue'
import Industriel from '../pages/Industriel.vue'
import Temperature from '../pages/Temperature.vue'


const routes = [
  { path: '/', component: Overview },
  { path: '/electricite', component: Electricite },
  { path: '/eau', component: Eau },
  { path: '/gaz', component: Gaz },
  { path: '/vent', component: Vent },
  { path: '/air', component: Air },
  { path: '/industriel', component: Industriel },
  { path: '/temperature', component: Temperature },

]

export default createRouter({
  history: createWebHashHistory(),
  routes
})
