<template>
    <el-container>
        <el-header>
            <div class="header-content">
                <img alt="logo" class="logo" />
                <el-menu mode="horizontal">
                    <el-menu-item>疾病百科</el-menu-item>
                    <el-menu-item>查找药品</el-menu-item>
                    <el-menu-item>智能医生</el-menu-item>
                </el-menu>
                <el-dropdown class="user-dropdown">
                    <span class="el-dropdown-link">
                        欢迎你 【 管理员 】
                    </span>
                    <el-dropdown-menu>
                        <el-dropdown-item>个人资料</el-dropdown-item>
                        <el-dropdown-item>退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </el-header>

        <el-container>
            <el-aside width="300px">
                <el-card class="sidebar-card">
                    <div class="user-info">
                        <img alt="author" class="avatar" />
                        <div class="user-details">
                            <h3>管理员</h3>
                            <p>📞 17746678954</p>
                            <p>✉️ 2678788262@qq.com</p>
                        </div>
                    </div>
                </el-card>
                <el-menu default-active="1">
                    <el-menu-item index="1">我的资料</el-menu-item>
                    <el-menu-item index="2">智能医生</el-menu-item>
                    <el-menu-item index="3">疾病管理</el-menu-item>
                    <el-menu-item index="4">药品管理</el-menu-item>
                    <el-menu-item index="5">反馈管理</el-menu-item>
                    <el-menu-item index="6">退出登录</el-menu-item>
                </el-menu>
            </el-aside>

            <el-main>
                <el-card>
                    <h3>我的资料</h3>
                    <el-form label-width="100px">
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="账号名称">
                                    <el-input v-model="user.account" disabled></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="真实姓名">
                                    <el-input v-model="user.realName"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="性别">
                                    <el-input v-model="user.gender" disabled></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="年龄">
                                    <el-input v-model="user.age"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="邮箱">
                                    <el-input v-model="user.email"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="手机号">
                                    <el-input v-model="user.phone"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="12">
                                <el-form-item label="注册时间">
                                    <el-input v-model="user.registerTime" disabled></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-button type="primary" @click="saveChanges">保存修改</el-button>
                    </el-form>
                </el-card>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
export default {
    data() {
        return {
            user: {
                account: "admin",
                realName: "管理员",
                gender: "男",
                age: 23,
                email: "2678788262@qq.com",
                phone: "17746678954",
                registerTime: "2022年05月03日 15:55:41",
            },
        };
    },
    methods: {
        saveChanges() {
            // Save user data logic here
            this.$message.success("资料保存成功！");
        },
    },
};
</script>

<style scoped>
.header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.logo {
    height: 40px;
}

.user-dropdown {
    margin-left: auto;
}

.sidebar-card {
    margin-bottom: 20px;
}

.user-info {
    display: flex;
}

.avatar {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    margin-right: 15px;
}

.user-details {
    display: flex;
    flex-direction: column;
    justify-content: center;
}
</style>