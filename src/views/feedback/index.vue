<template>
  <div class="common-layout">
    <el-container>
      <Header />
      <el-main class="content">
        <div class="overlay"></div>
        <div class="content-main">
          <div class="header-section">
            <h1>User Feedback</h1>
          </div>
          <form @submit.prevent="submitForm" class="form-report">
            <!-- 标题输入框 -->
            <label class="form-group">
              <span>Title:</span>
              <input
                v-model="title"
                type="text"
                class="input"
                placeholder="Enter your feedback title"
              />
            </label>
            <!-- 联系方式类型 -->
            <label class="form-group">
              <span>Contact:</span>
              <select v-model="contactMethod" class="select">
                <option :value="1">Email</option>
                <option :value="2">Phone</option>
              </select>
              <input
                v-model="contact"
                type="text"
                class="input"
                :placeholder="
                  contactMethod === 1 ? 'Enter your email' : 'Enter your phone number'
                "
              />
            </label>
            <!-- 反馈内容 -->
            <label class="form-group">
              <span>Message:</span>
              <textarea
                v-model="content"
                class="textarea"
                placeholder="Type your message here"
              ></textarea>
            </label>
            <!-- 反馈主题 -->
            <label class="form-group">
              <span>Type:</span>
              <select v-model="type" class="select">
                <option :value="1">Advice</option>
                <option :value="2">Problem</option>
              </select>
            </label>
            <!-- 提交按钮 -->
            <div class="form-footer">
              <button class="button">Send</button>
            </div>
          </form>
        </div>
      </el-main>
      <Footer />
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import { reqAddOrUpdatefeedBack } from "@/api/feedback/index";
import { ElNotification, ElMessage } from "element-plus";
import useUserStore from "@/stores/modules/user";

const title = ref(""); // 标题
const contactMethod = ref(1); // 默认为1, 1为邮箱 2为电话
const contact = ref("");
const content = ref("");
const type = ref(1); // 默认值为1，1为建议，2为问题
const userStore = useUserStore();

// 验证规则
const validateContact = (): boolean => {
  if (contactMethod.value === 1) {
    // Email 验证规则
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(contact.value)) {
      ElMessage({
        type: "warning",
        message: "请输入有效的Email地址",
      });
      return false;
    }
  } else if (contactMethod.value === 2) {
    // Phone 验证规则
    const phoneRegex = /^\d{10,11}$/; // 假设电话号码为10-11位数字
    if (!phoneRegex.test(contact.value)) {
      ElMessage({
        type: "warning",
        message: "请输入有效的电话号码",
      });
      return false;
    }
  }
  return true;
};

const submitForm = async () => {
  // 进行联系方式验证
  if (!validateContact()) {
    return; // 验证失败，阻止提交
  }

  const feedback = {
    userId: userStore.id,
    name: userStore.userName,
    title: title.value,
    contactMethod: contactMethod.value,
    contact: contact.value,
    content: content.value,
    type: type.value, // 传递数值给后端
    status: 1, //默认反馈状态为未处理
  };
  console.log(feedback);

  const result: any = await reqAddOrUpdatefeedBack(feedback);
  if (result.code === 200) {
    title.value = "";
    contactMethod.value = 1;
    contact.value = "";
    content.value = "";
    type.value = 1; // 重置为默认值
    ElNotification({
      type: "success",
      message: "Feedback sent successfully",
    });
  } else {
    ElNotification({
      type: "error",
      message: "Failed to send feedback",
    });
  }
};
</script>

<style scoped>
/* 保持之前的样式不变 */
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

.content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: url("@/assets/images/background2.png") no-repeat center center;
  background-size: cover;
  position: relative;
  overflow: hidden;
}

.overlay {
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.5); /* Dark overlay to improve readability */
  z-index: 1;
}

.content-main {
  position: relative;
  max-width: 500px;
  width: 100%;
  padding: 40px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.85); /* Semi-transparent white background */
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(8px); /* Slight blur effect for a frosted glass look */
  z-index: 2;
  max-height: 80vh; /* Limit height to enable scrolling */
  overflow-y: auto; /* Enable vertical scrolling */
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

.header-section h1 {
  font-size: 24px;
  color: #333;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
  color: #4a4a4a;
}

.form-report {
  display: flex;
  flex-direction: column;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}

.form-group > span {
  font-weight: 600;
  color: #666;
  margin-bottom: 8px;
}

.input,
.select,
.textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  color: #333;
  transition: border-color 0.3s;
}

.input:focus,
.select:focus,
.textarea:focus {
  border-color: #409eff;
  outline: none;
}

.select {
  height: 40px;
  appearance: none;
  background-color: #ffffff;
}

.textarea {
  height: 120px;
  resize: none;
}

.form-footer {
  display: flex;
  justify-content: center;
}

.button {
  padding: 12px 32px;
  border-radius: 8px;
  border: none;
  background-color: #409eff;
  color: #ffffff;
  font-weight: bold;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.button:hover {
  background-color: #66b1ff;
}

.button:active {
  background-color: #0073e6;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .content-main {
    padding: 20px;
    width: 90%;
  }

  .header-section h1 {
    font-size: 20px;
  }

  .button {
    width: 100%;
    padding: 12px;
    font-size: 14px;
  }
}
</style>
