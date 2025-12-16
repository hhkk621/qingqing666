import { createApp } from 'vue'
import { createPinia } from 'pinia'
import persistedstate from "pinia-plugin-persistedstate"
import 'element-plus/dist/index.css'
import '@/assets/css/main.css'

// 导入 Element-Plus插件
import ElementPlus from 'element-plus'
//element-plus 全局 国际化插件
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

//element-plus 图标插件
import * as ElementPlusIconsVue from '@element-plus/icons-vue'


import App from './App.vue'
import router from './router'
// import 'bootstrap/dist/css/bootstrap.css'
// import * as bootstrap from 'bootstrap'
//总对象,整个页面
const app = createApp(App)
//配置ElementPlus插件给app实例，并配置国际化
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(ElementPlus, {
    locale: zhCn,
})
//创建pinia对象
const pinia = createPinia();
//pinia插件添加到pinia对象中
pinia.use(persistedstate);
//pinia添加到app对象中
app.use(pinia)
//router添加到app对象
app.use(router)
//绑定到id=app的div中
app.mount('#app')
