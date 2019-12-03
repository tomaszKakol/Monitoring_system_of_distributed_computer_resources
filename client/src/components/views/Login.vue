<template>
  <page title="Logowanie">
    <login-window>
    </login-window>
  </page>
</template>

<script>
import Page from '@/components/templates/Page'
import LoginWindow from '@/components/sections/LoginWindow'
import Vuex from 'vuex'

/**@group Strony
 * @vuese
 * Strona logowania.
 *
 * Używana bezpośrednio przez router.
 */
export default {
  name: 'login',
  components: {
    'page': Page,
    'login-window': LoginWindow
  },
  methods: {
    ...Vuex.mapActions(['login'])
  }
}
</script>
