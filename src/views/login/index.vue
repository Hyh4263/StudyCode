<template>
  <div class="common-layout">
    <el-container class="app_wrapper">
      <el-main class="login_wrapper">
        <el-form class="login_form" :model="loginForm" :rules="rules" ref="loginForms">
          <h1>Hello</h1>
          <h2>欢迎来到药品甄选</h2>
          <el-form-item prop="userAccount">
            <el-input :prefix-icon="User" v-model="loginForm.userAccount"></el-input>
          </el-form-item>
          <el-form-item prop="userPwd">
            <el-input
              type="password"
              :prefix-icon="Lock"
              v-model="loginForm.userPwd"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              :loading="loading"
              type="primary"
              size="default"
              class="login_btn"
              @click="login"
              >登录</el-button
            >
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="default" class="login_btn" @click="register"
              >注册</el-button
            >
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { User } from "@element-plus/icons-vue";
import { Lock } from "@element-plus/icons-vue";
import { reactive, ref } from "vue";
import { ElNotification } from "element-plus";
// 引入用户相关的小仓库
import useUserStore from "@/stores/modules/user";
//引入路由器和路由
import { useRouter, useRoute } from "vue-router";
// 引入获取当前时间的函数
import { getTime } from "@/utils/time";
// 定义变量控制按钮加载效果
let loading = ref(false);
//获取路由器对象
let $router = useRouter();
// 获取路由对象
let $route = useRoute();
let useStore = useUserStore();
//收集账号与密码的数据
let loginForm = reactive({ userAccount: "admin", userPwd: "admin123" });
// 获取el-form组件
let loginForms = ref();

// 自定义校验规则函数
const validatorUserName = (_rule: any, value: any, callback: any) => {
  // rule:校验对象
  // value：表单元素文本内容
  // 函数：如果符合条件callback方行通过，
  // 如果不符合条件callback方法，注入错误信息
  if (value.length >= 3) {
    callback();
  } else {
    callback(new Error("账号长度至少三位"));
  }
};
const validatorpassword = (_rule: any, value: any, callback: any) => {
  // rule:校验对象
  // value：表单元素文本内容
  // 函数：如果符合条件callback方行通过，
  // 如果不符合条件callback方法，注入错误信息
  if (value.length >= 5) {
    callback();
  } else {
    callback(new Error("密码长度至少五位"));
  }
};

// 注册按钮
const register = () => {
  $router.push({ path: "/user/register" });
};

// 登录按钮回调
const login = async () => {
  // 保证全部表单校验通过才发送请求
  await loginForms.value.validate();
  // 加载效果：开始加载
  loading.value = true;

  try {
    await useStore.userLogin(loginForm);

    await useStore.userInfo();

    let redirect: any = $route.query.redirect;
    $router.push({ path: redirect || "/" });

    ElNotification({
      type: "success",
      message: "欢迎回来",
      title: `HI,${getTime()}好`,
    });
  } catch (error) {
    loading.value = false;

    ElNotification({
      type: "error",
      message: (error as Error).message,
    });
  } finally {
    loading.value = false;
  }
};

// 定义表单校验需要的配置对象
const rules = {
  //规则对象属性：
  // required：代表这个字段务必要校验
  // min：文本长度至少多少位
  // max：文本长度至多多少位
  // message：错误的提示信息
  // trigger：触发校验表单的时间，change—>文本发生变化时触发校验，blur->失去焦点的时候触发校验
  userAccount: [
    // { required: true, message: '用户名不能为空', trigger: 'blur' },
    // { required: true, min: 6, max: 10, message: '账号长度至少六位', trigger: 'change' }
    { trigger: "change", validator: validatorUserName },
  ],
  userPwd: [
    // { required: true, min: 6, max: 10, message: '密码长度至少六位', trigger: 'change' }
    { trigger: "change", validator: validatorpassword },
  ],
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

.login_wrapper {
  width: 100%;
  max-width: 400px;
  padding: 40px;
  background: url("@/assets/images/login_form.png") no-repeat center center;
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

  .login_btn {
    width: 100%;
  }
}
</style>
