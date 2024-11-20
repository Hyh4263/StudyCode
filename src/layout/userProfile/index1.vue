<template>
  <div class="common-layout">
    <el-container class="container">
      <Header />

      <el-main>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="box-card">
              <div class="profile-sidebar">
                <div class="profile-userpic">
                  <el-image style="width: 150px; height: 150px" :src="user.imgPath" alt="相关图片" />
                </div>
                <div class="profile-usertitle">
                  <!-- <div class="profile-usertitle-name">{{ user.userName }}</div>
                  <div class="profile-usertitle-job">{{ user.userTel }}</div>
                  <div class="profile-usertitle-job">{{ user.userEmail }}</div> -->
                  <h3 class="mb-1" th:text="{{ user.userName }}">{{ user.userName }}</h3>
                  <p class="detail"><i class="fa fa-phone"></i> {{ user.userTel }}</p>
                  <p class="detail"><i class="fa fa-envelope-o"></i> {{ user.userEmail }}</p>
                </div>
                <div class="profile-userbuttons">
                  <el-menu>
                    <el-menu-item index="3" disabled>
                      <el-icon>
                        <document />
                      </el-icon>
                      <span>Navigator Three</span>
                    </el-menu-item>
                    <el-menu-item index="4">
                      <el-icon>
                        <setting />
                      </el-icon>
                      <span>我的资料</span>
                    </el-menu-item>
                    <el-menu-item index="5">
                      <el-icon>
                        <setting />
                      </el-icon>
                      <span>智能医生</span>
                    </el-menu-item>
                    <el-menu-item index="6">
                      <el-icon>
                        <setting />
                      </el-icon>
                      <span>退出登录</span>
                    </el-menu-item>
                  </el-menu>
                </div>
              </div>
            </el-card>
          </el-col>

          <el-col :span="18">
            <el-card class="box-card">
              <div class="profile-content">
                <h4>智能在线医生</h4>
                <div class="chat-box">
                  <ChatMessage :messages="messages" />
                </div>
                <el-input v-model="newMessage" placeholder="输入要咨询的内容..."></el-input>
                <el-button type="primary" @click="sendMessage">发送</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
      <Footer></Footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Header from '@/components/header/index.vue';
import Footer from '@/components/footer/index.vue';
// import ChatMessage from '@/components/chat/index.vue'; // 确保您有这个组件

const user = ref({
  imgPath: 'assets/images/profile.jpg', // 默认图片路径
  userName: '张三',
  userTel: '17879544343',
  userEmail: 'isxuewei@qq.com'
});

const messages = ref([
  { sender: 'doctor', content: '张三你好，我是您的智能专属医生陪伴，身体不舒服或者有任何需要咨询的问题，都可以向我提问，我会全心全意为您解答！' }
]);

const newMessage = ref('');

const editProfile = () => {
  // 编辑资料逻辑
};

const logout = () => {
  // 退出登录逻辑
};

const sendMessage = () => {
  if (newMessage.value.trim() !== '') {
    messages.value.push({ sender: 'user', content: newMessage.value });
    newMessage.value = '';
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

.profile-userpic img {
  width: 200px;
  height: 200px;
  border-radius: 50%;
}

.profile-usertitle {
  margin-top: 20px;
}

.profile-usertitle-name {
  font-size: 16px;
  font-weight: 600;
  color: #5a7391;
}

.profile-usertitle-job {
  font-size: 12px;
  color: #5b9bd1;
}

.profile-edit-btn,
.profile-logout-btn {
  margin-top: 10px;
}

.chat-box {
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #ebebeb;
  padding: 10px;
  margin-bottom: 10px;
}
</style>
