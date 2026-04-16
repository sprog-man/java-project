// 导入Vue的createApp函数，用于创建Vue应用实例
import { createApp } from 'vue'
// 导入Pinia，用于状态管理
import { createPinia } from 'pinia'
// 导入ElementPlus UI组件库
import ElementPlus from 'element-plus'
// 导入ElementPlus的样式文件
import 'element-plus/dist/index.css'
// 导入App根组件
import App from './App.vue'
// 导入路由配置
import router from './router'
// 导入全局样式文件
import './styles/global.scss'

// 创建Vue应用实例
const app = createApp(App)

// 使用Pinia进行状态管理
app.use(createPinia())
// 使用ElementPlus UI组件库
app.use(ElementPlus)
// 使用路由
app.use(router)

// 将应用挂载到id为app的DOM元素上
app.mount('#app')
