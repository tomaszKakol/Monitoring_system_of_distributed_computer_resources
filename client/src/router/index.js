import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/views/Home'
import Login from '@/components/views/Login'
import Registration from '@/components/views/Registration'
import Charts from '@/components/views/Charts'
import List from '@/components/views/List'
import store from '../store'

/**
 * Check if logged in.
 */
const checkLoggedIn = (to, _from, next) => {
  store.commit('tryFromLocalStorage')
  next()
}

/**
 * Check login status, if not logged in, route to home.
 */
const onlyLogged = (to, from, next) => {
  store.commit('tryFromLocalStorage')
  if (store.getters.isLoggedIn) {
    next()
  } else {
    store.commit('save_route', to.path)
    router.push('/login')
  }
}

/**
 * Check login status, if logged in, route to home.
 */
const onlyUnlogged = (to, from, next) => {
  store.commit('tryFromLocalStorage')
  if (store.getters.isLoggedIn) {
    router.push('/home')
  } else {
    next()
  }
}


Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      alias: '/home',
      name: 'Home',
      component: Home,
      beforeEnter: checkLoggedIn
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      beforeEnter: onlyUnlogged
    },
    {
      path: '/registration',
      name: 'Registration',
      component: Registration,
      beforeEnter: onlyUnlogged
    },
    {
      path: '/charts',
      name: 'Charts',
      component: Charts,
      beforeEnter: onlyLogged
    },
    {
      path: '/list',
      name: 'List',
      component: List,
      beforeEnter: onlyLogged
    }
  ]
})

export default router
