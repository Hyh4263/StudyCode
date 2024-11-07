<template>
  <div>
    <router-view></router-view>
  </div>
</template>

<script setup></script>

<script lang="ts">
export default {
  name: "App",
  mounted() {
    // 添加事件监听器
    window.addEventListener("beforeunload", this.clearLocalStorage);
  },
  beforeDestroy() {
    // 移除事件监听器，避免内存泄漏
    window.removeEventListener("beforeunload", this.clearLocalStorage);
  },
  methods: {
    clearLocalStorage(event) {
      localStorage.clear();
      // 可以选择阻止默认行为，显示确认对话框
      // event.preventDefault();
      // 或者设置返回值
      // event.returnValue = '';
    },
  },
};
</script>

<style>
html,
body,
#app {
  height: 100%;
  width: 100%;
}
</style>
