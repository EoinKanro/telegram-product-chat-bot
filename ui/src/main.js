/* eslint-disable */
import { createApp } from 'vue'
import * as VueRouter from 'vue-router'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"

import App from './App.vue'
import HomePage from './components/HomePage.vue'

const homePath = '/'

const routes = [
    { path: homePath, component: HomePage}
]

const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes
})

createApp(App)
    .use(router)
    .mount('#app')

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
