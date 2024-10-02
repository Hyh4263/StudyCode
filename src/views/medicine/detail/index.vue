<template>
  <div class="common-layout">
    <el-container class="container">
      <!-- 头部区域 -->
      <Header></Header>
      <!-- 内容区域 -->
      <el-main>
        <!-- 药品信息部分 -->
        <el-card class="medicine-card">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-image
                style="width: 100%; height: auto"
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

/* .main {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 20px;
} */

.medicine-card {
  width: 100%;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.drug-info {
  padding: 20px;
}

.drug-name {
  font-size: 30px;
  font-weight: bold;
  margin-bottom: 10px;
}

.drug-price {
  font-size: 24px;
  color: #67c23a;
  margin-top: 30px;
  margin-bottom: 10px;
}

.drug-brand {
  font-size: 18px;
  font-size: 20px;
  font-weight: bold;
}

.drug-indications h3,
.drug-section h3 {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
  text-align: left;
  margin-top: 50px;
}

.drug-section {
  margin-top: 20px;
}

.drug-section p,
.drug-indications p {
  font-size: 16px;
  line-height: 1.5;
  text-align: left;
}
</style>
