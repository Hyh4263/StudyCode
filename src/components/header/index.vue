<template>
  <div class="header-content">
    <!-- <Logo /> -->

    <!-- Search Bar with Button moved near Logo -->
    <div class="search-container">
      <el-input
        v-model="searchQuery"
        placeholder="Search..."
        class="search-input"
        prefix-icon="Search"
        clearable
        @keyup.enter="handleSearch"
      >
        <!-- Search Button -->
        <template #append>
          <el-button class="search-button" type="primary" @click="handleSearch">
            搜索
          </el-button>
        </template>
      </el-input>
    </div>

    <div class="menu-wrapper">
      <el-menu
        collapse="false"
        class="nav-menu"
        mode="horizontal"
        :router="true"
        ellipsis="false"
      >
        <el-menu-item
          v-for="item in menuItems"
          :key="item.index"
          :index="item.index"
          @click="navigateTo(item.index)"
        >
          {{ item.label }}
        </el-menu-item>
      </el-menu>
    </div>

    <!-- User Login/Logout -->
    <div v-if="!isLoggedIn" class="login-buttons">
      <el-button type="primary" plain @click="goToLogin">登录</el-button>
      <el-button type="primary" @click="goToRegister">注册</el-button>
    </div>
    <loginUser v-if="isLoggedIn" style="margin-left: 100px" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import Logo from "@/components/logo/index.vue";
import loginUser from "./loginUser.vue";
import useUserStore from "@/stores/modules/user";
import { Search } from "@element-plus/icons-vue";

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const isLoggedIn = ref(false);
const searchQuery = ref("");

// Define menu items
const menuItems = ref([
  { index: "/", label: "Home" },
  { index: "/health", label: "Health" },
  { index: "/medicine", label: "Medicine" },
  { index: "/illness", label: "Illness" },
  { index: "/feedback", label: "Feedback" },
]);

// Check if the user is logged in
onMounted(() => {
  isLoggedIn.value = !!localStorage.getItem("TOKEN");
});

// Set initial query based on the current route query parameter
searchQuery.value = route.query.keyword ? route.query.keyword.toString() : "";

// Handle search functionality
const handleSearch = () => {
  const trimmedQuery = searchQuery.value.trim();
  if (trimmedQuery) {
    router.push({ path: "/search", query: { keyword: trimmedQuery } });
  }
};

// Navigation method
const navigateTo = (path: string) => {
  router.push(path);
};

// Methods for login and register buttons
const goToLogin = () => {
  router.push("/user/login");
};

const goToRegister = () => {
  router.push("/user/register");
};
</script>

<style scoped>
.header-content {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 0 20px;
}
.menu-wrapper {
  flex: 0 0 500px; /* 固定菜单宽度 */
  display: flex;
  justify-content: center;
}

.nav-menu {
  flex-grow: 1;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.search-container {
  display: flex;
  align-items: center;
  margin-left: 20%;
}

.search-input {
  width: 400px;
}

.search-button {
  background-color: #00bfa5;
  color: white;
  font-weight: 500;
  transition: background-color 0.3s;
}

.search-button:hover {
  background-color: #009688;
}

.login-buttons {
  display: flex;
  align-items: center;
  /* margin-left: 10px; */
  margin-left: 10%;
}
</style>
