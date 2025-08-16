import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

import LoginView from '../views/LoginView.vue'
import Home from '../views/Home.vue'

// 定义路由规则
const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'LoginView',
        component: LoginView
    },
    {
        path: '/home',
        name: 'Home',
        component: Home
    },
]

// 创建路由器实例
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

export default router