<template>
  <div style="height: 100%">
    <el-col :span="20">
      <el-card class="box-card">
        <div class="profile-content">
          <h4>智能在线医生</h4>
          <div class="chat-box">
            <ChatMessage :messages="messages" />
            <!-- 如果 isLoading 为 true，则显示加载提示 -->
            <div v-if="isLoading" class="loading-message">智能医生正在搜索中...</div>
          </div>
          <el-input v-model="newMessage" placeholder="输入要咨询的内容..."></el-input>
          <el-button type="primary" @click="sendMessage" :disabled="isLoading"
            >发送</el-button
          >
        </div>
      </el-card>
    </el-col>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import ChatMessage from "@/components/chat/index.vue";
import { reqDoctorMessage } from "@/api/doctor/index";
import useUserStore from "@/stores/modules/user";

let userStore = useUserStore();

// 存储用户信息
const user = ref({
  userName: userStore.userName,
});

// 消息队列
// 消息队列
const messages = ref([
  {
    sender: "doctor",
    content: `${user.value.userName}你好，我是您的智能专属医生陪伴，身体不舒服或者有任何需要咨询的问题，都可以向我提问，我会全心全意为您解答！`,
  },
]);

// 输入的消息和加载状态
const newMessage = ref("");
const isLoading = ref(false);

// 发送消息
const sendMessage = async () => {
  if (newMessage.value.trim() !== "") {
    // 先将用户的消息添加到消息队列中
    messages.value.push({ sender: "user", content: newMessage.value });
    const userMessage = newMessage.value; // 备份用户输入的内容
    newMessage.value = ""; // 清空输入框

    // 显示加载提示
    isLoading.value = true;

    try {
      // 调用 API 请求智能医生回复
      const res = await reqDoctorMessage(userMessage);

      // 收到响应后处理医生的回复
      if (res.code === 200) {
        messages.value.push({ sender: "doctor", content: res.data });
      } else {
        console.error("API 返回了一个错误响应:", res);
        messages.value.push({
          sender: "doctor",
          content: "对不起，暂时无法处理您的请求。",
        });
      }
    } catch (error) {
      console.error("请求失败:", error);
      messages.value.push({ sender: "doctor", content: "网络错误，请稍后重试。" });
    } finally {
      // 不管成功还是失败，取消加载提示
      isLoading.value = false;
    }
  }
};
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

.profile-sidebar {
  text-align: center;
}
.profile-content {
  height: 100%;
}
.box-card {
  /* height: 800px; */
}

.profile-userpic img {
  width: 200px;
  height: 200px;
  border-radius: 50%;
}

.chat-box {
  /* height: 100%; */
  height: 500px;
  overflow: auto;
  border: 1px solid #ebebeb;
  padding: 10px;
  margin-bottom: 10px;
}

.loading-message {
  color: gray;
  font-style: italic;
  text-align: center;
  margin: 10px 0;
}
</style>
