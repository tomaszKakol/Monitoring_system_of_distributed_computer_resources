<template>
  <v-app id="app" dark>
    <router-view/>
  </v-app>
</template>

<script>
export default {
    name: 'App'
}
</script>

<style lang="scss">
#app {
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}
@import 'components/vuelendar/scss/vuelendar.scss';
</style>
