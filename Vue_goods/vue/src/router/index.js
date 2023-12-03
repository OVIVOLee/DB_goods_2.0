import Vue from 'vue'
import VueRouter from 'vue-router'
import Manage from '../views/Manage.vue'
import User from "@/views/User.vue";
import Good from "@/views/Good.vue";
import store from "@/store";
import Restock from "@/views/Restock.vue";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Manage,
    redirect: "/user",
    children: [
      { path:'/user', name: 'User', component: User },
      { path:'/good', name: 'Good', component: Good },
      { path:'/restock', name: 'Restock', component: Restock }
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//路由守卫
router.beforeEach((to,from,next) => {
  localStorage.setItem("currentPathName", to.name)//设置当前路由名称,为了在Header组件中使用
  store.commit("setPath")
  next()  //放行路由
})

export default router
