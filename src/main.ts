import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router/index'

import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css'
//@ts-ignore忽略当前文件ts类型的检测否则有红色提示(打包会失败)
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
//svg插件需要的配置代码
import 'virtual:svg-icons-register'
//引入自定义插件对象：注册整个项目全局组件
import gloablComponent from '@/components';
const app = createApp(App)


app.use(createPinia())
app.use(router)

app.use(ElementPlus, {
    locale: zhCn
})
app.use(gloablComponent);
app.mount('#app')
