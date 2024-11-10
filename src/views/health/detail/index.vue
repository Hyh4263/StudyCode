<template>
  <div class="common-layout">
    <el-container>
      <Header />
      <el-main class="content">
        <div class="content-main">
          <!-- 显示标题 -->
          <h1 class="header-section">{{ eczemaInfo.title }}</h1>

          <!-- 图片展示在视频上方 -->
          <div class="media-container">
            <img
              v-if="eczemaInfo.imgPath"
              :src="eczemaInfo.imgPath"
              alt="Health Image"
              class="media-image"
            />
          </div>

          <!-- 显示内容在图片下方 -->
          <p class="topic-content">{{ eczemaInfo.topicContent }}</p>

          <!-- 视频展示在内容下方 -->
          <div class="media-container" v-if="eczemaInfo.videoPath">
            <video :src="eczemaInfo.videoPath" controls class="media-video"></video>
          </div>

          <!-- 显示内容再次在视频下方 -->
          <p class="topic-content">{{ eczemaInfo.topicContent }}</p>

          <!-- 显示详细信息 -->
          <div class="details">
            <p><strong>浏览量：</strong>{{ eczemaInfo.viewNumber }}</p>
            <p><strong>发布时间：</strong>{{ eczemaInfo.createTime }}</p>
          </div>
        </div>
      </el-main>
    </el-container>
    <Footer />
  </div>
</template>

<script setup lang="ts">
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { ElNotification } from "element-plus";
import { reqHealthyById } from "@/api/healthy/index";

const route = useRoute();
const eczemaInfo = ref({
  id: "",
  imgPath: "",
  videoPath: "",
  title: "",
  topicContent: "",
  status: "",
  viewNumber: 0,
  createTime: "",
  updateTime: "",
});
const loading = ref(true);

const fetchIllnessDetails = async (id: number) => {
  loading.value = true;
  try {
    const res: any = await reqHealthyById(id);
    if (res.code === 200) {
      Object.assign(eczemaInfo.value, res.data.Healthy);
    }
  } catch (error) {
    ElNotification({
      type: "error",
      message: "加载健康话题详情失败",
    });
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchIllnessDetails(Number(route.params.id));
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

.el-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: auto;
}

.content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background-size: cover;
  position: relative;
  overflow: auto;
}

.content-main {
  width: 50%;
  height: 100%;
}

.header-section {
  font-size: 24px;
  color: #4a4a4a;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}

.media-container {
  width: 100%;
  margin-bottom: 20px;
  text-align: center;
}

.media-image {
  width: 60%;
  border-radius: 10px;
  margin-bottom: 20px;
}

.media-video {
  width: 100%;
  border-radius: 10px;
}

.topic-content {
  color: #333;
  font-size: 16px;
  line-height: 1.8;
  margin-bottom: 20px;
  text-align: justify;
}

.details {
  font-size: 14px;
  color: #666;
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.details p {
  margin: 0;
}

/* Custom scrollbar style */
.content-main::-webkit-scrollbar {
  width: 8px;
}

.content-main::-webkit-scrollbar-track {
  background: transparent;
}

.content-main::-webkit-scrollbar-thumb {
  background-color: #c0c4cc;
  border-radius: 4px;
}

.content-main::-webkit-scrollbar-thumb:hover {
  background-color: #a0a4ac;
}

@media (max-width: 600px) {
  .content-main {
    width: 90%;
    padding: 20px;
  }

  .header-section {
    font-size: 20px;
  }
}
</style>
