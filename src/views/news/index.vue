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
        <!-- 分页器 -->
        <el-pagination
          @current-change="getIllnesses"
          @size-change="sizeChange"
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[3, 5, 7, 9]"
          :background="true"
          layout="prev, pager, next, jumper, ->, sizes, total"
          :total="total"
        />
      </el-main>
      <Footer></Footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { reqIllnessList } from "@/api/news";

const pageTitle = ref("全部");
const newsItems = ref<any[]>([]);

const currentPage = ref(1);
const pageSize = ref(5);
const total = ref<number>(0);

// 分页器
const sizeChange = (newSize: number) => {
  pageSize.value = newSize;
  currentPage.value = 1;
  getIllnesses();
};

// 获取疾病列表
const getIllnesses = async (page = 1) => {
  currentPage.value = page;
  const result: any = await reqIllnessList(currentPage.value, pageSize.value);
  console.log("疾病列表数据", result);
  if (result.code === 200) {
    total.value = result.data.totalElements;
    newsItems.value = result.data.illness.map((item: any) => ({
      id: item.id,
      title: item.illness_name,
      description: item.include_reason,
      date: new Date(item.update_time).toLocaleDateString(),
      category: item.kindName,
      views: item.pageview,
    }));
  }
};

onMounted(() => {
  getIllnesses();
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
