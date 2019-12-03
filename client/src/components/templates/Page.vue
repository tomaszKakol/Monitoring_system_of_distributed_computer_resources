<template>
  <div>
    <navigation :title="title" fixed app m=a-5px></navigation>
    <v-content app>
      <h1 v-if="title !== ''">{{ title }}</h1>

      <!-- Treść strony. -->
      <slot></slot>
    </v-content>
  </div>
</template>

<script>
import Navigation from '@/components/sections/Navigation'

/**@group Szablony
 * Podstawowy szablon strony.
 *
 * Dodaje pasek nawigacji, nagłówek itd.
 */
export default {
  name: 'page',
  props: {
    /** Tytuł strony */
    title: {
      type: String,
      required: true
    }
  },
  components: {
    'navigation': Navigation
  }
}
</script>
