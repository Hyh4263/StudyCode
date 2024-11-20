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
        <el-dropdown-item @click="handleUserInfo"> 个人资料 </el-dropdown-item>
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
const handleUserInfo = () => {
  // 跳转到个人中心页面
  $router.push({ path: "/user/index" });
};
</script>
<script lang="ts">
export default {
  name: "loginUser",
};
</script>
<style scoped>
.el-dropdown-link {
  width: 100px;
  cursor: pointer;
  /* color: var(--el-color-primary); */
  display: flex;
  align-items: center;
  font-weight: 500;
}

.el-icon--right {
  margin-left: 5px; /* 调整图标与用户名的间距 */
  font-size: 14px; /* 根据需要调整图标大小 */
}

.el-dropdown-menu {
  min-width: 150px; /* 设置下拉菜单宽度 */
}

.el-dropdown-item {
  padding: 10px 20px; /* 设置菜单项的内边距 */
  font-size: 14px; /* 适当的字体大小 */
}
</style>
