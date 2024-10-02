<template>
    <el-menu :default-active="activeIndex" class="el-menu-vertical-demo" @select="handleSelect">
        <div class="avatar-container">
            <img src="path-to-avatar" class="avatar" />
            <div class="info">
                <p class="name">管理员</p>
                <p class="contact">17746678954</p>
                <p class="contact">2678788262@qq.com</p>
            </div>
        </div>
        <el-menu-item index="1">
            <i class="el-icon-user"></i>
            <span slot="title">我的资料</span>
        </el-menu-item>
        <el-menu-item index="2">
            <i class="el-icon-chat-line-round"></i>
            <span slot="title">智能医生</span>
        </el-menu-item>
        <el-menu-item index="3">
            <i class="el-icon-s-data"></i>
            <span slot="title">疾病管理</span>
        </el-menu-item>
        <el-menu-item index="4">
            <i class="el-icon-s-goods"></i>
            <span slot="title">药品管理</span>
        </el-menu-item>
        <el-menu-item index="5">
            <i class="el-icon-s-comment"></i>
            <span slot="title">反馈管理</span>
        </el-menu-item>
        <el-menu-item index="6">
            <i class="el-icon-s-unfold"></i>
            <span slot="title">退出登录</span>
        </el-menu-item>
    </el-menu>
</template>

<script>
export default {
    data() {
        return {
            activeIndex: "1",
        };
    },
    methods: {
        handleSelect(key, keyPath) {
            this.$router.push({ path: keyPath[0] });
        },
    },
};
</script>

<style scoped>
.avatar-container {
    text-align: center;
    padding: 20px;
}

.avatar {
    width: 60px;
    height: 60px;
    border-radius: 50%;
}

.info {
    margin-top: 10px;
}

.name {
    font-size: 16px;
    font-weight: bold;
}

.contact {
    font-size: 14px;
    color: #999;
}
</style>
