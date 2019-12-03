<template>
  <get-table
    ref='table'
    title='Metryki'
    :headers="headers"
    :actions="actions"
    :quick_access="quick_access"
    no_data_text='Brak metryk do wyświetlenia'
    :tryGet="reloadList"
    :getOptions="all_options"
  >
    <template v-slot="props">
      <td align="left">
        {{ props.item.name }}
      </td>
      <td
        v-if="show_type"
        align="right"
        class="caption"
      >
        ( {{ props.item.type || "brak typu" }} )
      </td>
    </template>
    <template v-slot:text="props">
      <v-list two-line>
        <v-list-tile>
          <v-list-tile-content>
            <v-list-tile-title>
              {{ props.item.type }} [{{ props.item.unit }}]
            </v-list-tile-title>
            <v-list-tile-sub-title>
              typ
            </v-list-tile-sub-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile>
          <v-list-tile-content>
            <v-list-tile-title>
              {{ props.item['host-id'] }}
            </v-list-tile-title>
            <v-list-tile-sub-title>
              host
            </v-list-tile-sub-title>
          </v-list-tile-content>
        </v-list-tile>
        <v-list-tile>
          <v-list-tile-content>
            <v-list-tile-title>
              {{ props.item['user-id'] || '(publiczna)' }}
            </v-list-tile-title>
            <v-list-tile-sub-title>
              właściciel
            </v-list-tile-sub-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </template>
    <template v-slot:dialogs>
      <dialog-records
        v-model="records_dialog.active"
        :metric="records_dialog.item"
      />
    </template>
  </get-table>
</template>

<script>
import Vuex from 'vuex'
import GetTable from '@/components/sections/GetTable'
import DialogRecords from '@/components/dialogs/DialogRecords'

/**@group Sekcje
 * Sekcja pobierająca dane metryk.
 */
export default {
  name: 'list-metrics',
  components: {
    'get-table': GetTable,
    'dialog-records': DialogRecords
  },
  props: {
    /** Fragment nazwy do znalezienia. */
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
      all_headers: {
        name: {
          text: 'Nazwa',
          value: 'name',
          align: 'left',
          sortable: true
        },
        type: {
          text: 'Typ',
          value: 'type',
          align: 'right',
          sortable: true
        }
      },
      actions: [
        {
          text: 'Lista pomiarów',
          icon: 'list',
          handler: this.showRecords
        },
        {
          text: 'Utwórz metrykę złożoną',
          icon: 'add_circle',
          handler: this.addMetricBy
        },
        {
          text: 'Dodaj do wykresu',
          icon: 'show_chart',
          handler: this.addToChart
        }
      ],
      records_dialog: {
        item: { 'metric-id': null },
        active: false
      }
    }
  },
  computed: {
    all_options () {
      let value = {...this.options}
      value.searched = this.searched
      return value
    },

    show_type () {
      return (this.options && this.options.show_type) || false
    },

    quick_access () {
      return (this.options && this.options.quick_access) || false
    },

    headers () {
      let value = []
      value.push(this.all_headers.name)
      if (this.show_type) {
        value.push(this.all_headers.type)
      }
      return value
    }
  },
  methods: {
    ...Vuex.mapActions(['listMetrics']),

    /** Pokazuje pomiary danej metryki */
    showRecords (item) {
      this.records_dialog.item = item
      this.records_dialog.active = true
    },

    /** Dodaje metrykę pochodną */
    addMetricBy (item) {
      item
      //TODO
    },

    /** Dodaje do wykresu */
    addToChart (item) {
      // eslint-disable-next-line
      console.log("item: " + json.stringify(item))
      item
      //TODO
    },

    /** Pobiera dane do wylistowania metryk. */
    reloadList: async function (options = {}) {
      return await this.listMetrics(options)
    }
  }
}
</script>
