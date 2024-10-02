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
                  <el-image
                    style="width: 150px; height: 150px"
                    :src="user.imgPath"
                    alt="相关图片"
                  />
                </div>
                <div class="profile-usertitle">
                  <h3 class="mb-1">{{ user.userName }}</h3>
                  <p class="detail"><i class="fa fa-phone"></i> {{ user.userTel }}</p>
                  <p class="detail">
                    <i class="fa fa-envelope-o"></i> {{ user.userEmail }}
                  </p>
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
            <el-card class="dashboard-list">
              <h4 class="gray">我的资料</h4>
              <div class="dashboard-list-static">
                <div class="edit-profile-photo">
                  <el-image
                    style="width: 150px; height: 150px; border-radius: 50%"
                    :src="user.imgPath"
                    alt=""
                    id="img-preview"
                  />
                  <div class="change-photo-btn">
                    <div class="photoUpload">
                      <span><i class="fa fa-upload"></i> 点击上传</span>
                      <input type="file" class="upload" @change="uploadPhoto" />
                      <input style="display: none" v-model="user.imgPath" type="text" />
                    </div>
                  </div>
                </div>

                <div class="my-profile">
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="账户名称">
                        <el-input v-model="user.userAccount" disabled />
                      </el-form-item>
                      <el-input v-model="user.id" type="text" style="display: none" />
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="真实姓名">
                        <el-input v-model="user.userName" />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="空格性别">
                        <el-input v-model="user.userSex" disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="年龄">
                        <el-input v-model="user.userAge" type="number" />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="邮箱">
                        <el-input v-model="user.userEmail" disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="手机号">
                        <el-input v-model="user.userTel" type="number" />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-form-item label="注册时间">
                    <el-input :value="formattedCreateTime" disabled />
                  </el-form-item>

                  <el-button type="primary" @click="updateProfile">保存修改</el-button>
                </div>
              </div>
            </el-card>

            <el-card class="dashboard-form">
              <h4>修改登录密码</h4>
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="当前密码">
                    <el-input type="password" v-model="oldPassword" placeholder="" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="新密码">
                    <el-input type="password" v-model="newPassword1" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="确认密码">
                    <el-input type="password" v-model="newPassword2" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-button type="primary" @click="updatePassword" class="mt-2"
                    >修改密码</el-button
                  >
                </el-col>
              </el-row>
            </el-card>

            <!-- <el-card class="dashboard-content">
              <div class="message">
                <div class="message-header inbox-item">
                  <div class="inbox-image">
                    <el-image src="assets/images/team/user-2.jpg" alt="image" />
                  </div>
                  <div class="inbox-content">
                    <p>智能在线医生</p>
                    <span>你的专属医生，随时为您服务！</span>
                  </div>
                </div>
                <div class="message-content">
                  <div id="messages" style="height: 260px; overflow-y: auto">
                    <div class="msg-received">
                      <div class="msg-image">
                        <el-image src="assets/images/team/user-2.jpg" alt="image" />
                      </div>
                      <div class="msg-content">
                        <p>现在</p>
                        <p class="msg">{{ user.userName }}你好，我是您的智能专属医生薛伟，身体不舒服或者有任何需要咨询的问题，都可以向我提问，我会全心全意为您解答！</p>
                      </div>
                    </div>
                  </div>

                  <div class="msg-reply">
                    <el-input type="textarea" v-model="newMessage" placeholder="输入要咨询的内容..." />
                    <el-button type="primary" @click="sendMessage" class="mt-2 pull-right">发送</el-button>
                  </div>
                </div>
              </div>
            </el-card> -->
          </el-col>
        </el-row>
      </el-main>
      <Footer />
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";

const user = ref({
  imgPath: "assets/images/profile.jpg", // 默认图片路径
  userName: "张三",
  userTel: "17879544343",
  userEmail: "isxuewei@qq.com",
  userAccount: "zhangsan",
  userSex: "男",
  userAge: 30,
  id: "12345",
  createTime: new Date(),
});

const formattedCreateTime = ref(
  `${user.value.createTime.getFullYear()}年${
    user.value.createTime.getMonth() + 1
  }月${user.value.createTime.getDate()}日`
);

const oldPassword = ref("");
const newPassword1 = ref("");
const newPassword2 = ref("");

const newMessage = ref("");

const uploadPhoto = (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e: any) => {
      user.value.imgPath = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const updateProfile = () => {
  // console.log('Profile updated:', user.value);
};

const updatePassword = () => {
  if (newPassword1.value === newPassword2.value) {
    // console.log('Password updated:', newPassword1.value);
  } else {
    // console.error('Passwords do not match');
  }
};

const sendMessage = () => {
  if (newMessage.value.trim() !== "") {
    console.log("Message sent:", newMessage.value);
    newMessage.value = "";
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

.dashboard-list-static {
  padding: 20px;
}

.edit-profile-photo img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
}

.change-photo-btn {
  margin-top: 10px;
}

.my-profile {
  margin-top: 20px;
}

.dashboard-content {
  margin-top: 20px;
}

.message {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 300px;
}

.message-header {
  display: flex;
  align-items: center;
}

.inbox-image img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.inbox-content {
  margin-left: 10px;
}

.msg-received {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
}

.msg-received .msg-image img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}

.msg-received .msg-content {
  margin-left: 10px;
}

.msg-reply {
  margin-top: 10px;
}
</style>
