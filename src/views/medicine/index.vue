<template>
  <div class="common-layout">
    <el-container class="container">
      <Header></Header>
      <el-main class="main">
        <el-card class="content-card">
          <div class="comment">
            <div class="header">
              <h3>药品列表</h3>
            </div>
            <div class="comment-box" v-for="(medicine, index) in medicines" :key="index">
              <div class="comment-image">
                <a>
                  <img :src="getImageUrl(medicine.img)" alt="image" />
                </a>
              </div>
              <div class="comment-content">
                <router-link :to="'/medicine/' + medicine.id">
                  <h5 class="medicine-name">{{ medicine.name }}</h5>
                  <p class="comment-date">{{ medicine.band }}牌中药</p>
                  <div class="inline-container">
                    <span class="num-rating">{{ getMedicineType(medicine.type) }}</span>
                    <span class="comment-title">{{ medicine.keyword }}</span>
                  </div>
                  <p class="comment">{{ medicine.effect }}</p>
                </router-link>
              </div>
            </div>
          </div>

          <el-pagination
            @current-change="getMedicineList"
            @size-change="sizeChange"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[3, 5, 7, 9]"
            :background="true"
            layout="prev, pager, next, jumper, ->, sizes, total"
            :total="total"
            class="pagination"
          />
        </el-card>
      </el-main>
    </el-container>
    <Footer></Footer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { reqMedicineList } from "@/api/medicine";
import defaultImage from "@/assets/images/logo.png";

const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(0);
const medicines = ref([]);

const getMedicineType = (type: any) => {
  return type === 0 ? "西药" : type === 1 ? "中药" : "中性药";
};

const getMedicineList = async (pager = 1) => {
  currentPage.value = pager;
  const result: any = await reqMedicineList(currentPage.value, pageSize.value);
  if (result.code === 200) {
    total.value = result.data.totalElements;
    medicines.value = result.data.medicineList.map((item: any) => ({
      createTime: item.create_time,
      id: item.id,
      img: item.img_path,
      keyword: item.keyword,
      band: item.medicine_brand,
      effect: item.medicine_effect,
      name: item.medicine_name,
      price: item.medicine_price,
      type: item.medicine_type,
      taboo: item.taboo,
      use: item.us_age,
      updatedTime: formatTime(item.update_time),
    }));
  }
};

onMounted(() => {
  getMedicineList();
});

const sizeChange = (newSize: number) => {
  pageSize.value = newSize;
  currentPage.value = 1;
  getMedicineList();
};

const formatTime = (timeString: string): string => {
  const date = new Date(timeString);
  return `${date.getFullYear()}-${(date.getMonth() + 1)
    .toString()
    .padStart(2, "0")}-${date
    .getDate()
    .toString()
    .padStart(2, "0")} ${date
    .getHours()
    .toString()
    .padStart(2, "0")}:${date
    .getMinutes()
    .toString()
    .padStart(2, "0")}:${date.getSeconds().toString().padStart(2, "0")}`;
};

const getImageUrl = (imageUrl) => {
  return imageUrl ? imageUrl : defaultImage;
};
</script>

<style scoped>
.common-layout {
  display: flex;
  flex-direction: column;
  height: 100vh; /* Full viewport height */
  width: 100%;
}

.container {
  width: 100%;
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
  padding: 20px;
  background-color: #f7f7f7;
}

.content-card {
  /* width: 100%; Use full width of the container */
  width: 1200px;
  max-width: 1200px; /* Set a max width to avoid stretching too much on very wide screens */
  height: 100%;
  background-color: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow-y: auto; /* Allow scrolling */
}

.comment {
  width: 100%;
}

.header {
  text-align: center;
  margin-bottom: 20px;
}

.comment-box {
  display: flex;
  padding: 20px;
  margin-bottom: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  transition: box-shadow 0.3s;
}

.comment-box:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.comment-image {
  flex-shrink: 0;
  width: 120px;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
}

.comment-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.comment-content {
  flex-grow: 1;
  margin-left: 20px;
  color: #333;
}

.medicine-name {
  font-size: 1.25rem;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.comment-date {
  font-size: 0.875rem;
  color: #888;
  margin-bottom: 10px;
  text-align: right;
}

.inline-container {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.num-rating {
  background-color: #4db7fe;
  padding: 5px 10px;
  border-radius: 5px;
  color: white;
  font-size: 0.875rem;
}

.comment-title {
  font-size: 0.875rem;
  color: #555;
}

.comment {
  color: #555;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>
