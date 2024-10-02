<template>
  <div class="common-layout">
    <el-container class="container">
      <Header></Header>
      <el-main class="main" style="background-color: #f7f7f7; color: #fff">
        <el-card class="content-card">
          <div class="comment">
            <div class="header">
              <h3>全部</h3>
            </div>
            <div class="comment-box" v-for="(medicine, index) in medicines" :key="index">
              <div class="comment-image">
                <a>
                  <img :src="getImageUrl(medicine.imgPath)" alt="image" />
                </a>
              </div>
              <div class="comment-content">
                <router-link :to="'/medicine/' + medicine.id">
                  <h5 class="m-0">{{ medicine.medicineName }}</h5>
                  <p class="comment-date" style="text-align: right">
                    {{ medicine.medicineBrand }}牌中药
                  </p>
                  <div class="inline-container">
                    <span class="num-rating white">{{
                      getMedicineType(medicine.medicineType)
                    }}</span>
                    <span class="comment-title"
                      ><i>{{ medicine.keyword }}</i></span
                    >
                  </div>
                  <p class="comment">{{ medicine.medicineEffect }}</p>
                </router-link>
              </div>
            </div>
          </div>
        </el-card>
      </el-main>
    </el-container>
    <Footer></Footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { reqHasMEDICINE } from "@/api/medicine";
import defaultImage from "@/assets/images/logo.png"; // Ensure this path is correct

const getMedicineType = (type) => {
  if (type === 0) return "西药";
  if (type === 1) return "中药";
  return "中性药";
};

const medicines = ref([]);

const fetchMedicines = async () => {
  try {
    const response = await reqHasMEDICINE();
    if (response.code === 200 && response.data && response.data.medicineList) {
      medicines.value = response.data.medicineList;
    } else {
      console.error("Error fetching medicines: ", response.message);
    }
  } catch (error) {
    console.error("Error fetching medicines:", error);
  }
};

// Fetch medicines when the component is mounted
onMounted(() => {
  fetchMedicines();
});

const getImageUrl = (imageUrl) => {
  return imageUrl ? imageUrl : defaultImage;
};
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
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 60px;
}

.content-card {
  max-width: 100%;
  margin: 30px 100px;
  min-height: 992px;
}

.comment {
  width: 100%;
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.comment-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  margin-top: 80px;
}

.comment-image {
  flex-shrink: 0;
  width: 100px;
}

.comment-image img {
  width: 100%;
  height: auto;
  object-fit: cover;
}

.comment-content {
  width: 80%;
  margin: 0 0 0 200px;
  text-align: left;
  color: #666;
}

.inline-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.m-0 {
  margin: 0;
  font-size: 20px;
}

.comment-date {
  margin-bottom: 10px;
  color: #999;
  text-align: right;
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

.comment {
  padding: 20px 0 0 0;
}
</style>
