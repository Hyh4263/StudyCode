<template>
  <div>
    <el-card>
      <el-row>
        <el-col :span="24">
          <h1 class="page-title">数据统计</h1>
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
            <h2>药品种类数量统计</h2>
            <ECharts :options="medicineTypeOptions" />
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
import { reqGetUserList,reqGetAllUser } from "@/api/user/index";
import { reqIllnessList, reqIllness } from "@/api/illness/index";
import { reqMedicineList, reqHasAllMedicines } from "@/api/medicine/index";
import useUserStore from "@/stores/modules/user";

// Chart options refs
const diseaseCategoryOptions = ref({});
const diseaseVisitOptions = ref({});
const genderOptions = ref({});
const ageOptions = ref({});
const registrationOptions = ref({});
const medicineTypeOptions = ref({});


// Fetch data on component mount
onMounted(() => {
  fetchDiseaseData(); // Fetch disease statistics
  fetchUserStatistics(); // Fetch user statistics
  fetchMedicineData();
});

// Fetch disease statistics
const fetchDiseaseData = () => {
  reqIllness().then((response) => {
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
        axisLabel: { rotate: 0, interval: 0 }, // Rotate for better readability
      },
      yAxis: { type: "value", name: "浏览次数" },
      series: [
        {
          name: "浏览次数",
          type: "scatter",
          data: scatterData.map((item) => item.value),
          symbolSize: (value: number) => value * 1, // Scale the size of the points
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
  reqGetAllUser().then((response) => {
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

// Initialize registration chart as a line chart
const initRegistrationChart = (monthlyRegistration: any) => {
  registrationOptions.value = {
    title: { text: "每月注册用户数量" },
    tooltip: { trigger: "axis", axisPointer: { type: "line" } }, // Tooltip and axis pointer for a line chart
    xAxis: {
      type: "category",
      data: monthlyRegistration.map((item: any) => item.month),
    },
    yAxis: { type: "value" },
    series: [
      {
        data: monthlyRegistration.map((item: any) => item.count),
        type: "line", // Change to line chart
        smooth: true, // Optional: adds a smooth curve to the line
        lineStyle: {
          color: "#5470C6", // Set line color
        },
        itemStyle: {
          color: "#5470C6", // Set color of points on the line
        },
      },
    ],
  };
};

// fetchMedicineData();
const fetchMedicineData = () => {
  reqHasAllMedicines().then((response) => {
    console.log("返回的数据：", response);
    const data = response.data;
    const medicineTypeCount = processMedicineTypeData(data);
    initMedicineTypeChart(medicineTypeCount);
  });
};

// Process and initialize medicine type data
const processMedicineTypeData = (medicineList: any[]) => {
  const typeCount = medicineList.reduce((acc: any, medicine: any) => {
    const typeName = getMedicineTypeName(medicine.medicineType);
    acc[typeName] = (acc[typeName] || 0) + 1;
    return acc;
  }, {});

  console.log("药品类型统计数据：", typeCount); // 确认数据中包含 "中成药"
  return typeCount;
};

const getMedicineTypeName = (type: number) => {
  switch (type) {
    case 0:
      return "西药";
    case 1:
      return "中药";
    case 2:
      return "中成药";
    default:
      return "其他";
  }
};

const initMedicineTypeChart = (data: any) => {
  console.log("初始化药品种类数据：", data); // 检查是否包含 "中成药"
  medicineTypeOptions.value = {
    title: { text: "药品种类数量统计", left: "center" },
    tooltip: { trigger: "axis", axisPointer: { type: "shadow" } },
    xAxis: { type: "category", data: Object.keys(data), axisLabel: { interval: 0 } },
    yAxis: { type: "value", name: "数量" },
    series: [{ data: Object.values(data), type: "bar", itemStyle: { color: "#5470C6" } }],
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

.page-title {
  font-size: 2em;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 30px;
}

.chart-container {
  width: 100%;
  height: 400px;
  margin-bottom: 30px;
  padding: 20px;
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.el-card {
  background-color: #f9f9f9;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.page-title {
  font-size: 2.5em;
  font-weight: 600;
  color: #2c3e50;
  text-align: center;
  margin-bottom: 40px;
}

.chart-container {
  width: 100%;
  height: 420px;
  margin-bottom: 30px;
  padding: 25px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.chart-container:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.el-card {
  background-color: #f4f6f8;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.el-card:hover {
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.15);
}

.el-row {
  margin-bottom: 25px;
}

.el-tooltip {
  font-size: 0.9em;
  color: #555;
  background-color: #eee;
  padding: 8px;
  border-radius: 8px;
}
</style>
