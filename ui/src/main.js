/* eslint-disable */
import { createApp } from 'vue'
import * as VueRouter from 'vue-router'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"

import App from '@/App.vue'
import HomePage from '@/components/HomePage.vue'
import SettingsPage from "@/components/SettingsPage";
import DataPage from "@/components/DataPage";
import UtilsPage from "@/components/UtilsPage";

const homePath = '/'
const settingsPath = '/settings'
const dataPath = '/data'
const utilsPath = '/utils'

const routes = [
    { path: homePath, component: HomePage},
    { path: settingsPath, component: SettingsPage},
    { path: dataPath, component: DataPage},
    { path: utilsPath, component: UtilsPage}
]

const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes
})

const app = createApp(App)

app.use(router)
    .mount('#app')

app.config.globalProperties.$homePath = homePath
app.config.globalProperties.$settingsPath = settingsPath
app.config.globalProperties.$dataPath = dataPath
app.config.globalProperties.$utilsPath = utilsPath

//TODO
/**
 * Set background to template from App.vue styles
 *
 * @param to - router to
 */
function setBackground(to) {
    let backgroundClass = to.matched[0].components.default.data?.().bodyBackgroundClass
    if (backgroundClass) {
        window.document.body.className = backgroundClass;
    } else {
        window.document.body.className = '';
    }
}
