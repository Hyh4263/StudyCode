<template>
  <div>
    <el-row>
      <el-col :span="24">
        <h1 class="page-title">疾病与用户数据统计</h1>
      </el-col>
    </el-row>

    <!-- 疾病统计图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="chart-container">
          <h2>疾病类别数量统计</h2>
          <ECharts :options="diseaseCategoryOptions" />
        </div>
      </el-col>

      <el-col :span="12">
        <div class="chart-container">
          <h2>浏览记录时间分布</h2>
          <ECharts :options="visitTimeOptions" />
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <div class="chart-container">
          <h2>疾病浏览次数统计</h2>
          <ECharts :options="diseaseVisitOptions" />
        </div>
      </el-col>
    </el-row>

    <!-- 用户统计图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="chart-container">
          <h2>用户性别分布</h2>
          <ECharts v-if="genderOptions" :options="genderOptions" />
        </div>
      </el-col>

      <el-col :span="12">
        <div class="chart-container">
          <h2>用户年龄分布</h2>
          <ECharts v-if="ageOptions" :options="ageOptions" />
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <div class="chart-container">
          <h2>每月注册用户数量</h2>
          <ECharts v-if="registrationOptions" :options="registrationOptions" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import axios from "axios";
import { reqGetUserList } from "@/api/user/index.ts";
import ECharts from "@/components/Echarts/ECharts.vue";

export default {
  components: {
    ECharts,
  },
  data() {
    return {
      // 疾病统计图表选项
      diseaseCategoryOptions: {
        title: { text: "疾病类别数量统计" },
        tooltip: { trigger: "axis", axisPointer: { type: "shadow" } },
        xAxis: { type: "category", data: ["类别1", "类别2", "类别3", "类别4", "类别5"] },
        yAxis: { type: "value" },
        series: [{ data: [5, 6, 3, 4, 2], type: "bar" }],
      },
      visitTimeOptions: {
        title: { text: "浏览记录时间分布" },
        tooltip: { trigger: "axis" },
        xAxis: {
          type: "category",
          data: [
            "00:00",
            "02:00",
            "04:00",
            "06:00",
            "08:00",
            "10:00",
            "12:00",
            "14:00",
            "16:00",
            "18:00",
            "20:00",
            "22:00",
          ],
        },
        yAxis: { type: "value" },
        series: [{ data: [12, 8, 5, 6, 10, 15, 20, 14, 10, 8, 7, 12], type: "line" }],
      },
      diseaseVisitOptions: {
        title: { text: "疾病浏览次数统计", left: "center" },
        tooltip: { trigger: "item" },
        legend: { orient: "vertical", left: "left" },
        series: [
          {
            name: "浏览次数",
            type: "pie",
            radius: "50%",
            data: [
              { value: 10, name: "病毒性感冒" },
              { value: 8, name: "风寒感冒" },
              { value: 6, name: "扁桃体发炎" },
              { value: 4, name: "偏头痛" },
              { value: 2, name: "便秘" },
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
      },
      // 用户统计图表选项
      genderOptions: null,
      ageOptions: null,
      registrationOptions: null,
    };
  },
  mounted() {
    this.fetchDiseaseData(); // 获取疾病统计数据
    this.fetchUserStatistics(); // 获取用户统计数据
  },
  methods: {
    fetchDiseaseData() {
      // 模拟从后端获取疾病统计数据
      // TODO: 在这里添加从后端获取数据的逻辑
    },
    fetchUserStatistics() {
      reqGetUserList().then((response) => {
        const data = response.data;
        this.initGenderChart(data.userSex);
        this.initAgeChart(data.userAge);
        this.initRegistrationChart(data.createTime);
      });
    },
    initGenderChart(genderStats) {
      this.genderOptions = {
        title: { text: "用户性别分布" },
        tooltip: { trigger: "item" },
        legend: { top: "5%", left: "center" },
        series: [
          {
            name: "性别",
            type: "pie",
            radius: "50%",
            data: genderStats.map((item) => ({ value: item.count, name: item.gender })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
          },
        ],
      };
    },
    initAgeChart(ageStats) {
      this.ageOptions = {
        title: { text: "用户年龄分布" },
        xAxis: { type: "category", data: ageStats.map((item) => item.age_range) },
        yAxis: { type: "value" },
        series: [{ data: ageStats.map((item) => item.count), type: "bar" }],
      };
    },
    initRegistrationChart(monthlyRegistration) {
      this.registrationOptions = {
        title: { text: "每月注册用户数量" },
        xAxis: { type: "category", data: monthlyRegistration.map((item) => item.month) },
        yAxis: { type: "value" },
        series: [{ data: monthlyRegistration.map((item) => item.count), type: "line" }],
      };
    },
  },
};
</script>

<style scoped>
.page-title {
  text-align: center;
  margin: 20px 0;
}

.chart-container {
  padding: 20px;
  background-color: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

h2 {
  text-align: center;
}
</style>
