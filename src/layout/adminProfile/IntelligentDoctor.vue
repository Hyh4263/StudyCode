<template>
  <div style="height: 100%">
    <el-row :gutter="20">
      <!-- 聊天框 -->
      <el-col :span="18">
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

      <!-- 用户列表 -->
      <el-col :span="6" class="user-list">
        <el-card class="user-list-card">
          <div class="list-header">
            <h4>用户列表</h4>
          </div>
          <el-scrollbar class="list-scroll">
            <div
              v-for="user in users"
              :key="user.id"
              class="user-item"
              @click="redirectToUserCommunication(user)"
            >
              <img :src="user.avatar" alt="用户头像" class="user-avatar" />
              <div class="user-info">
                <p class="user-name">{{ user.userName }}</p>
              </div>
            </div>
          </el-scrollbar>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router"; // 使用 Vue Router
import ChatMessage from "@/components/chat/index.vue";
import { reqDoctorMessage, fetchRealDoctors } from "@/api/doctor/index";
import useUserStore from "@/stores/modules/user";
import { ElMessage } from "element-plus";

interface User {
  id: number;
  userName: string;
  avatar: string;
}

let userStore = useUserStore();
const router = useRouter(); // 获取路由实例

// 当前登录用户信息
const currentUser = ref({
  id: userStore.id,
  userName: userStore.userName,
});

// 消息队列
const messages = ref([
  {
    sender: "doctor",
    content: `${currentUser.value.userName}你好，我是您的智能专属医生陪伴，身体不舒服或者有任何需要咨询的问题，都可以向我提问，我会全心全意为您解答！`,
  },
]);

// 用户列表
const users = ref<User[]>([]);

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
        ElMessage.error("智能医生暂时无法处理您的请求，请稍后重试。");
        messages.value.push({
          sender: "doctor",
          content: "对不起，暂时无法处理您的请求。",
        });
      }
    } catch (error) {
      console.error("请求失败:", error);
      messages.value.push({ sender: "doctor", content: "网络错误，请稍后重试。" });
    } finally {
      isLoading.value = false;
    }
  }
};

// 获取用户列表
onMounted(() => {
  fetchDoctors();
});

const fetchDoctors = async () => {
  try {
    const res = await fetchRealDoctors();
    const rawUsers = res.data;
    console.log("原始用户列表:", rawUsers);

    // 过滤掉无效用户数据以及当前登录用户
    const validUsers = Object.values(rawUsers).filter(
      (user: any) =>
        user !== null &&
        user.id &&
        user.userName &&
        user.userName !== currentUser.value.userName // 排除当前登录用户
    );

    // 转换用户数据格式为前端需要的结构
    users.value = validUsers.map((user: any) => ({
      id: user.id,
      userName: user.userName,
      avatar: user.imgPath,
    }));

    console.log("用户列表（过滤当前用户后）:", users.value);
  } catch (error) {
    console.error("获取用户列表失败:", error);
    ElMessage.error("获取用户列表失败，请稍后重试。");
  }
};

// 跳转到用户通信模块
const redirectToUserCommunication = (user: any) => {
  router.push({
    name: "Chat",
    params: { targetUserName: user.userName }, // 通过 params 传递目标用户信息
  });
};
</script>

<style scoped>
/* 布局样式 */
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

/* 聊天框样式 */
.chat-box {
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

/* 用户列表样式 */
.user-list {
  height: 100%;
  background-color: #ffffff;
}

.user-list-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.list-header {
  text-align: center;
  margin-bottom: 10px;
}

.list-scroll {
  flex-grow: 1;
  overflow-y: auto;
  padding: 10px;
}

.user-item {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.user-item:hover,
.user-item.selected {
  background-color: #e6f7ff;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 16px;
  font-weight: bold;
  margin: 0;
}
</style>
