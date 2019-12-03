<template>
  <v-btn flat :to="to" @click="handler">
    <v-icon left>{{ icon }}</v-icon>
    {{ text }}
  </v-btn>
</template>

<script>
/**@group Elementy
 * Przycisk paska nawigacji.
 */
export default {
  name: 'button-nav',
  props: {
    /** Nazwa ikony do wyświetlenia. */
    icon: {
      type: String,
      required: true
    },

    /** Tekst do wyświetlenia. */
    text: {
      type: String,
      required: true
    },

    /** Odnośnik routera do odesłania przy kliknięciu. */
    to: {
      type: String,
      required: false
    },

    /** Funkcja obsługująca kliknięcie */
    handler: {
      type: Function,
      required: false,
      // brak obsługi
      default: () => {}
    }
  }
}
</script>
