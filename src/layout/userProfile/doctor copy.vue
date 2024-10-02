<template>
  <el-container>
    <el-row :gutter="20">
      <el-col :span="18">
        <el-card class="dashboard-content">
          <div class="dashboard-form mb-4">
            <div class="row">
              <div class="col-lg-12 col-md-12 col-xs-12 padding-right-30">
                <el-card class="dashboard-list">
                  <h4 class="gray">我的资料</h4>
                  <div class="dashboard-list-static">
                    <div class="edit-profile-photo">
                      <img :src="user.imgPath" alt="" id="img-preview" />
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
                          <el-form-item label="性别">
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
              </div>
            </div>
          </div>

          <div class="dashboard-form mb-4">
            <el-card class="dashboard-password">
              <h4>修改登录密码</h4>
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="当前密码">
                    <el-input v-model="oldPassword" type="password" placeholder="" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="新密码">
                    <el-input v-model="password1" type="password" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="确认密码">
                    <el-input v-model="password2" type="password" />
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-button type="primary" @click="updatePassword">修改密码</el-button>
                </el-col>
              </el-row>
            </el-card>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-container>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';

const user = ref({
  imgPath: 'assets/images/profile.jpg', // 默认图片路径
  userName: '张三',
  userAccount: 'zhangsan',
  userSex: '男',
  userAge: 30,
  userEmail: 'isxuewei@qq.com',
  userTel: '17879544343',
  createTime: new Date(),
  id: '1'
});

const oldPassword = ref('');
const password1 = ref('');
const password2 = ref('');

const uploadPhoto = (event: Event) => {
  const input = event.target as HTMLInputElement;
  const file = input.files?.[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      user.value.imgPath = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  }
};

const updateProfile = () => {
  // 保存修改逻辑
  console.log('Profile updated:', user.value);
};

const updatePassword = () => {
  // 修改密码逻辑
  if (password1.value === password2.value) {
    console.log('Password updated:', oldPassword.value, password1.value);
  } else {
    console.error('Passwords do not match');
  }
};

const formattedCreateTime = computed(() =>
  user.value.createTime.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    second: 'numeric'
  })
);
</script>

<style scoped>
.dashboard-content {
  padding: 20px;
}

.edit-profile-photo img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
}

.change-photo-btn {
  display: flex;
  align-items: center;
  justify-content: center;
}

.photoUpload input[type="file"] {
  display: none;
}

.my-profile .el-form-item {
  margin-bottom: 15px;
}

.el-button {
  margin-top: 20px;
}
</style>
