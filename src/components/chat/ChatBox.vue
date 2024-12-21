<template>
  <div class="chat-box">
    <!-- 左侧用户列表 -->
    <el-card class="user-list" shadow="hover">
      <h3 class="list-title">在线用户</h3>
      <div class="user-list-container">
        <el-list>
          <el-list-item
            v-for="user in users"
            :key="user.id"
            :class="{ active: user.userName === selectedUser }"
            @click="selectUser(user.userName)"
          >
            <div class="user-item">
              <img v-if="user.avatar" :src="user.avatar" alt="avatar" class="avatar" />
              <span>{{ user.userName }}</span>
            </div>
          </el-list-item>
        </el-list>
      </div>
    </el-card>

    <!-- 右侧聊天区域 -->
    <el-card class="chat-area" shadow="hover">
      <h3 v-if="selectedUser" class="chat-title">与 {{ selectedUser }} 的聊天</h3>
      <div class="message-list" ref="messageList">
        <!-- 消息列表 -->
        <MessageItem
          v-for="message in messages"
          :key="message.timestamp"
          :message="message"
          :is-self="message.sender"
        />
      </div>

      <!-- 输入框 -->
      <div class="input-container">
        <el-input
          v-model="messageContent"
          placeholder="输入消息..."
          @keyup.enter="sendMessage"
          class="input-message"
          clearable
        >
          <template #append>
            <el-button type="primary" @click="sendMessage">发送</el-button>
          </template>
        </el-input>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted, watch, onBeforeUnmount, computed } from "vue";
import { ElMessage } from "element-plus";
import MessageItem from "./MessageItem.vue";
import useUserStore from "@/stores/modules/user";
import { useRoute } from "vue-router";

export default {
  components: { MessageItem },
  setup() {
    const ws = ref(null); // WebSocket 实例
    const users = ref([]); // 在线用户列表
    const messages = reactive([]); // 聊天消息列表
    const selectedUser = ref(""); // 当前选中的用户
    const messageContent = ref(""); // 当前输入的消息
    const messageList = ref(null); // 聊天窗口 DOM 引用
    const userStore = useUserStore(); // 用户信息存储
    const route = useRoute(); // 获取路由实例

    const currentUser = computed(() => userStore.getUser); // 使用计算属性动态获取用户信息

    console.log("当前登录用户:", currentUser.value);

    // 获取目标用户
    const targetUserName = route.params.targetUserName || ""; // 从路由参数中获取目标用户
    console.log("目标用户:", targetUserName);

    if (targetUserName) {
      selectedUser.value = targetUserName;
    }

    /**
     * 初始化 WebSocket 连接
     */
    const initWebSocket = () => {
      if (!userStore.token) {
        ElMessage.error("未登录，请重新登录");
        return;
      }

      ws.value = new WebSocket(`ws://localhost:8081/intoChat?token=${userStore.token}`);

      ws.value.onopen = () => {
        console.log("WebSocket 连接成功");
        ElMessage.success("已连接到聊天服务器");
      };

      ws.value.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);

          if (data.status === -1) {
            ElMessage.error(data.message);
            return;
          }

          switch (data.message) {
            case "getUser":
              users.value = data.users;
              break;

            case "loadMessage":
              messages.splice(0, messages.length, ...data.messages);
              scrollToBottom();
              break;

            case "sendMessage":
              messages.push(...data.messages);
              scrollToBottom(); // 滚动到底部
              break;

            default:
              console.warn("未知消息类型:", data.message);
          }
        } catch (error) {
          console.error("WebSocket 消息解析失败:", error);
        }
      };

      ws.value.onclose = () => {
        console.log("WebSocket 连接关闭");
        ElMessage.warning("连接已断开");
      };

      ws.value.onerror = (error) => {
        console.error("WebSocket 错误:", error);
        ElMessage.error("WebSocket 连接错误");
      };
    };

    /**
     * 选择用户
     * @param {string} userName 用户名
     */
    const selectUser = (userName) => {
      selectedUser.value = userName;
      messages.splice(0, messages.length);

      if (ws.value) {
        console.log(`加载与 ${userName} 的聊天记录`);
        ws.value.send(
          JSON.stringify({
            message: "loadMessage",
            from: currentUser.value.userName,
            to: userName,
          })
        );
      }
    };

    /**
     * 发送消息
     */
    const sendMessage = () => {
      if (!currentUser.value || !currentUser.value.userName) {
        ElMessage.error("用户未登录，请重新登录");
        return;
      }

      if (!messageContent.value.trim()) {
        ElMessage.warning("请输入消息内容");
        return;
      }

      if (ws.value) {
        ws.value.send(
          JSON.stringify({
            message: "sendMessage",
            from: currentUser.value.userName,
            to: selectedUser.value,
            content: messageContent.value,
          })
        );
        messageContent.value = "";
      } else {
        ElMessage.error("未连接到 WebSocket 服务器，无法发送消息");
      }
    };

    /**
     * 滚动消息列表到底部
     */
    const scrollToBottom = () => {
      if (messageList.value) {
        messageList.value.scrollTop = messageList.value.scrollHeight;
      }
    };

    /**
     * 清理 WebSocket 连接
     */
    const cleanWebSocket = () => {
      if (ws.value) {
        ws.value.close();
        ws.value = null;
      }
    };

    // 初始化 WebSocket 连接
    onMounted(() => {
      initWebSocket();
    });

    // 组件销毁时清理 WebSocket
    onBeforeUnmount(() => {
      cleanWebSocket();
    });

    return {
      users,
      selectedUser,
      messages,
      messageContent,
      messageList,
      sendMessage,
      selectUser,
    };
  },
};
</script>

<style scoped>
.chat-box {
  display: flex;
  gap: 20px;
  height: calc(100vh - 120px);
}
/* 左侧用户列表 */
.user-list {
  width: 25%;
  border-radius: 8px;
  background-color: #ffffff;
}

.list-title {
  text-align: center;
  font-weight: bold;
  margin-bottom: 10px;
}

.user-list-container {
  max-height: calc(100vh - 140px);
  overflow-y: auto;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.user-item:hover {
  background-color: #eef2f7;
}

.user-item.active {
  background-color: #dce6f1;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

/* 右侧聊天区域 */
.chat-area {
  width: 75%;
  display: flex;
  flex-direction: column;
}

.chat-title {
  text-align: center;
  font-weight: bold;
  margin-bottom: 10px;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  height: 500px;
  border: 1px solid #eaeaea;
  padding: 10px;
  background-color: #f9f9f9;
}

.input-container {
  margin-top: 10px;
  display: flex;
  align-items: center;
}

.input-message {
  flex: 1;
}
</style>
