<template>
  <div>
    <v-card-text v-if="log || error">
      <p class='log'>{{ log }}</p>
      <p class='error' v-for="err in error" :key="err">
        {{ err }}
      </p>
    </v-card-text>
  
    <slot :items="items"></slot>

    <slot name="dialogs"></slot>
  </div>
</template>

<script>
import Vuex from 'vuex'

/**@group Sekcje
 * Szablon strony pobierającej dane.
 */
export default {
  name: 'get-list',
  data () {
    return {
      items: [],
      log: '',
      error: ''
    }
  },
  props: {
    /** Tytuł listy. */
    title: {
      type: String,
      required: false
    },

    /** Funkcja pobierająca dane do wylistowania. */
    tryGet: {
      // (options) => response
      type: Function,
      required: true
    },

    /** Opcje do przekazania funkcji przeładowującej.
     *
     *  Uwaga: użycie {} spowoduje ciągłe przeładowywanie listy!
     */
    getOptions: {
      type: Object,
      required: false,
      // brak opcji
      default: undefined
    }
  },
  methods: {
    ...Vuex.mapActions(['request']),

    /** Przeładuj listę. */
    reload: async function (options = this.getOptions) {
      const { log, error, data } =
        await this.request(() => this.tryGet(options))
      this.items = data || [];
      this.log = log
      this.error = error ? error.split('\n') : ''
    }
  },
  computed: {
    /** Udostępnij kopie danych listy. */
    get_items () {
      return [...this.items]
    }
  },
  mounted () {
    this.reload()
  },
  watch: {
    /** Kiedy opcje się zmieniają, przeładuj listę. */
    getOptions (newVal) {  // newVal, oldVal
      this.getOptions = newVal
      this.reload(newVal)
    }
  }
}
</script>
