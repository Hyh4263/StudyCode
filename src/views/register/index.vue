<template>
  <div class="common-layout">
    <el-container class="app_wrapper">
      <el-main class="register_wrapper">
        <el-form
          class="register_form"
          :model="registerForm"
          :rules="rules"
          ref="registerForms"
        >
          <h1>注册</h1>
          <h2>欢迎加入药品甄选</h2>
          <el-form-item prop="userAccount">
            <el-input
              :prefix-icon="User"
              v-model="registerForm.userAccount"
              placeholder="账号"
            ></el-input>
          </el-form-item>
          <el-form-item prop="username">
            <el-input
              :prefix-icon="User"
              v-model="registerForm.userName"
              placeholder="用户名"
            ></el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input
              v-model="registerForm.userEmail"
              placeholder="邮箱"
              @blur="validateEmail"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button :disabled="isGettingCode" @click="getEmailCode">
              {{ isGettingCode ? `重新获取验证码 (${countdown})` : "获取邮箱验证码" }}
            </el-button>
          </el-form-item>
          <el-form-item prop="code">
            <el-input v-model="registerForm.code" placeholder="验证码"></el-input>
          </el-form-item>
          <el-form-item prop="realAge">
            <el-input v-model="registerForm.userAge" placeholder="真实年龄"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              type="password"
              :prefix-icon="Lock"
              v-model="registerForm.userPwd"
              show-password
              placeholder="密码"
            ></el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input
              type="password"
              :prefix-icon="Lock"
              v-model="registerForm.confirmPassword"
              show-password
              placeholder="确认密码"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              :loading="loading"
              type="primary"
              size="default"
              class="register_btn"
              @click="register"
              >注册</el-button
            >
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { User, Lock } from "@element-plus/icons-vue";
import { reactive, ref } from "vue";
import { ElNotification } from "element-plus";
import { useRouter } from "vue-router";
import useUserStore from "@/stores/modules/user";
import { reqSendEmailCode, reqRegister } from "@/api/user/index";

let loading = ref(false);
let isGettingCode = ref(false);
let countdown = ref(60);
let $router = useRouter();
let useStore = useUserStore();

let registerForm = reactive({
  // userAccount: '',
  // userName: '',
  // email: '',
  // code: '',
  // userAge: '',
  // userPwd: '',
  confirmPassword: "",
  userAccount: "",
  userName: "",
  userPwd: "",
  userAge: "",
  code: "",
  userSex: "",
  userEmail: "",
});

let registerForms = ref();

const startCountdown = () => {
  isGettingCode.value = true;
  countdown.value = 60;
  let interval = setInterval(() => {
    countdown.value -= 1;
    if (countdown.value <= 0) {
      clearInterval(interval);
      isGettingCode.value = false;
    }
  }, 1000);
};

const validateEmail = async () => {
  await registerForms.value.validateField("email");
};

const getEmailCode = async () => {
  if (!registerForm.userEmail) {
    ElNotification({
      type: "error",
      message: "请输入邮箱后获取验证码",
    });
    return;
  }

  try {
    // Call your API to send the email code
    let res = await reqSendEmailCode(registerForm.userEmail);
    // console.log(res);
    if (res.code === 200) {
      ElNotification({
        type: "success",
        message: "验证码已发送至您的邮箱",
      });
      startCountdown();
    } else {
      ElNotification({
        type: "error",
        message: "获取验证码失败，请重试",
      });
    }
  } catch (error) {
    ElNotification({
      type: "error",
      message: "获取验证码失败，请重试",
    });
  }
};

const validatorUserName = (_rule: any, value: any, callback: any) => {
  if (value.length >= 5) {
    callback();
  } else {
    callback(new Error("账号长度至少五位"));
  }
};

const validatorEmail = (_rule: any, value: any, callback: any) => {
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (emailPattern.test(value)) {
    callback();
  } else {
    callback(new Error("请输入有效的邮箱地址"));
  }
};

const validatorPassword = (_rule: any, value: any, callback: any) => {
  if (value.length >= 6) {
    callback();
  } else {
    callback(new Error("密码长度至少六位"));
  }
};

const validatorConfirmPassword = (_rule: any, value: any, callback: any) => {
  if (value === registerForm.userPwd) {
    callback();
  } else {
    callback(new Error("两次输入的密码不一致"));
  }
};

const register = async () => {
  await registerForms.value.validate();
  loading.value = true;
  let res = await reqRegister(registerForm);
  if (res.code == 200 && res.data != null) {
    ElNotification({
      type: "success",
      message: "注册成功，欢迎加入",
      title: "注册成功",
    });

    $router.push({ path: "/user/login" });
    loading.value = false;
  } else {
    ElNotification({
      type: "error",
      message: res.msg,
    });
    loading.value = false;
  }
};

const rules = {
  userAccount: [{ trigger: "change", validator: validatorUserName }],
  userName: [{ trigger: "change", validator: validatorUserName }],
  userEmail: [{ trigger: "change", validator: validatorEmail }],
  userPwd: [{ trigger: "change", validator: validatorPassword }],
  confirmPassword: [{ trigger: "change", validator: validatorConfirmPassword }],
  code: [{ required: true, message: "验证码不能为空", trigger: "change" }],
  userAge: [{ required: true, message: "请输入真实年龄", trigger: "change" }],
};
</script>

<style lang="scss">
.common-layout {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
}

.app_wrapper {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url("@/assets/images/background.jpg") no-repeat center center fixed;
  background-size: cover;
}

.register_wrapper {
  width: 100%;
  max-width: 400px;
  padding: 40px;
  background: url("@/assets/images/register_form.png") no-repeat center center;
  background-size: cover;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 300px;

  h1,
  h2 {
    color: white;
  }

  h1 {
    font-size: 40px;
  }

  h2 {
    font-size: 20px;
    margin: 20px 0px;
  }

  .register_btn {
    width: 100%;
  }
}
</style>
