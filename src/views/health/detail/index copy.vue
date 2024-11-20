<template>
  <div class="common-layout">
    <el-container>
      <!-- Header -->
      <Header />

      <!-- Main Content -->
      <el-main>
        <div class="content">
          <div class="content-main"></div>
        </div>
      </el-main>
      <!-- Footer -->
      <Footer />
    </el-container>
  </div>
</template>

<script setup lang="ts">
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { ref, onMounted, computed } from "vue";
import { useRouter, useRoute } from "vue-router";

import { ElNotification } from "element-plus";
import { reqHealthyById } from "@/api/healthy/index";
import { View } from "@element-plus/icons-vue";

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
const medicines = ref<any[]>([]);
const loading = ref(true);

const fetchIllnessDetails = async (id: number) => {
  loading.value = true;
  try {
    const res: any = await reqHealthyById(id);
    console.log("res:", res);
    if (res.code === 200) {
      Object.assign(eczemaInfo, res.data.Healthy);
    }
    console.log("eczemaInfo:", eczemaInfo);

    // eczemaInfo.value = response.data.;
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
}

.container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background-size: cover;
  position: relative;
  overflow: hidden;
}

.content-main {
  position: relative;
  width: 90%;
  height: 90%;
  padding: 40px;
  border-radius: 10px;
  /* background: rgba(255, 255, 255, 0.85); Semi-transparent white background */
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(8px); /* Slight blur effect for a frosted glass look */
  z-index: 2;
  overflow-y: auto; /* Enable vertical scrolling */
}

.topic-card {
  padding: 20px;
  margin-bottom: 20px;
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  background-color: #fff;
}

.topic-title {
  font-size: 28px;
  font-weight: bold;
  color: var(--primary-color);
}

.topic-meta {
  display: flex;
  gap: 15px;
  font-size: 14px;
  color: var(--tertiary-color);
  margin-top: 10px;
}

.topic-image img {
  width: 100%;
  border-radius: 8px;
  margin-top: 20px;
  box-shadow: var(--box-shadow);
}

.topic-description {
  font-size: 16px;
  line-height: 1.6;
  color: var(--secondary-color);
  margin-top: 20px;
}

.related-card {
  padding: 20px;
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  background-color: #fff;
}

.related-title {
  font-size: 24px;
  font-weight: bold;
  color: var(--primary-color);
  margin-bottom: 20px;
}

.medicine-card {
  padding: 15px;
  border-radius: 8px;
  box-shadow: var(--box-shadow);
  background-color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.medicine-image img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 15px;
}

.medicine-info {
  width: 100%;
}

.medicine-name {
  font-size: 18px;
  font-weight: bold;
  color: var(--primary-color);
  margin-bottom: 5px;
}

.medicine-category {
  font-size: 14px;
  color: #67c23a;
  margin-bottom: 10px;
}

.medicine-description {
  font-size: 14px;
  color: var(--secondary-color);
  line-height: 1.4;
}

@media (max-width: 768px) {
  .main {
    padding: 20px;
  }

  .topic-title {
    font-size: 24px;
  }

  .related-title {
    font-size: 20px;
  }

  .medicine-card {
    margin-bottom: 15px;
  }
}
</style>
