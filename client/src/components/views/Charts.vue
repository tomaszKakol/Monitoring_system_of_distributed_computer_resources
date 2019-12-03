<template>
  <page title="Wykresy">
    <div id="container">
      <div id="chartsContainer">
        <LinearChart />
      </div>

    </div>

  </page>
</template>

<script>
  import Page from '@/components/templates/Page'
  import LinearChart from '@/components/charts/LinearChart.vue'

  /**@group Strony
   * @vuese
   * Strona prezentacji wykresów.
   *
   * Używana bezpośrednio przez router.
   */
  export default {
    name: 'Charts',
    components: {
      'page': Page,
      LinearChart,
    }
  }

</script>

<Style>
  #container {
    min-width: 310px;
    max-width: 400px;
    min-height: 100px;
    margin: 0 auto
  }
  #chartsContainer {
    padding: 10px;
  }
</Style>