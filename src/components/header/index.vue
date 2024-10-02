<template>
  <div class="header-content">
    <Logo></Logo>
    <el-menu
      collapse="false"
      class="nav-menu"
      mode="horizontal"
      :router="true"
      ellipsis="false"
    >
      <el-menu-item index="search" class="search-icon">
        <el-icon>
          <Search />
        </el-icon>
      </el-menu-item>
      <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.index">
        <router-link :to="item.index">{{ item.label }}</router-link>
      </el-menu-item>
    </el-menu>

    <div v-if="!isLoggedIn">
      <router-link to="/user/login">
        <el-button type="primary" plain>登录</el-button>
      </router-link>
      <router-link to="/user/register">
        <el-button type="primary">注册</el-button>
      </router-link>
    </div>
    <loginUser v-if="isLoggedIn" style="margin-left: 100px"></loginUser>
  </div>
</template>

<script setup lang="ts">
import { Search } from "@element-plus/icons-vue";
import Logo from "@/components/logo/index.vue";
import loginUser from "./loginUser.vue";
import { ref, onMounted } from "vue";
import useUserStore from "@/stores/modules/user";

const userStore = useUserStore();
const isLoggedIn = ref(false);

const menuItems = ref([
  { index: "/", label: "Home" },
  { index: "/health", label: "Health" },
  { index: "/medicine", label: "Medicine" },
  { index: "/news", label: "News" },
  { index: "/client", label: "Client" },
]);

onMounted(() => {
  // 初始化时检查用户数据
  if (localStorage.getItem("TOKEN")) {
    isLoggedIn.value = true;
  }
});
</script>

<script lang="ts">
export default {
  name: "Header",
};
</script>

<style scoped>
.header-content {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 0 20px;
}

.nav-menu {
  flex-grow: 1;
  display: flex;
  justify-content: flex-end;
  align-items: center;

  .el-menu-item {
    padding: 0 10px;

    .router-link-active {
      color: #00bfa5;
    }
  }

  .search-icon {
    margin-left: "30px";
  }
}
</style>
