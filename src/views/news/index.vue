<template>
  <div class="common-layout">
    <el-container class="container">
      <!-- 头部区域 -->
      <Header></Header>
      <!-- 内容区域 -->
      <el-main class="main">
        <div class="title">{{ pageTitle }}</div>
        <el-row :gutter="24" class="news-list">
          <el-col
            style="padding: 12px 12px"
            :span="8"
            v-for="(item, index) in newsItems"
            :key="index"
          >
            <el-card class="news-item">
              <div class="news-content">
                <router-link :to="'/news/' + item.id">
                  <h3>{{ item.title }}</h3>
                  <p>{{ item.description }}</p>
                  <div class="news-footer">
                    <span>{{ item.date }}</span>
                    <span class="news-category">{{ item.category }}</span>
                    <el-icon>
                      <View />
                    </el-icon>
                    <span>{{ item.views }}</span>
                  </div>
                </router-link>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
      <Footer></Footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { reqHasNewsList } from "@/api/news";

const pageTitle = ref("全部");
const newsItems: any = ref([]);

const fetchNewsList = async () => {
  try {
    const response: any = await reqHasNewsList();
    if (response.code === 200 && response.data) {
      pageTitle.value = response.data.title;
      newsItems.value = response.data.illness.map((item: any) => ({
        id: item.id,
        title: item.illness_name,
        description: item.include_reason,
        date: new Date(item.update_time).toLocaleDateString(),
        category: item.kindName,
        views: item.pageview,
      }));
    } else {
      console.error("Error fetching news items: ", response.message);
    }
  } catch (error) {
    console.error("Error fetching news items:", error);
  }
};

onMounted(() => {
  fetchNewsList();
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
  flex: 1;
  padding: 20px;
}

.title {
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
}

.news-list {
  display: flex;
  flex-wrap: wrap;
}

.news-item {
  height: 100%;
  /* 保证卡片高度一致 */
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.news-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.news-content h3 {
  font-size: 20px;
  margin-bottom: 10px;
}

.news-content p {
  flex-grow: 1;
  margin-bottom: 10px;
}

.news-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  border-top: 1px solid #eaeaea;
  padding-top: 10px;
  font-size: 14px;
  color: #888;
}
</style>
