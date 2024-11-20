<template>
    <div class="logo">
        <img src="@/assets/images/logo.png" alt="Logo">
    </div>
</template>

<script setup lang="ts">
// 引入设置标题与logo配置文件
// import setting from '@/setting.ts'
</script>
<script lang="ts">
export default {
    name: 'Logo'
}
</script>
<style scoped lang="scss">
.logo {
    flex-shrink: 0;
    margin-right: auto;

    img {
        width: 105px;
        height: 42px;
    }
}
</style>