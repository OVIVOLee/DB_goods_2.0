import Vue from 'vue'
import VueRouter from 'vue-router'
import Manage from '../views/Manage.vue'
import User from "@/views/User.vue";
import Good from "@/views/Good.vue";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Manage',
    component: Manage,
    redirect: "/user",
    children: [
      { path:'/user', name: 'User', component: User },
      { path:'/good', name: 'Good', component: Good }
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
