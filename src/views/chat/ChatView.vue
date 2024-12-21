<template>
  <div class="common-layout">
    <el-container>
      <Header></Header>
      <el-main class="content">
        <div>
          <ChatBox />
        </div>
      </el-main>
    </el-container>
    <Footer />
  </div>
</template>

<script setup lang="ts">
import ChatBox from "../../components/chat/ChatBox.vue";
import Header from "../../components/header/index.vue";
import Footer from "@/components/footer/index.vue";
</script>
<style scoped>
.common-layout {
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
</style>
