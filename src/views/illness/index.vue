<template>
  <div class="common-layout">
    <el-container>
      <!-- 头部区域 -->
      <Header></Header>
      <!-- 内容区域 -->
      <el-main class="content">
        <!-- Loading State -->
        <el-empty v-if="loading" description="正在加载..." class="empty-state" />
        <el-card class="box-card" v-else>
          <div class="title">{{ pageTitle }}</div>
          <el-row :gutter="24" class="illness-list">
            <el-col
              :span="8"
              v-for="(item, index) in illnessItems"
              :key="index"
              :class="['illness-col', { 'last-col': index % 3 === 2 }]"
            >
              <el-card class="illness-item" @click="navigateToIllness(item.id)">
                <div class="illness-content">
                  <h3 style="border-bottom: 1px solid #eaeaea">{{ item.title }}</h3>
                  <p>{{ item.description }}</p>
                  <div class="illness-footer">
                    <span>{{ item.date }}</span>
                    <span class="illness-category">{{ item.category }}</span>
                    <el-icon>
                      <View />
                    </el-icon>
                    <span>{{ item.views }}</span>
                  </div>
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
            :page-sizes="[3, 6, 7, 9]"
            :background="true"
            layout="prev, pager, next, jumper, ->, sizes, total"
            :total="total"
          />
        </el-card>
      </el-main>
      <Footer></Footer>
    </el-container>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { ElNotification, ElMessage } from "element-plus";

import { reqIllnessList } from "@/api/illness/index";

const pageTitle = ref("全部");
const illnessItems = ref<any[]>([]);
const loading = ref(false); // 添加加载状态

const currentPage = ref(1);
const pageSize = ref(6);
const total = ref<number>(0);

const router = useRouter();

// 分页器
const sizeChange = (illnessize: number) => {
  pageSize.value = illnessize;
  currentPage.value = 1;
  getIllnesses();
};

// 获取疾病列表
const getIllnesses = async (page = 1) => {
  loading.value = true; // 开始加载
  currentPage.value = page;
  try {
    const result: any = await reqIllnessList(currentPage.value, pageSize.value);
    if (result.code === 200) {
      total.value = result.data.totalElements;
      illnessItems.value = result.data.illness.map((item: any) => ({
        id: item.id,
        title: item.illness_name,
        description: item.include_reason,
        date: new Date(item.update_time).toLocaleDateString(),
        category: item.kindName,
        views: item.pageview,
      }));
    }
  } catch (error) {
    ElNotification({
      type: "error",
      message: "获取疾病列表失败",
    });
  } finally {
    loading.value = false; // 结束加载
  }
};

// 导航到疾病详情页
const navigateToIllness = (id: string) => {
  router.push(`/illness/${id}`);
};

onMounted(() => {
  getIllnesses();
});
</script>
<style scoped>
.common-layout {
  position: absolute;
  height: 130vh;

  inset: 0;
}

.el-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.content {
  background-color: #f3f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: calc(100vh - 64px);
  padding: 0px;
  box-sizing: border-box;
}

.empty-state {
  color: #8e99ab;
  font-size: 18px;
  text-align: center;
  padding: 50px 0;
}

.box-card {
  width: 100%;
  height: 100%;
  overflow-y: auto;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  padding: 0px;
  transition: transform 0.3s ease-in-out;
}

.title {
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
  border-bottom: 1px solid #eaeaea;
}

.illness-col {
  padding: 12px 12px;
  width: 33.3333%; /* 每行3个卡片 */
}

.last-col {
  margin-right: 0; /* 最后一个卡片去掉右边距 */
}

.illness-item {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease-in-out;
  cursor: pointer;
}

.illness-item:hover {
  transform: translateY(-10px);
}

.illness-content {
  display: flex;
  flex-direction: column;
  height: 250px;
  padding: 20px;
}

.illness-content h3 {
  font-size: 20px;
  margin-bottom: 10px;
  color: #333;
}

.illness-content p {
  flex-grow: 1;
  margin-bottom: 10px;
  color: #666;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.illness-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  border-top: 1px solid #eaeaea;
  padding-top: 10px;
  font-size: 14px;
  color: #888;
}

.illness-footer .illness-category {
  margin-left: 10px;
}

@media (max-width: 768px) {
  .illness-col {
    width: 100%; /* 移动设备上每行1个卡片 */
  }

  .illness-content h3 {
    font-size: 18px;
  }

  .illness-content p {
    -webkit-line-clamp: 2;
  }
}
</style>
