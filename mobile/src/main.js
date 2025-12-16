import { createApp } from 'vue'
import {createPinia} from 'pinia'
import persistedstate  from 'pinia-plugin-persistedstate'
import 'element-plus/dist/index.css'
import 'bootstrap-icons/font/bootstrap-icons.css'; // 引入 Bootstrap Icons CSS
import 'bootstrap'
import './assets/scss/styles.scss'
import * as bootstrap from 'bootstrap'//引入 Bootstrap JS
//引入css
import '@/assets/css/main.css'
// 导入 Element-Plus插件
import ElementPlus from 'element-plus'
//element-plus 全局 国际化插件
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
//element-plus 图标插件
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import App from './App.vue'
//配置路由
import router from "./router"

//创建vue对象
let app = createApp(App);

//配置ElementPlus插件给app实例，并配置国际化
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(ElementPlus, {
    locale: zhCn,
})
//创建pinia对象
let pinia = createPinia();
//给pinia对象设置插件
pinia.use(persistedstate);
//vue对象,配置上pinia
app.use(pinia);
//vue对象,配置router
app.use(router);

app.mount('#app');

