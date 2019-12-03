<template>
  <v-container fluid>
    <v-layout row wrap>
      <v-flex xs12 sm4 md4>
        <v-card flat>
          <v-card-text>
            Ogólne
            <v-checkbox
              v-model="options.quick_access"
              label="szybki dostęp"
              hide-details
            />
            <v-checkbox
              v-model="options.show_type"
              label="wyświetlaj typ"
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
              v-model="options.types"
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
 * Sekcja opcji wyszukiwania metryk.
 */
export default {
  name: 'options-metrics',
  props: {
    /** Obiekt opcji do przekazania do listy metryk. */
    options: {
      /** {
       *    quick_access: Boolean,
       *    show_type: Boolean,
       *    types: [{ name: String, unit: String }, ...]
       *  }
       */
      type: Object,
      required: true,
      /** {
       *    quick_access: true,
       *    show_type: true,
       *    types: []
       *  }
       */
      default () {
        return {
          quick_access: true,
          show_type: true,
          types: []
        }
      }
    },

    /** Dostępne typy metryk. */
    types: {
      // [{ name: String, unit: String }, ...]
      type: Array,
      required: false,
      // []
      default () {
        return []
      }
    }
  },
  /** Aktualizuje wyjście do rodzica przy każdym wywołaniu wydarzenia. */
  model: {
    prop: 'options',
    // Zdarzenie zmiany opcji.
    event: 'change'
  }
}
</script>
