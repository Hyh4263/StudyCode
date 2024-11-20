<template>
  <div ref="chart" :style="{ width: '100%', height: '400px' }"></div>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: "ECharts",
  props: {
    options: {
      type: Object,
      required: true,
    },
  },
  mounted() {
    this.initChart();
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart);
      this.chart.setOption(this.options);
    },
  },
  watch: {
    options: {
      handler(newOptions) {
        if (this.chart) {
          this.chart.setOption(newOptions);
        }
      },
      deep: true,
    },
  },
  beforeUnmount() {
    if (this.chart) {
      this.chart.dispose();
    }
  },
};
</script>

<style scoped>
/* Add styles if needed */
</style>
