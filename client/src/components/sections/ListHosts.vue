<template>
  <get-table
    ref='table'
    title='Hosty'
    id='hosts'
    :headers="headers"
    :actions="actions"
    no_data_text='Brak hostów do wyświetlenia'
    :tryGet="reloadList"
    :getOptions="all_options"
  >
    <template v-slot="props">
      <td align="left">
        {{ props.item.name }}
      </td>
    </template>
    <template v-slot:text="props">
      <v-list two-line>
        <v-list-tile
         v-for="metric in props.item.metrics"
         :key="metric['metric-id']"
        >
          <v-list-tile-content>
            <v-list-tile-title>
              {{ metric['metric-id'] }}
            </v-list-tile-title>
            <v-list-tile-sub-title>
              {{ metric.type }} [{{ metric.unit }}]
            </v-list-tile-sub-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </template>
  </get-table>
</template>

<script>
import Vuex from 'vuex'
import GetTable from '@/components/sections/GetTable'

/**@group Sekcje
 * Sekcja pobierająca dane hostów.
 */
export default {
  name: 'list-hosts',
  components: {
    'get-table': GetTable
  },
  props: {
    /** Fragment nazwy do wyszukania. */
    searched: {
      type: String,
      required: false
    },

    /** Dodatkowe opcje listowania. */
    options: {
      type: Object,
      required: false
    }
  },
  data () {
    return {
      data: [],
      headers: [
        {
          text: 'Nazwa',
          value: 'name',
          align: 'left',
          sortable: true
        }
      ],
      actions: []
    }
  },
  computed: {
    all_options () {
      let value = {...this.options}
      value.searched = this.searched
      return value
    }
  },
  methods: {
    ...Vuex.mapActions(['listHosts']),

    /** Pokazuje metryki dostępne dla danego hosta */
    showMetrics (item) {
      item
      //TODO
    },

    /** Pobiera dane do wylistowania metryk. */
    reloadList: async function (options = {}) {
      return await this.listHosts(options)
    }
  }
}
</script>
