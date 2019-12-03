<template>
  <v-btn
    flat icon
    @click.stop="handler">
    <v-icon list>{{ icon }}</v-icon>
  </v-btn>
</template>

<script>
/**@group Elementy
 * Przycisk dla pasków i list.
 */
export default {
  name: 'button-bar',
  props: {
    /** Nazwa ikony do wyświetlenia. */
    icon: {
      type: String,
      required: true
    },

    /** Funkcja obsługująca kliknięcie */
    handler: {
      type: Function,
      required: true
    }
  }
}
</script>
