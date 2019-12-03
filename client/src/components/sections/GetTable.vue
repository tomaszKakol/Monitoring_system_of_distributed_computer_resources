<template>
  <get-list
    ref='list'
    :title="title"
    :tryGet="tryGet"
    :getOptions="getOptions"
  >
    <template v-slot="prop">
      <v-data-table
        :headers="real_headers"
        :items="prop.items"
        :expand="false"
        :no-data-text="no_data_text"
        item-key="name"
        hide-actions
      >
        <template v-slot:items="props">
          <tr @click="props.expanded = !props.expanded">
            <!-- Wiersz pojedynzcego wpisu -->
            <slot :item="props.item" :index="props.index">
            </slot>
            <td v-if="quick_access && has_actions">
              <button-bar
               v-for='action in actions'
               :key="action.text"
               :icon="action.icon"
               :handler="() => action.handler(props.item)" />
            </td>
          </tr>
        </template>
        <template v-slot:expand="props">
          <v-card>
            <!-- Rozwinięta forma wiersza wpisu -->
            <slot name="text" :item="props.item" :index="props.index">
            </slot>
            <v-card-actions
             class="justify-center"
             v-if="!quick_access && has_actions"
            >
              <button-text
               v-for='action in actions'
               :key="action.text"
               :icon="action.icon"
               :text="action.text"
               :handler="() => action.handler(props.item)"
              />
            </v-card-actions>
          </v-card>
        </template>
      </v-data-table>
    </template>
    <template v-slot:dialogs>
      <!-- Okienka -->
      <slot name="dialogs"></slot>
    </template>
  </get-list>
</template>

<script>
import GetList from '@/components/sections/GetList'
import ButtonBar from '@/components/elements/ButtonBar'
import ButtonText from '@/components/elements/ButtonText'

/**@group Sekcje
 * Samopobierająca tabela danych.
 */
export default {
  name: 'get-table',
  components: {
    'get-list': GetList,
    'button-bar': ButtonBar,
    'button-text': ButtonText
  },
  props: {
    /** Tytuł listy. */
    title: {
      type: String,
      required: true
    },

    /** Czy wyświetlać Akcje jako ostatnią kolumnę?
     *
     *  Jeśli nie, będą wyświetlone w rozwinięciu.
     */
    quick_access: {
      type: Boolean,
      required: false,
      default: true
    },

    /** Funkcja pobierająca dane do wylistowania. */
    tryGet: {
      // (options) => { log, error, data }
      type: Function,
      required: true
    },

    /** Opcje do przekazania funkcji przeładowującej. */
    getOptions: {
      type: Object,
      required: false,
      // brak opcji
      default: undefined
    },

    /** Nagłówki tabeli. */
    headers: {
      // [{ text: String, value: String }, ...]
      type: Array,
      required: true
    },

    /** Dostępne akcje.
     *
     *  Akcja składa się z tekstu (text), ikony (icon)
     *  oraz funkcji obsługującej (handler).
     */
    actions: {
      type: Array,
      required: true
    },

    /** Tekst braku danych. */
    no_data_text: {
      type: String,
      required: false,
      default: 'Brak danych do wyświetlenia'
    }
  },
  computed: {
    has_actions() {
      return (this.actions && this.actions.length > 0)
    },

    real_headers () {
      let value = [...this.headers];
      if (this.quick_access && this.has_actions) {
        value.push({
          text: 'Akcje',
          value: 'actions',
          align: 'center',
          sortable: false
        })
      }
      return value
    }
  }
}
</script>
