import { createApp } from 'vue'
import App from './App.vue'

import './style.css'

import router from './router/index.ts' // 确保路由文件路径正确

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 1. 先创建 app 实例
const app = createApp(App)

// 2. 再注册图标（此时 app 已定义）
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 3. 挂载路由和 ElementPlus
app.use(router)
app.use(ElementPlus)

// 4. 挂载应用
app.mount('#app')