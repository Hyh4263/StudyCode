<template>
  <div class="common-layout">
    <el-container>
      <!-- 头部区域 -->
      <Header></Header>
      <!-- 内容区域 -->
      <el-main class="banner">
        <div class="banner-content">
          <div class="banner-text">
            <h1 class="banner-title">
              智慧医药 <br /><span class="highlight"
                >最好的医疗咨询和自我诊断健康中心</span
              >
            </h1>
            <p class="banner-description">
              The best healthcare and self diagnostic health center
            </p>
            <div class="banner-buttons">
              <el-button type="primary">立即开始</el-button>
              <el-button>了解更多</el-button>
            </div>
          </div>
          <div class="banner-image">
            <img src="@/assets/images/img-1.png" alt="Banner Image" />
          </div>
        </div>
      </el-main>
    </el-container>
    <!-- 底部区域 -->
    <Footer></Footer>
  </div>
</template>

<script setup lang="ts">
// import { Search } from '@element-plus/icons-vue';
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
</script>

<style scoped>
.common-layout {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
}

.el-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.logo {
  flex-shrink: 0;
  margin-right: auto;
}

.nav-menu {
  flex-grow: 1;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.banner {
  background-color: #e0f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  height: calc(100vh - 64px);
}

.banner-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 1200px;
  padding: 20px;
}

.banner-text {
  max-width: 600px;
}

.banner-title {
  font-size: 48px;
  line-height: 1.2;
  color: #000000;
}

.banner-title .highlight {
  color: #00bfa5;
}

.banner-description {
  margin: 20px 0;
  font-size: 18px;
  color: #666666;
}

.banner-buttons {
  display: flex;
  gap: 10px;
}

.banner-image img {
  max-width: 100%;
  height: auto;
}

.footer {
  background-color: #333333;
  color: #ffffff;
  text-align: center;
  padding: 20px 0;
  font-size: 14px;
}
</style>
