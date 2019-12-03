<template>
  <v-dialog
    v-model="isActive"
    width=500
  >
    <v-toolbar dark>
      <v-toolbar-title>
        Recordy dla: {{ metric_name }}
      </v-toolbar-title>

      <v-spacer></v-spacer>

      <v-toolbar-items>
        <!-- Przycisk dodawania do wykresu (niezaimplementowany) -->
        <button-bar
         icon = "show_chart"
         :handler = "() => addToStaticChart()"
        />

        <!-- Przycisk wyjścia z okna. -->
        <button-bar
         icon = "clear"
         :handler = "() => isActive = false"
        />
      </v-toolbar-items>
    </v-toolbar>
    <v-card>
      <v-layout row wrap>
        <v-flex xs12 sm4 md4>
          <v-card flat>
            <v-card-text>
              <!-- Ile rekordów należy wyświetlić. -->
              <v-text-field
                type='number'
                v-model="options.n"
                label="ile"
                hide-details
              />

              <!-- Przełącznik liczby rekordów na wszystkie. -->
              <button-text
                text = 'wszystkie'
                :handler = '() => options.n = 0'
              />
            </v-card-text>
          </v-card>
        </v-flex>
        <v-flex xs12 sm4 md4>
          <v-card flat>
            <v-card-text>
              <!-- Od której daty. -->
              <v-text-field
                v-model="options.from"
                label="od"
                hide-details
              />
            </v-card-text>
          </v-card>
        </v-flex>
        <v-flex xs12 sm4 md4>
          <v-card flat>
            <v-card-text>
              <!-- Do której daty. -->
              <v-text-field
                v-model="options.to"
                label="do"
                hide-details
              />
            </v-card-text>
          </v-card>
        </v-flex>
      </v-layout>

      <!-- Dynamiczna lista rekordów. -->
      <get-list
        ref='list'
        :title='"Recordy dla: {{ metric_name }}"'
        :tryGet="reloadRecords"
        :getOptions="all_options"
        v-slot="props"
      >
        <v-container>
          <v-layout
            row
            justify-space-between
            v-for="item in props.items"
            :key="item.ts"
          >
            <v-flex xs4 class="text-xs-right">
              {{ item.val }} {{ metric_unit }}
            </v-flex>
            <v-flex xs4 class="text-xs-left">
              {{ item.ts }}
            </v-flex>
          </v-layout>
        </v-container>
      </get-list>
    </v-card>
  </v-dialog>
</template>

<script>
import Vuex from 'vuex'
import GetList from '@/components/sections/GetList'
import ButtonBar from '@/components/elements/ButtonBar'
import ButtonText from '@/components/elements/ButtonText'

/**@group Okienka
 * Dialog recordów metryki.
 *
 * Zawiera listę rekordów oraz przyciski pozwalające modyfikować jej opcje:
 * <ul>
 *   <li> ile --- rekordów do wyświetlenia
 *        (niedodatnie wartości znaczą 'wszystkie') </li>
 *   <li> wszystkie --- resetuje liczbę rekordów do wyświetlenia </li>
 * </ul>
 */
export default {
  name: 'dialog-records',
  components: {
    'get-list': GetList,
    'button-bar': ButtonBar,
    'button-text': ButtonText
  },
  props: {
    /** Czy dialog ma być aktywny.
     *
     *  Zalecane użycie opcji v-model.
     */
    active: {
      type: Boolean,
      required: true
    },

    /** Metryka, dla której wyświetlane są rekordy. */
    metric: {
      // { 'metric-id': String, name: String, unit: String }
      type: Object,
      required: true
    }
  },
  /** Aktualizuje flagę aktywności dialogu. */
  model: {
    prop: 'active',
    event: 'activityChanged'
  },
  data () {
    return {
      options: {
        n: 0
              
        //TODO: add 'to' and 'from' when
        // the format is known / feature is fixed
      }
    }
  },
  computed: {
    /** Widok metryki resetowany po zamknięciu okna. */
    mymetric () {
      if (!this.active) {  // closing --- reset to no metric
        return {}
      } else {
        return this.metric
      }
    },

    /** Id metryki. */
    metric_id () {
      return this.mymetric['metric-id']
    },

    /** Nazwa metryki. */
    metric_name () {
      return this.mymetric.name || ''
    },

    /** Jednostka metryki. */
    metric_unit () {
      return this.mymetric.unit || ''
    },

    /** Wszystkie opcje zapytania. */
    all_options () {
      return { ...this.options, id: this.metric_id }
    },

    /** Czy okno jest aktywne. Nieaktywne okno resetuje swoje dane. */
    isActive: {
      get () {
        return this.active
      },
      set (value) {
        // nextTick pozwala get-table zdążyć zresetować widok
        this.$nextTick(() => {
          // Wyzwalane kiedy okno jest włączane lub wyłączane.
          // @arg Nowy stan aktywności okna (patrz: active).
          this.$emit('activityChanged', value)
        })
      }
    }
  },
  methods: {
    ...Vuex.mapActions(['listRecords']),

    /** Pobiera dane do wylistowania pomiarów.
     *
     *  Uwaga: jeśli options nie zawiera id,
     *  zapytanie nie zostanie wykonane.
     */
    reloadRecords: async function (options) {
      if (options.id) {
        return await this.listRecords(options)
      } else {
        return []
      }
    },

    /** Resetuje metrykę przy zamknieciu okna lub ustawia po otwarciu. */
    
    /** Dodaje pomiary (nie metrykę) do statycznego wykresu. */
    addToStaticChart () {
      const records = this.get_records()
      // eslint-disable-next-line
      console.log("Records: " + JSON.stringify(records))
      //TODO
    },

    /** @vuese
     *  Zwraca listę rekordów listy.
     */
    get_records () {
      return this.$refs.list.get_items
    }
  },
  watch: {
    active (value) {
      this.isActive = value
    }
  }
}
</script>
