<template>
  <page title='Rejestracja'>
    <register-window>
    </register-window>
  </page>
</template>

<script>
import Page from '@/components/templates/Page'
import RegisterWindow from '@/components/sections/RegisterWindow'

/**@group Strony
 * @vuese
 * Strona formularza rejestracji.
 *
 * Używana bezpośrednio przez router.
 */
export default {
  name: 'registration',
  components: {
    'page': Page,
    'register-window': RegisterWindow
  }
}
</script>
