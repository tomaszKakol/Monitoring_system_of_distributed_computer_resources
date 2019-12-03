<template>
  <v-container fluid>
    <v-layout row wrap>
      <v-flex xs12 sm4 md4>
        <v-card flat>
          <v-card-text>
            Ogólne
            <v-text-field
              type='number'
              v-model="options.top"
              label="n najlepszych"
              hide-details
            />
          </v-card-text>
        </v-card>
      </v-flex>
      <v-flex xs12 sm4 md4>
        <v-card flat>
          <v-card-text>
            Tylko typy
            <v-checkbox
              v-for="type in types"
              v-model="options.metric_types"
              :value="type.name"
              :key="type.name"
              :label="type.name"
              hide-details
            />
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
/**@group Sekcje
 * Sekcja opcji wyszukiwania hostów.
 */
export default {
  name: 'options-hosts',
  props: {
    /** Obiekt opcji do przekazania do listy hostów. */
    options: {
      /** {
       *    quick_access: Boolean,
       *    metric_types: [{ name: String }, ...],
       *    top: Number
       *  }
       */
      type: Object,
      required: true,
      /** {
       *    quick_access: true,
       *    metric_types: [],
       *    top: 0
       *  }
       */
      default () {
        return {
          quick_access: true,
          metric_types: [],
          top: 0
        }
      }
    },

    /** Dostępne typy metryk. */
    types: {
      type: Array,
      required: false,
      default () {
        return []
      }
    }
  },
  /** Aktualizuje wyjście do rodzica przy każdym wywołaniu wydarzenia. */
  model: {
    prop: 'options',
    event: 'change'
  }
}
</script>
