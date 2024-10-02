<template>
  <div class="common-layout">
    <el-container class="container">
      <!-- 头部区域 -->
      <Header></Header>
      <!-- 内容区域 -->
      <el-main class="main">
        <el-row :gutter="24">
          <!-- 左侧部分 -->
          <el-col :span="8">
            <el-card class="content-card">
              <el-image
                style="width: 150px; height: 150px"
                :src="eczemaInfo.imgPath"
                alt="相关图片"
              />
              <h2 class="title">{{ eczemaInfo.title }}</h2>
              <p class="description">{{ eczemaInfo.description }}</p>
              <h3 class="sub-title">一般症状：</h3>
              <p class="description">{{ eczemaInfo.generalSymptoms }}</p>
              <h3 class="sub-title">特殊症状：</h3>
              <p class="description">{{ eczemaInfo.specialSymptoms }}</p>
            </el-card>
          </el-col>
          <!-- 右侧部分 -->
          <el-col :span="16">
            <el-card class="medicine-card">
              <h3 class="related-title">相关药物如下</h3>
              <el-row
                :gutter="20"
                style="margin-top: 20px"
                v-for="(drug, index) in medicines"
                :key="index"
              >
                <el-col :span="6">
                  <el-image
                    style="width: 150px; height: 150px"
                    :src="drug.imgPath"
                    alt="药品图片"
                  />
                </el-col>
                <el-col :span="18">
                  <div class="drug-info" style="background-color: rgb(241, 241, 241)">
                    <h2 class="drug-name" @click="goToMedicineDetail(drug.medicineId)">
                      {{ drug.medicineName }}
                    </h2>
                    <p class="comment-date">{{ drug.medicineBrand }}</p>
                    <div class="inline-container">
                      <div class="num-rating white">{{ drug.medicineCategory }}</div>
                      <span class="comment-title"
                        ><i>{{ drug.drugQuote }}</i></span
                      >
                    </div>
                    <div class="drug-usage">{{ drug.medicineUsage }}</div>
                  </div>
                </el-col>
              </el-row>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
      <!-- 底部区域 -->
      <Footer></Footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { useRoute } from "vue-router";
import { useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import { reqIllnessDetails } from "@/api/news"; // 假设您有一个函数用于请求疾病详情
const route = useRoute();
const router = useRouter();

const eczemaInfo = ref({
  imgPath: "",
  title: "",
  description: "",
  generalSymptoms: "",
  specialSymptoms: "",
});

const medicines: any = ref([]);

const fetchIllnessDetails = async (id: number) => {
  try {
    const response: any = await reqIllnessDetails(id);
    if (response.code === 200 && response.data) {
      const illness = response.data.illness;
      eczemaInfo.value = {
        imgPath: illness.imgPath || "/mnt/data/image.png", // 如果没有提供图片路径，则使用默认图片路径
        title: illness.illnessName,
        description: illness.includeReason,
        generalSymptoms: illness.illnessSymptom,
        specialSymptoms: illness.specialSymptom,
      };

      medicines.value = response.data.medicine.map(
        (item: {
          id: any;
          imgPath: any;
          medicineName: any;
          keyword: any;
          medicineEffect: any;
          medicineBrand: any;
        }) => ({
          medicineId: item.id,
          imgPath: item.imgPath,
          medicineName: item.medicineName,
          medicineCategory: item.keyword,
          drugQuote: '"是药三分毒，切忌不要乱吃药！"', // 这里假设药物报价是固定的，如果是动态的请替换
          medicineUsage: item.medicineEffect,
          medicineBrand: item.medicineBrand,
        })
      );
    } else {
      console.error("Error fetching illness details: ", response.message);
    }
  } catch (error) {
    console.error("Error fetching illness details:", error);
  }
};

const goToMedicineDetail = (id: number) => {
  router.push({ name: "medicineDetail", params: { id } });
};

onMounted(() => {
  const illnessId = Number(route.params.id);
  // console.log(illnessId);
  fetchIllnessDetails(illnessId);
});
</script>

<style scoped>
.common-layout {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
}

.container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.main {
  width: 100%;
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 30px 20px;
}

.content-card {
  width: 100%;
  margin: 10px 0px 0px 30px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.medicine-card {
  width: 90%;
  margin-left: 50px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.sub-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
  margin-top: 20px;
}

.description {
  font-size: 16px;
  line-height: 1.5;
  text-align: left;
}

.related-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
}

.drug-info {
  padding: 10px 0;
}

.drug-name {
  font-size: 15px;
  font-weight: bold;
  margin: 5px 0px 5px 20px;
  text-align: left;
}

.drug-category {
  font-size: 16px;
  color: #67c23a;
  margin-bottom: 5px;
}

.drug-quote {
  font-size: 14px;
  color: #999;
  margin-bottom: 10px;
}

.drug-usage {
  font-size: 16px;
  line-height: 1.5;
  margin-bottom: 10px;
  margin: 15px 0px 5px 0px;
}

.drug-brand {
  font-size: 16px;
  font-weight: bold;
}

.inline-container {
  margin-left: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.num-rating.white {
  background: #4db7fe;
  border-radius: 5px;
  padding: 3px 6px;
  color: #fff;
}

.comment-title {
  color: #999;
}

.comment-date {
  color: #999;
  font-size: 15px;
  text-align: right;
}
</style>
