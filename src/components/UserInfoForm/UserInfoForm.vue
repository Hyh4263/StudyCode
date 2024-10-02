<template>
  <div>
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
              <el-form-item label="账号">
                <el-input v-model="user.userAccount" disabled />
              </el-form-item>
              <el-input v-model="user.id" type="text" style="display: none" />
            </el-col>
            <el-col :span="12">
              <el-form-item label="名称">
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
            <el-input :value="user.createTime" disabled />
          </el-form-item>

          <el-button type="primary" @click="updateProfile">保存修改</el-button>
        </div>
      </div>
      <div class="dashboard-form">
        <h4>修改登录密码</h4>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="当前密码">
              <el-input type="password" v-model="updatePwdUser.userPwd" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="新密码">
              <el-input type="password" v-model="updatePwdUser.userNewPwd" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="确认密码">
              <el-input type="password" v-model="updatePwdUser.surePassword" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-button type="primary" @click="updatePwd" class="mt-2">修改密码</el-button>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";
// 导入仓库
import useUserStore from "@/stores/modules/user";
import { useRouter } from "vue-router";
import { ElNotification } from "element-plus";

let $router = useRouter();
//获取存储用户信息的仓库对象
let userStore = useUserStore();
const user = ref({
  id: userStore.id,
  imgPath: userStore.avatar, // 默认图片路径
  userName: userStore.userName,
  userTel: userStore.userTel,
  userEmail: userStore.userEmail,
  userAccount: userStore.userAccount,
  userSex: userStore.userSex,
  userAge: userStore.userAge,
  userRole: userStore.roleStatus,
  createTime: userStore.createTime,
});

// const formattedCreateTime = ref(
//   `${user.value.createTime.getFullYear()}年${
//     user.value.createTime.getMonth() + 1
//   }月${user.value.createTime.getDate()}日`
// );

//获取更新密码用户信息
const updatePwdUser = reactive({
  id: userStore.id,
  userAccount: userStore.userAccount,
  userPwd: "",
  userNewPwd: "",
  surePassword: "",
});

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

// const updateProfile = () => {
//    userStore.updateProfile(user.value);
//    // $router.push({ path: "/login" });

// };
const updateProfile = async () => {
  try {
    await userStore.updateProfile(user.value); // 等待更新操作完成
    ElNotification({
      type: "success",
      message: "修改成功",
    });
    $router.push({ path: "/user/login" }); // 更新成功后跳转到登录页面
  } catch (error) {
    // 处理错误情况，例如显示提示信息给用户
    ElNotification({
      type: "error",
      message: (error as Error).message,
    });
  }
};

const updatePwd = async () => {
  if (updatePwdUser.userNewPwd === updatePwdUser.surePassword) {
    try {
      // 传递完整的 updatePwdUser 对象
      await userStore.updatePassword(updatePwdUser);
      ElNotification({
        type: "success",
        message: "修改成功",
      });
      $router.push({ path: "/user/login" });
    } catch (error) {
      // 处理错误情况，例如显示提示信息给用户
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

const sendMessage = () => {
  if (newMessage.value.trim() !== "") {
    // console.log('Message sent:', newMessage.value);
    newMessage.value = "";
  }
};
</script>
<script lang="ts">
export default {
  name: "Footer",
};
</script>

<style lang="scss" scoped></style>
