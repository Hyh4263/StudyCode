<template>
  <el-dropdown>
    <span class="el-dropdown-link">
      {{ userStore.userName }}
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item>
          <router-link to="/user/index">个人资料</router-link>
        </el-dropdown-item>
        <el-dropdown-item @click="handleLogout">
          <!-- <router-link to="/user/logout">退出登录</router-link> -->
          退出登录
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script lang="ts" setup>
import { ArrowDown } from "@element-plus/icons-vue";
//引入用户相关的仓库,获取当前用户的头像、昵称
import useUserStore from "@/stores/modules/user";
import { useRouter } from "vue-router";
let $router = useRouter();
//获取存储用户信息的仓库对象
let userStore = useUserStore();

// 定义退出登录方法
const handleLogout = () => {
  userStore.userLogout().then(() => {
    // 跳转到登录页面
    $router.push({ path: "/home" });
    // 如果是在home页面，刷新页面，路由会自动跳转到home页面，此时需要跳转到登录页面
    if ($router.currentRoute.value.path === "/home") {
      // $router.push({ path: "/login" });
      location.reload();
    }
  });
};
</script>
<script lang="ts">
export default {
  name: "loginUser",
};
</script>
<style scoped>
.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>
