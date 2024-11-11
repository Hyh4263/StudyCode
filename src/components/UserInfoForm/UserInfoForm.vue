<template>
  <div style="height: 100%">
    <el-card class="dashboard-list">
      <h4 class="gray">我的资料</h4>
      <div class="dashboard-list-static">
        <div class="edit-profile-photo">
          <el-image
            style="width: 150px; height: 150px; border-radius: 50%"
            :src="previewImgPath || user.imgPath"
            alt="头像预览"
            id="img-preview"
          />
          <div class="change-photo-btn">
            <div class="photoUpload">
              <span><i class="fa fa-upload"></i> 选择更换头像</span>
              <input type="file" class="upload" @change="handleFileChange" />
            </div>
            <el-button type="primary" @click="confirmUploadPhoto" class="mt-2"
              >更换头像</el-button
            >
          </div>
        </div>

        <div class="my-profile">
          <el-row :gutter="20">
            <el-col :span="12" :lg="12" :md="24" :sm="24">
              <el-form-item label="账号">
                <el-input v-model="user.userAccount" disabled />
              </el-form-item>
              <el-input v-model="user.id" type="text" style="display: none" />
            </el-col>
            <el-col :span="12" :lg="12" :md="24" :sm="24">
              <el-form-item label="名称">
                <el-input v-model="user.userName" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12" :lg="12" :md="24" :sm="24">
              <el-form-item label="性别">
                <el-input v-model="user.userSex" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12" :lg="12" :md="24" :sm="24">
              <el-form-item label="年龄">
                <el-input v-model.number="user.userAge" type="number" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12" :lg="12" :md="24" :sm="24">
              <el-form-item label="邮箱">
                <el-input v-model="user.userEmail" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12" :lg="12" :md="24" :sm="24">
              <el-form-item label="手机号">
                <el-input v-model.number="user.userTel" type="number" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="注册时间">
            <el-input :value="user.createTime" disabled />
          </el-form-item>

          <el-button type="primary" @click="updateProfile" class="full-width mt-2"
            >保存修改</el-button
          >
        </div>

        <div class="dashboard-form">
          <h4>修改登录密码</h4>
          <el-row :gutter="20">
            <el-col :span="8" :lg="8" :md="24" :sm="24">
              <el-form-item label="当前密码">
                <el-input type="password" v-model="updatePwdUser.userPwd" />
              </el-form-item>
            </el-col>
            <el-col :span="8" :lg="8" :md="24" :sm="24">
              <el-form-item label="新密码">
                <el-input type="password" v-model="updatePwdUser.userNewPwd" />
              </el-form-item>
            </el-col>
            <el-col :span="8" :lg="8" :md="24" :sm="24">
              <el-form-item label="确认密码">
                <el-input type="password" v-model="updatePwdUser.surePassword" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-button type="primary" @click="updatePwd" class="full-width mt-2"
                >修改密码</el-button
              >
            </el-col>
          </el-row>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from "vue";
import useUserStore from "@/stores/modules/user";
import { useRouter } from "vue-router";
import { ElNotification } from "element-plus";

const $router = useRouter();
const userStore = useUserStore();

const user = computed(() => ({
  id: userStore.id,
  imgPath: userStore.avatar,
  userName: userStore.userName,
  userTel: userStore.userTel,
  userEmail: userStore.userEmail,
  userAccount: userStore.userAccount,
  userSex: userStore.userSex,
  userAge: userStore.userAge,
  userRole: userStore.roleStatus,
  createTime: userStore.createTime,
}));

console.log("返回的用户信息:", user);

// 获取更新密码用户信息
const updatePwdUser = reactive({
  id: userStore.id,
  userAccount: userStore.userAccount,
  userPwd: "",
  userNewPwd: "",
  surePassword: "",
});

const previewImgPath = ref(""); // Preview path for selected image

const handleFileChange = (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0];
  if (file) {
    previewImgPath.value = URL.createObjectURL(file);
  }
};

const confirmUploadPhoto = async () => {
  const fileInput = document.querySelector(".upload") as HTMLInputElement;
  const file = fileInput?.files?.[0];
  if (!file) {
    ElNotification({
      type: "warning",
      message: "请先选择图片",
    });
    return;
  }

  const formData = new FormData();
  formData.append("image", file);
  formData.append("userId", user.value.id);

  try {
    const response = await userStore.changeAvatar(formData);
    user.value.imgPath = response.data.imgPath;
    ElNotification({
      type: "success",
      message: "头像更换成功",
    });
    $router.push({ path: "/user/login" });
  } catch (error) {
    ElNotification({
      type: "error",
      message: "头像上传失败",
    });
  }
};

const updateProfile = async () => {
  try {
    await userStore.updateProfile(user.value); // 等待更新操作完成
    ElNotification({
      type: "success",
      message: "修改成功",
    });
    $router.push({ path: "/user/login" });
  } catch (error) {
    ElNotification({
      type: "error",
      message: (error as Error).message,
    });
  }
};

const updatePwd = async () => {
  if (updatePwdUser.userNewPwd === updatePwdUser.surePassword) {
    try {
      await userStore.updatePassword(updatePwdUser);
      ElNotification({
        type: "success",
        message: "修改成功",
      });
      $router.push({ path: "/user/login" });
    } catch (error) {
      ElNotification({
        type: "error",
        message: (error as Error).message,
      });
    }
  } else {
    ElNotification({
      type: "warning",
      message: "两次输入的密码不一致",
    });
  }
};
</script>

<style lang="scss" scoped>
$primary-color: #409eff;
$secondary-color: #66b1ff;
$border-radius: 12px;
$margin-bottom: 20px;

.dashboard-list {
  padding: 30px;
  height: 100%; // Set a fixed height for a consistent layout
  background-color: white;
  border-radius: $border-radius;

  .gray {
    color: #666;
    font-size: 1.2rem;
    font-weight: bold;
    text-align: center;
    margin-bottom: 20px;
  }

  .dashboard-list-static {
    display: flex;
    flex-direction: column;
    gap: 20px;
    height: 100%;
    overflow-y: auto; // Enables scrolling within the card if content exceeds height
  }

  .edit-profile-photo,
  .my-profile,
  .dashboard-form {
    width: 100%;
    padding: 20px;
    background-color: white;
    border-radius: $border-radius;
    box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.05);
    flex-shrink: 0; // Prevents shrinking when container height is limited
  }

  .edit-profile-photo {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .el-button {
    width: 100%;
    background-color: $primary-color;
    color: #fff;
    font-weight: 600;
    border-radius: 8px;
    transition: background-color 0.3s ease;

    &:hover {
      background-color: $secondary-color;
    }
  }

  .el-form-item {
    margin-bottom: 15px;
  }

  .el-input {
    width: 100%;
  }

  .el-row {
    margin-bottom: $margin-bottom;
  }

  .full-width {
    width: 100%;
  }

  .mt-2 {
    margin-top: 10px;
  }

  @media (max-width: 768px) {
    .el-col {
      margin-bottom: 15px;
    }
  }
}
</style>
