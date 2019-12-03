import Vue from 'vue'
import Vuex from 'vuex'
import actions from './actions'
import * as getters from './getters'
import mutations from './mutations'
import { newAuth } from './connections'

Vue.use(Vuex)


const state = {
  /** Last requested route derirected to login. */
  req_route: null,

  /** If the user is authentified */
  isLoggedIn: false,

  /** User's data. Null if not logged in. */
  user: {
    username: null,
    password: null
  },

  /** Access token of the user. */
  access_token: null,

  /** API Gateway's axios instance. */
  api: null,

  /** Authentication service's axios instance. */
  auth: newAuth()
}

const options = {
  state,
  mutations,
  actions,
  getters
}

const store = new Vuex.Store(options)

export default store
