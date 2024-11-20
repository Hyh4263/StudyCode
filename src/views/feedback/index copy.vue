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
          <form @submit.prevent="handleSubmit" class="form-report">
            <label class="form-group">
              <span>Contact:</span>
              <select v-model="contactMethod" class="select">
                <option value="Email">Email</option>
                <option value="Phone">Phone</option>
                <option value="QQ">QQ</option>
              </select>
              <input
                v-model="contactInfo"
                type="text"
                class="input"
                placeholder="Enter your contact info"
              />
            </label>
            <label class="form-group">
              <span>Message:</span>
              <textarea
                v-model="message"
                class="textarea"
                placeholder="Type your message here"
              ></textarea>
            </label>
            <label class="form-group">
              <span>Subject:</span>
              <select v-model="subject" class="select">
                <option value="Job Inquiry">Job Inquiry</option>
                <option value="General Question">General Question</option>
              </select>
            </label>
            <div class="form-footer">
              <button type="submit" class="button">Send</button>
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

const contactMethod = ref("Email");
const contactInfo = ref("");
const message = ref("");
const subject = ref("Job Inquiry");

const handleSubmit = () => {
  const feedback = {
    contactMethod: contactMethod.value,
    contactInfo: contactInfo.value,
    message: message.value,
    subject: subject.value,
  };
  console.log("Form submitted with data:", feedback);
  // Add form submission logic here, such as sending data to an API
};
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
