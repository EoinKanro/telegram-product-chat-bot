/* eslint-disable */
import { createApp } from 'vue'
import * as VueRouter from 'vue-router'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"

import App from './App.vue'
import HomePage from './components/HomePage.vue'
import LoginPage from './components/LoginPage.vue'

const homePath = '/'
const loginPath = '/login'

const routes = [
    { path: homePath, component: HomePage},
    { path: loginPath, component: LoginPage}
]

const routesPaths = []
routes.forEach(route => {
    routesPaths.push(route.path)
})

const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes
})

createApp(App)
    .use(router)
    .mount('#app')

router.beforeEach( (to) => {
    checkAuth(to)
    setBackground(to)
})

/**
 * Check is auth ok
 * If false - show login form
 *
 * @param to - router to
 */
function checkAuth(to) {
    let authOk = LoginPage.methods.checkAuth()

    if (to.path !== loginPath && !authOk) {
        router.replace({path: loginPath})
    }
    if (to.path === loginPath && authOk || !routesPaths.includes(to.path)) {
        router.replace({path: homePath})
    }
}

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
