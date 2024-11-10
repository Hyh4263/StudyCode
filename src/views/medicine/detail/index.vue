<template>
  <div class="common-layout">
    <el-container class="container">
      <!-- 头部区域 -->
      <Header></Header>
      <!-- 内容区域 -->
      <el-main class="content">
        <!-- 药品信息部分 -->
        <el-card class="medicine-card">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-image
                style="width: 100%; height: 280px"
                :src="medicine.imgPath"
                alt="药品图片"
              />
            </el-col>
            <el-col :span="16">
              <div class="drug-info">
                <h2 class="drug-name" style="text-align: left">
                  {{ medicine.medicineName }}
                </h2>
                <div class="drug-price" style="text-align: left">
                  {{ medicine.medicinePrice }} ¥/盒
                </div>
                <div class="drug-brand" style="text-align: left; margin-top: 30px">
                  品牌方：{{ medicine.medicineBrand }}
                </div>
                <div class="drug-indications">
                  <h3>适应症：</h3>
                  <p>{{ medicine.medicineEffect }}</p>
                </div>
              </div>
            </el-col>
          </el-row>
          <!-- 药品相互作用 -->
          <el-row class="drug-section">
            <el-col :span="24">
              <h3>药的相互作用：</h3>
              <p>{{ medicine.interaction }}</p>
            </el-col>
          </el-row>
          <!-- 药品禁忌 -->
          <el-row class="drug-section">
            <el-col :span="24">
              <h3>禁忌：</h3>
              <p>{{ medicine.taboo }}</p>
            </el-col>
          </el-row>
          <!-- 用法用量 -->
          <el-row class="drug-section">
            <el-col :span="24">
              <h3>用法用量：</h3>
              <p>{{ medicine.usAge }}</p>
            </el-col>
          </el-row>
        </el-card>
      </el-main>
      <!-- 底部区域 -->
      <Footer></Footer>
    </el-container>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { reqHasMedicineByOne } from "@/api/medicine";

const route = useRoute();
const medicine = ref({
  imgPath: "",
  medicineName: "",
  medicinePrice: "",
  medicineBrand: "",
  medicineEffect: "",
  interaction: "",
  taboo: "",
  usAge: "",
});

const fetchMedicineDetails = async (id: number) => {
  try {
    // const response = await axios.get(`http://localhost:5173/api/medicine/findMedicineOne/${1}`);
    const response = await reqHasMedicineByOne(id);
    // console.log(response);
    if (response.code === 200) {
      const medData = response.data.medicine; // 根据API返回的数据结构调整
      // console.log(medData);
      medicine.value = {
        imgPath: medData.imgPath,
        medicineName: medData.medicineName,
        medicinePrice: medData.medicinePrice,
        medicineBrand: medData.medicineBrand,
        medicineEffect: medData.medicineEffect,
        interaction: medData.interaction,
        taboo: medData.taboo,
        usAge: medData.usAge,
      };
    }
  } catch (error) {
    console.error("Failed to fetch medicine details:", error);
  }
};

onMounted(() => {
  const medicineId = Number(route.params.id);
  console.log(medicineId);
  fetchMedicineDetails(medicineId);
});
</script>
<style scoped>
.common-layout {
  position: absolute;
  height: 100vh;
  inset: 0;
  display: flex;
  flex-direction: column;
}

.container {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
}

.content {
  background-color: #f3f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  padding: 0px;
  box-sizing: border-box;
}

.medicine-card {
  width: 100%;
  padding: 30px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.medicine-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.drug-info {
  padding: 20px;
}

.drug-name {
  font-size: 30px;
  font-weight: 700;
  color: #333;
  margin-bottom: 10px;
  text-transform: capitalize;
}

.drug-price {
  font-size: 24px;
  color: #67c23a;
  margin-top: 10px;
  font-weight: 600;
}

.drug-brand {
  font-size: 18px;
  color: #555;
  font-weight: 600;
  margin-top: 8px;
}

.drug-indications h3,
.drug-section h3 {
  font-size: 22px;
  font-weight: 600;
  color: #4a4a4a;
  margin-top: 20px;
  border-left: 4px solid #67c23a;
  padding-left: 10px;
}

.drug-section,
.drug-indications {
  margin-top: 15px;
  color: #666;
}

.drug-section p,
.drug-indications p {
  font-size: 16px;
  line-height: 1.6;
  text-align: left;
  color: #444;
}

/* Animations for smooth content load */
@keyframes fadeIn {
  0% {
    opacity: 0;
    transform: translateY(10px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.medicine-card,
.drug-info,
.drug-section {
  animation: fadeIn 0.6s ease both;
}

/* Responsive Design */
@media (max-width: 768px) {
  .medicine-card {
    padding: 20px;
  }

  .drug-name {
    font-size: 26px;
  }

  .drug-price {
    font-size: 20px;
  }

  .drug-brand,
  .drug-indications h3,
  .drug-section h3 {
    font-size: 18px;
  }

  .drug-section p,
  .drug-indications p {
    font-size: 15px;
  }
}

@media (max-width: 768px) {
  .medicine-card {
    padding: 15px;
  }

  .drug-name {
    font-size: 24px;
  }

  .drug-price {
    font-size: 18px;
  }

  .drug-brand,
  .drug-indications h3,
  .drug-section h3 {
    font-size: 16px;
  }

  .drug-section p,
  .drug-indications p {
    font-size: 14px;
  }
}
</style>
