<template>
  <div class="common-layout">
    <el-container>
      <Header></Header>
      <el-main class="content">

        <el-empty v-if="loading" description="正在加载..." class="empty-state" />
        <!-- Add loading effect to the main content card -->
        <el-card class="content-card" v-else=>
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
                <div @click="navigateToMedicine(medicine)">
                  <h5 class="medicine-name">{{ medicine.name }}</h5>
                  <p class="comment-date">{{ medicine.band }}牌</p>
                  <div class="inline-container">
                    <span class="num-rating">{{ getMedicineType(medicine.type) }}</span>
                    <span class="comment-title">{{ medicine.keyword }}</span>
                  </div>
                  <p class="comment">{{ medicine.effect }}</p>
                </div>
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
      <Footer></Footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { reqMedicineList } from "@/api/medicine";
import defaultImage from "@/assets/images/logo.png";

const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(0);
const loading = ref(false); // Loading state
const router = useRouter();
const medicines = ref([]);

const getMedicineType = (type: any) => {
  return type === 0 ? "西药" : type === 1 ? "中药" : "中性药";
};

const navigateToMedicine = (medicine: any) => {
  router.push(`/medicine/${medicine.id}`);
};

const getMedicineList = async (pager = 1) => {
  currentPage.value = pager;
  loading.value = true; // Start loading
  try {
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
  } finally {
    loading.value = false; // End loading
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
  position: absolute;
  height: 100vh;
  inset: 0;
  display: flex;
  flex-direction: column;
}

.el-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.header {
  height: 60px;
  text-align: center;
  margin-bottom: 20px;
  font-size: 20px;
  border-bottom: 1px solid #eaeaea;
}

.content {
  flex: 1;
  background-color: #f3f7fa;
  padding: 20px;
  box-sizing: border-box;
  overflow-y: auto;
}

.empty-state {
  color: #8e99ab;
  font-size: 18px;
  text-align: center;
  padding: 50px 0;
}

.content-card {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  background-color: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.comment {
  width: 100%;
}

.comment-box {
  display: flex;
  padding: 20px;
  margin-bottom: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  transition: box-shadow 0.3s, transform 0.3s;
  cursor: pointer;
}

.comment-box:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-5px);
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

/* 响应式设计 */
@media (max-width: 768px) {
  .content-card {
    padding: 15px;
  }

  .comment-box {
    flex-direction: column;
    align-items: center;
  }

  .comment-image {
    margin-bottom: 15px;
  }

  .comment-content {
    margin-left: 0;
    text-align: center;
  }

  .medicine-name {
    font-size: 1.125rem;
  }

  .comment-date,
  .comment-title {
    font-size: 0.8rem;
  }

  .comment {
    font-size: 0.875rem;
  }
}
</style>
