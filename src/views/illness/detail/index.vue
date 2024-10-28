<template>
  <div class="common-layout">
    <el-container class="container">
      <Header />

      <!-- Loading State -->
      <el-empty v-if="loading" description="正在加载..." class="empty-state" />
      <el-main class="main" v-else>
        <el-row :gutter="24">
          <!-- Left Section -->
          <el-col :span="8">
            <el-card class="content-card">
              <el-image
                class="eczema-image"
                :src="eczemaInfo.imgPath || '/mnt/data/image.png'"
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
          <!-- Right Section -->
          <el-col :span="16">
            <el-card class="medicine-card">
              <h3 class="related-title">相关药物如下</h3>
              <el-row
                v-for="(drug, index) in medicines"
                :key="index"
                :gutter="20"
                class="medicine-row"
              >
                <el-col :span="6">
                  <el-image class="medicine-image" :src="drug.imgPath" alt="药品图片" />
                </el-col>
                <el-col :span="18">
                  <div class="drug-info">
                    <h2 class="drug-name" @click="goToMedicineDetail(drug.medicineId)">
                      {{ drug.medicineName }}
                    </h2>
                    <p class="comment-date">{{ drug.medicineBrand }}牌</p>
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
      <Footer />
    </el-container>
  </div>
</template>

<script setup lang="ts">
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { useRoute, useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import { reqIllnessDetails } from "@/api/illness/index.ts";

interface IllnessInfo {
  imgPath: string;
  title: string;
  description: string;
  generalSymptoms: string;
  specialSymptoms: string;
}

interface Medicine {
  medicineId: number;
  imgPath: string;
  medicineName: string;
  medicineCategory: string;
  drugQuote: string;
  medicineUsage: string;
  medicineBrand: string;
}

const route = useRoute();
const router = useRouter();

const eczemaInfo = ref<IllnessInfo>({
  imgPath: "",
  title: "",
  description: "",
  generalSymptoms: "",
  specialSymptoms: "",
});

const medicines = ref<Medicine[]>([]);
const loading = ref(true);

const fetchIllnessDetails = async (id: number) => {
  try {
    const { code, data } = await reqIllnessDetails(id);
    if (code === 200 && data) {
      const { illness, medicine } = data;
      eczemaInfo.value = {
        imgPath: illness.imgPath || "/mnt/data/image.png",
        title: illness.illnessName,
        description: illness.includeReason,
        generalSymptoms: illness.illnessSymptom,
        specialSymptoms: illness.specialSymptom,
      };
      medicines.value = medicine.map((item: any) => ({
        medicineId: item.id,
        imgPath: item.imgPath,
        medicineName: item.medicineName,
        medicineCategory: item.keyword,
        drugQuote: '"是药三分毒，切忌不要乱吃药！"',
        medicineUsage: item.medicineEffect,
        medicineBrand: item.medicineBrand,
      }));
    } else {
      console.error("Error fetching illness details: ", data?.message);
    }
  } catch (error) {
    console.error("Error fetching illness details:", error);
    alert("加载数据时发生错误，请稍后再试。");
  } finally {
    loading.value = false;
  }
};

const goToMedicineDetail = (id: number) => {
  router.push({ name: "medicineDetail", params: { id } });
};

onMounted(() => {
  fetchIllnessDetails(Number(route.params.id));
});
</script>
<style scoped>
:root {
  --primary-color: #333;
  --secondary-color: #555;
  --tertiary-color: #888;
  --background-color: #f9f9f9;
  --border-radius: 12px;
  --box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  --transition-duration: 0.3s;
}

.common-layout {
  position: absolute;
  inset: 0;
}

.container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.empty-state {
  color: #8e99ab;
  font-size: 18px;
  text-align: center;
  padding: 50px 0;
}

.main {
  width: 100%;
  flex: 1;
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  padding: 40px 30px;
  transition: all var(--transition-duration) ease;
}

.eczema-image {
  flex-shrink: 0;
  width: 160px;
  height: 160px;
  border-radius: 8px;
  overflow: hidden;
}

.medicine-image {
  flex-shrink: 0;
  width: 120px;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
}

.content-card,
.medicine-card {
  width: 100%;
  margin: 10px 0 0 0;
  padding: 25px;
  box-shadow: var(--box-shadow);
  border-radius: var(--border-radius);
  transition: all var(--transition-duration) ease;
}

.medicine-card {
  margin-left: 30px;
}

.title,
.related-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 15px;
  color: var(--primary-color);
}

.sub-title {
  font-size: 18px;
  font-weight: bold;
  margin: 20px 0 10px;
  color: var(--primary-color);
}

.description {
  font-size: 16px;
  line-height: 1.6;
  text-align: left;
  color: var(--secondary-color);
  margin-bottom: 15px;
}

.drug-info {
  padding: 15px;
  background-color: var(--background-color);
  border-radius: var(--border-radius);
  transition: all var(--transition-duration) ease;
}

.drug-name {
  font-size: 18px;
  font-weight: bold;
  color: var(--primary-color);
  margin: 8px 0;
  text-align: left;
  cursor: pointer;
  transition: color var(--transition-duration) ease;
}

.drug-name:hover {
  color: #4db7fe;
}

.drug-category {
  font-size: 14px;
  color: #67c23a;
  margin-bottom: 5px;
}

.drug-quote {
  font-size: 14px;
  color: var(--tertiary-color);
  font-style: italic;
  margin-bottom: 10px;
}

.drug-usage {
  font-size: 15px;
  line-height: 1.6;
  margin-top: 8px;
  color: var(--secondary-color);
}

.drug-brand {
  font-size: 15px;
  font-weight: bold;
  color: var(--primary-color);
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
  padding: 3px 8px;
  color: #fff;
}

.comment-title {
  color: var(--tertiary-color);
  font-size: 14px;
}

.comment-date {
  color: var(--tertiary-color);
  font-size: 15px;
  text-align: right;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main {
    flex-direction: column;
  }

  .content-card,
  .medicine-card {
    margin-left: 0;
    margin-bottom: 20px;
  }

  .title,
  .related-title {
    font-size: 20px;
  }

  .description {
    font-size: 14px;
  }

  .drug-name {
    font-size: 16px;
  }

  .drug-usage {
    font-size: 14px;
  }
}
</style>
