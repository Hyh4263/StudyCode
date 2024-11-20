<template>
  <div class="health-layout">
    <el-container>
      <!-- Header -->
      <Header></Header>
      <!-- Main Content -->
      <el-main class="content">
        <!-- Carousel for Card Groups -->
        <el-carousel height="100%" :interval="5000" arrow="always" class="carousel">
          <el-carousel-item v-for="(group, index) in groupedHealthData" :key="index">
            <el-row :gutter="24">
              <el-col :span="6" v-for="item in group" :key="item.id">
                <el-card
                  shadow="always"
                  class="health-card"
                  @click="navigateToDetail(item.id)"
                >
                  <!-- Display Image or Video Based on Availability -->
                  <div v-if="!item.showVideo && item.img_path" class="media-container">
                    <img :src="item.img_path" alt="Image" class="card-image" />
                  </div>
                  <div v-else-if="item.video_path" class="media-container">
                    <video :src="item.video_path" controls autoplay class="card-video" />
                  </div>

                  <div class="card-content">
                    <h3 class="card-title">{{ truncatedText(item.title, 25) }}</h3>
                    <p class="card-description">
                      {{ truncatedText(item.topic_content, 150) }}
                    </p>
                  </div>

                  <!-- Footer with absolute positioning for left and right alignment -->
                  <div class="card-footer">
                    <p class="view-count">浏览量：{{ item.view_number }}</p>
                    <el-button
                      type="primary"
                      class="learn-more-button"
                      @click.stop="toggleVideo(item)"
                    >
                      {{ item.showVideo ? "关闭视频" : "播放视频" }}
                    </el-button>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-carousel-item>
        </el-carousel>
      </el-main>
    </el-container>
    <Footer></Footer>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { ElNotification } from "element-plus";
import { reqHealthyList } from "@/api/healthy/index";

const currentPage = ref(1);
const pageSize = ref(6);
const total = ref<number>(0);
const healthData = ref<any[]>([]);
const router = useRouter();

const truncatedText = (text: string, length: number) =>
  text.length > length ? text.slice(0, length) + "..." : text;

const toggleVideo = (item: any) => {
  item.showVideo = !item.showVideo;
};

const navigateToDetail = (id: string) => {
  router.push(`healthy/${id}`);
};

const fetchHealthTopics = async (page = 1) => {
  currentPage.value = page;
  try {
    const result: any = await reqHealthyList(currentPage.value, pageSize.value);
    console.log(result);
    if (result.code === 200) {
      total.value = result.data.totalElements;
      healthData.value = result.data.healthyList
        .filter((item: any) => item.status === 1)
        .map((item: any) => ({
          id: item.id,
          title: item.title,
          topic_content: item.topicContent,
          img_path: item.imgPath,
          video_path: item.videoPath,
          view_number: item.viewNumber,
          showVideo: false,
        }));
    }
  } catch (error) {
    ElNotification({
      type: "error",
      message: "获取健康话题列表失败",
    });
  }
};

const groupedHealthData = computed(() => {
  const groups = [];
  for (let i = 0; i < healthData.value.length; i += 4) {
    groups.push(healthData.value.slice(i, i + 4));
  }
  return groups;
});

onMounted(() => {
  fetchHealthTopics();
});
</script>
<style scoped>
.health-layout {
  position: absolute;
  inset: 0;
}

.el-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.content {
  background-color: #f3f7fa;
  padding: 20px;
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.carousel {
  width: 100%;
  height: 100%;
}

.media-container {
  width: 100%;
  height: 180px;
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 15px;
}

.card-image,
.card-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.health-card {
  height: 600px;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
  background-color: #fff;
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.health-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.card-content {
  flex: 1;
}

.card-title {
  font-size: 18px;
  color: #333;
  margin: 10px 0;
  line-height: 1.2;
}

.card-description {
  color: #666;
  line-height: 1.6;
  font-size: 14px;
  margin-bottom: 10px;
}

/* Absolute positioning for footer content */
.card-footer {
  position: absolute;
  bottom: 10px;
  left: 0;
  right: 0;
  padding: 0 20px;
  display: block;
  text-align: center;
}

.view-count {
  position: absolute;
  left: 20px;
  bottom: 10px;
  color: #888;
  font-size: 14px;
}

.learn-more-button {
  position: absolute;
  right: 20px;
  bottom: 10px;
  background-color: #007bff;
  border-radius: 8px;
  padding: 6px 12px;
  font-size: 14px;
}

.learn-more-button:hover {
  background-color: #0056b3;
  color: #fff;
}
</style>
