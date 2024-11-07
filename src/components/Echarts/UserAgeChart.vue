<!-- src/components/MedicineCategoryChart.vue -->
<template>
    <div class="chart-container">
      <v-chart :options="options" autoresize />
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted } from "vue";
  import { use } from "echarts/core";
  import { CanvasRenderer } from "echarts/renderers";
  import { PieChart } from "echarts/charts";
  import { TitleComponent, TooltipComponent, LegendComponent } from "echarts/components";
  import { ECharts } from "vue-echarts";
  
  use([CanvasRenderer, PieChart, TitleComponent, TooltipComponent, LegendComponent]);
  
  const options = ref({
    title: {
      text: "药品类别数量统计",
      left: "center",
      textStyle: {
        color: "#fff",
      },
    },
    tooltip: {
      trigger: "item",
    },
    legend: {
      bottom: "5%",
      left: "center",
      textStyle: {
        color: "#fff",
      },
    },
    series: [
      {
        name: "药品类别",
        type: "pie",
        radius: "50%",
        data: [
          { value: 1048, name: "抗生素" },
          { value: 735, name: "镇痛药" },
          { value: 580, name: "抗病毒药" },
          { value: 484, name: "降压药" },
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: "rgba(0, 0, 0, 0.5)",
          },
        },
      },
    ],
  });
  </script>
  
  <style scoped>
  .chart-container {
    width: 100%;
    height: 400px;
  }
  </style>
  