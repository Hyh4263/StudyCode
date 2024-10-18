<template>
  <div>
    <el-card>
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
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import ECharts from "@/components/Echarts/ECharts.vue";
import { reqGetUserList } from "@/api/user/index.ts";
import { reqIllnessList } from "@/api/news/index.ts";

// Chart options refs
const diseaseCategoryOptions = ref({});
const visitTimeOptions = ref({});
const diseaseVisitOptions = ref({});
const genderOptions = ref(null);
const ageOptions = ref(null);
const registrationOptions = ref(null);

// Fetch data on component mount
onMounted(() => {
  fetchDiseaseData(); // Fetch disease statistics
  fetchUserStatistics(); // Fetch user statistics
});

// Fetch disease statistics
const fetchDiseaseData = () => {
  reqIllnessList(1, 2).then((response) => {
    const data = response.data;
    console.log("疾病数据：", data);
  });

  diseaseCategoryOptions.value = {
    title: { text: "疾病类别数量统计" },
    tooltip: { trigger: "axis", axisPointer: { type: "shadow" } },
    xAxis: { type: "category", data: ["类别1", "类别2", "类别3", "类别4", "类别5"] },
    yAxis: { type: "value" },
    series: [{ data: [5, 6, 3, 4, 2], type: "bar" }],
  };

  visitTimeOptions.value = {
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
  };

  diseaseVisitOptions.value = {
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
  };
};

// Fetch user statistics
const fetchUserStatistics = () => {
  reqGetUserList().then((response) => {
    const data = response.data;

    // Process and initialize charts for gender, age, and registration
    const genderStats = processGenderData(data);
    initGenderChart(genderStats);

    const ageStats = processAgeData(data);
    initAgeChart(ageStats);

    const monthlyRegistration = processRegistrationData(data);
    initRegistrationChart(monthlyRegistration);
  });
};

// Process age data
const processAgeData = (data: any) => {
  const ageRanges = [
    { range: "0-10", min: 0, max: 10 },
    { range: "11-20", min: 11, max: 20 },
    { range: "21-30", min: 21, max: 30 },
    { range: "31-40", min: 31, max: 40 },
    { range: "41-50", min: 41, max: 50 },
    { range: "51+", min: 51, max: Infinity },
  ];

  const ageCount = ageRanges.reduce((acc: any, range) => {
    acc[range.range] = 0;
    return acc;
  }, {});

  data.forEach((user: any) => {
    if (user.userAge !== null) {
      const ageRange = ageRanges.find(
        (range) => user.userAge >= range.min && user.userAge <= range.max
      );
      if (ageRange) {
        ageCount[ageRange.range]++;
      }
    }
  });

  return Object.entries(ageCount).map(([age_range, count]) => ({ age_range, count }));
};

// Process gender data
const processGenderData = (data: any) => {
  const genderCount = data.reduce((acc: any, user: any) => {
    if (user.userSex) {
      acc[user.userSex] = (acc[user.userSex] || 0) + 1;
    }
    return acc;
  }, {});

  return Object.entries(genderCount).map(([gender, count]) => ({ gender, count }));
};

// Process registration data
const processRegistrationData = (data: any) => {
  const registrationCount = data.reduce((acc: any, user: any) => {
    if (user.createTime) {
      const month = new Date(user.createTime).toLocaleString("default", {
        month: "long",
        year: "numeric",
      });
      acc[month] = (acc[month] || 0) + 1;
    }
    return acc;
  }, {});

  return Object.entries(registrationCount).map(([month, count]) => ({ month, count }));
};

// Initialize gender chart
const initGenderChart = (genderStats: any) => {
  genderOptions.value = {
    title: { text: "用户性别分布" },
    tooltip: { trigger: "item" },
    legend: { top: "5%", left: "center" },
    series: [
      {
        name: "性别",
        type: "pie",
        radius: "50%",
        data: genderStats.map((item: any) => ({ value: item.count, name: item.gender })),
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
};

// Initialize age chart
const initAgeChart = (ageStats: any) => {
  ageOptions.value = {
    title: { text: "用户年龄分布" },
    xAxis: { type: "category", data: ageStats.map((item: any) => item.age_range) },
    yAxis: { type: "value" },
    series: [{ data: ageStats.map((item: any) => item.count), type: "bar" }],
  };
};

// Initialize registration chart
const initRegistrationChart = (monthlyRegistration: any) => {
  registrationOptions.value = {
    title: { text: "每月注册用户数量" },
    xAxis: { type: "category", data: monthlyRegistration.map((item: any) => item.month) },
    yAxis: { type: "value" },
    series: [{ data: monthlyRegistration.map((item: any) => item.count), type: "line" }],
  };
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
