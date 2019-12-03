<template>
  <page title="Strona Główna">
    Witamy na stronie głównej.
  </page>
</template>

<script>
import Page from '@/components/templates/Page'

/**@group Strony
 * @vuese
 * Strona główna serwisu.
 *
 * Używana bezpośrednio przez router.
 */
export default {
  name: 'home',
  components: {
    'page': Page
  }
}
</script>
