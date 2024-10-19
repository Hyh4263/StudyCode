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
import { reqIllnessList, reqHasNewsList } from "@/api/news/index.ts";

// Chart options refs
const diseaseCategoryOptions = ref({});
const diseaseVisitOptions = ref({});
const genderOptions = ref({});
const ageOptions = ref({});
const registrationOptions = ref({});

// Fetch data on component mount
onMounted(() => {
  fetchDiseaseData(); // Fetch disease statistics
  fetchUserStatistics(); // Fetch user statistics
});

// Fetch disease statistics
const fetchDiseaseData = () => {
  reqHasNewsList().then((response) => {
    const data = response.data;
    console.log("疾病数据：", data);

    //疾病类别数量统计
    const diseaseCategoryCount = processDiseaseCategoryData(data);
    initDiseaseCategoryChart(diseaseCategoryCount);

    // 每个疾病浏览次数统计
    const diseaseVisitCount = processDiseaseVisitData(data);
    console.log("疾病浏览次数统计：", diseaseVisitCount);
    initDiseaseVisitChart(diseaseVisitCount);
  });

  const processDiseaseCategoryData = (data: any) => {
    const categoryCount = data.illness.reduce((acc: any, illness: any) => {
      acc[illness.kindName] = (acc[illness.kindName] || 0) + 1;
      return acc;
    }, {});
    return categoryCount;
  };

  const initDiseaseCategoryChart = (data: any) => {
    diseaseCategoryOptions.value = {
      title: { text: "疾病类别数量统计" },
      tooltip: { trigger: "axis", axisPointer: { type: "shadow" } },
      xAxis: {
        type: "category",
        data: Object.keys(data),
        axisLabel: {
          interval: 0, // 显示所有标签
        },
      },
      yAxis: { type: "value" },
      series: [{ data: Object.values(data), type: "bar" }],
    };
  };

  // 处理每个疾病浏览数据
  const processDiseaseVisitData = (data: any) => {
    const visitCount = data.illness.reduce(
      (acc: { [key: string]: number }, illness: any) => {
        acc[illness.illness_name] = (acc[illness.illness_name] || 0) + illness.pageview;
        return acc;
      },
      {}
    );
    return visitCount;
  };

  // 初始化疾病浏览次数散点图
  const initDiseaseVisitChart = (data: any) => {
    const scatterData = Object.entries(data).map(([name, value]) => ({
      name,
      value,
    }));

    diseaseVisitOptions.value = {
      title: { text: "疾病浏览次数统计", left: "center" },
      tooltip: {
        trigger: "item",
        formatter: "{b}: {c} 次浏览", // Tooltip format for scatter points
      },
      xAxis: {
        type: "category",
        data: scatterData.map((item) => item.name),
        axisLabel: { rotate: 45, interval: 0 }, // Rotate for better readability
      },
      yAxis: { type: "value", name: "浏览次数" },
      series: [
        {
          name: "浏览次数",
          type: "scatter",
          data: scatterData.map((item) => item.value),
          symbolSize: (value: number) => value * 3, // Scale the size of the points
          itemStyle: {
            color: "#5470C6",
          },
        },
      ],
    };
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
    tooltip: { trigger: "axis", axisPointer: { type: "shadow" } },
    xAxis: {
      type: "category",
      data: ageStats.map((item: any) => item.age_range),
    },
    yAxis: { type: "value" },
    series: [{ data: ageStats.map((item: any) => item.count), type: "bar" }],
  };
};

// Initialize registration chart
const initRegistrationChart = (monthlyRegistration: any) => {
  registrationOptions.value = {
    title: { text: "每月注册用户数量" },
    tooltip: { trigger: "axis", axisPointer: { type: "shadow" } },
    xAxis: {
      type: "category",
      data: monthlyRegistration.map((item: any) => item.month),
    },
    yAxis: { type: "value" },
    series: [{ data: monthlyRegistration.map((item: any) => item.count), type: "bar" }],
  };
};
</script>

<style scoped>
.page-title {
  margin-bottom: 20px;
}

.chart-container {
  width: 100%;
  height: 400px;
  margin-bottom: 20px;
}
</style>
