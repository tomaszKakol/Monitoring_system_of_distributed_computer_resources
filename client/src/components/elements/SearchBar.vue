<template>
  <v-layout align-center class="search-bar">
    <v-flex shrink>
      <v-select
        class="search-bar-category"
        :value="category"
        :items="categories"
        item-text="text"
        item-value="value"
        prepend-icon="search"
        color="gray"
        @change="onChangeCategory($event)"
      />
    </v-flex>
    <v-flex grow>
      <v-text-field
        v-if="onChangeShowOptions !== undefined"
        class="search-bar-text"
        placeholder="Szukaj..."
        :clearable=true
        append-icon="menu"
        @click:append="changeShowOptions"
        @change="onSearch($event)"
      />
      <v-text-field
        v-if="onChangeShowOptions === undefined"
        class="search-bar-text"
        placeholder="Szukaj..."
        :clearable=true
        @change="onSearch($event)"
      />
    </v-flex>
  </v-layout>
</template>

<script>
/**@group Elementy
 * Pasek wyszukiwania.
 */
export default {
  name: 'search-bar',
  props: {
    /** Funkcja wywoływana przy zmianie wartości. */
    onSearch: {
      type: Function,
      required: true
    },

    /**
     * Funkcja wywoływania przy żądaniu pokazania/schowania opcji.
     * 
     * Jeśli nie zostanie podana, ikona opcji nie będzie wyświetlana.
     */
    onChangeShowOptions: {
      type: Function,
      required: false,
      // brak obsługi
      default: () => {}
    },

    /** Funkcja wywoływana przy zmianie kategorii. */
    onChangeCategory: {
      type: Function,
      required: false,
      // brak obsługi
      default: () => {}
    },

    /** Domyślna kategoria szukanego elementu. */
    category: {
      // { text: String, value: String }
      type: Object,
      required: false
    },

    /** Dostępne kategorie wyszukiwania. */
    categories: {
      // [{ text: String, value: String }, ...]
      type: Array,
      required: true
    }
  },
  data () {
    return {
      showOptions: false
    }
  },
  methods: {
    changeShowOptions () {
      this.showOptions = !this.showOptions
      this.onChangeShowOptions(this.showOptions)
    }
  }
}
</script>

<style scoped>
.search-bar-category >>> .v-select__selection {
  color: gray
}
.search-bar-category >>> .v-select__selections > input {
  width: 5px !important
}
.search-bar-category >>> .v-select__slot > .v-input__append-inner .v-icon {
  color: gray
}

.search-bar-text {
  margin-left: -10px !important;
}
.search-bar-text >>> .v-input__slot {
  padding-left: 20px !important;
}
</style>
