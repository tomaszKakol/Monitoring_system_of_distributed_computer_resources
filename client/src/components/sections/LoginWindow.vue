<template>
  <v-container fluid fill-height>
    <v-layout justify-center>
      <v-flex xs12 sm8 md4>
        <v-card class="elevation-12">
          <form @submit.prevent='trylogin()'>
            <v-card-text>
              <p class='log'>{{log}}</p>
              <p class='error'
               v-for='error in errors'
               v-bind:key='error'
              >
                {{error}}
              </p>
              <v-text-field class="t2e-v-text-field"
                prepend-icon="person"
                label='Nazwa'
                placeholder='Nazwa'
                v-model='user.username'
                required />
              <v-text-field class="t2e-v-text-field"
                prepend-icon="lock"
                type='password'
                label='Hasło'
                placeholder='hasło'
                v-model='user.password'
                required />
            </v-card-text>
            <v-card-actions>
              <v-btn class="t2e-log" to='registration'>Zarejestruj się</v-btn>
              <v-spacer></v-spacer>
              <v-btn class="t2e-log" type='submit'>Zaloguj</v-btn>
            </v-card-actions>
          </form>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import Vuex from 'vuex'
/**@group Sekcje
 * @vuese
 * Sekcja logowania.
 */
export default {
  name: 'login',
  data () {
    return {
      user: {
        username: '',
        password: ''
      },
      errors: [],
      log: ''
    }
  },
  methods: {
    ...Vuex.mapActions(['sendLogin', 'request']),
    trylogin: async function () {
      const { log, error, data } =
        await this.request(() => this.sendLogin(this.user))
      this.items = data || [];
      this.log = log
      this.errors = error ? error.split('\n') : ''
    }
  }
}
</script>

<style src="../style/buttons.css"></style>