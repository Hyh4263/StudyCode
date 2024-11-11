<template>
  <div class="common-layout">
    <el-container class="container">
      <Header />
      <el-card>
        <el-main>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card class="box-card">
                <div class="profile-sidebar">
                  <div class="profile-userpic">
                    <img :src="user.imgPath" alt="User Avatar" />
                  </div>
                  <div class="profile-usertitle">
                    <h3 class="mb-1">{{ user.userName }}</h3>
                    <p class="detail"><i class="fa fa-phone"></i> {{ user.userTel }}</p>
                    <p class="detail">
                      <i class="fa fa-envelope-o"></i> {{ user.userEmail }}
                    </p>
                  </div>
                  <div class="profile-userbuttons">
                    <el-menu @select="handleMenuItemClick">
                      <!-- 公共菜单项 -->
                      <el-menu-item index="1">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>我的资料</span>
                      </el-menu-item>
                      <el-menu-item index="2">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>智能医生</span>
                      </el-menu-item>

                      <!-- 管理员及超级管理员菜单项 -->
                      <el-menu-item v-if="isAdmin || isSuperAdmin" index="3">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>疾病管理</span>
                      </el-menu-item>
                      <el-menu-item v-if="isAdmin || isSuperAdmin" index="4">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>药品管理</span>
                      </el-menu-item>
                      <el-menu-item v-if="isAdmin || isSuperAdmin" index="5">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>话题管理</span>
                      </el-menu-item>
                      <el-menu-item v-if="isAdmin || isSuperAdmin" index="6">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>反馈管理</span>
                      </el-menu-item>

                      <!-- 仅超级管理员可见的菜单项 -->
                      <el-menu-item v-if="isSuperAdmin" index="7">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>用户管理</span>
                      </el-menu-item>
                      <el-menu-item v-if="isSuperAdmin" index="8">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>Echart统计</span>
                      </el-menu-item>
                      <!-- 公共菜单项 -->
                      <el-menu-item index="9">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>退出登录</span>
                      </el-menu-item>
                    </el-menu>
                  </div>
                </div>
              </el-card>
            </el-col>

            <el-col style="height: 900px" :span="18">
              <component :is="currentComponent" />
            </el-col>
          </el-row>
        </el-main>
      </el-card>
      <Footer />
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import UserInfoForm from "@/components/UserInfoForm/UserInfoForm.vue";
import Doctor from "@/layout/adminProfile/IntelligentDoctor.vue";
import useUserStore from "@/stores/modules/user";
import { useRouter } from "vue-router";

let userStore = useUserStore();
let $router = useRouter();

const user = ref({
  imgPath: userStore.avatar,
  userName: userStore.userName,
  userTel: userStore.userTel,
  userEmail: userStore.userEmail,
  userAccount: userStore.userAccount,
  userSex: userStore.userSex,
  userAge: userStore.userAge,
  userRole: userStore.roleStatus,
});

// 计算属性判断当前用户角色
const isAdmin = computed(() => userStore.roleStatus === 1);
const isSuperAdmin = computed(() => userStore.roleStatus === 2);

const asyncComponents = {
  Doctor: () =>
    import("@/layout/adminProfile/IntelligentDoctor.vue").then((m) => m.default),
  UserInfoForm: () =>
    import("@/components/UserInfoForm/UserInfoForm.vue").then((m) => m.default),
  DiseaseManagement: () =>
    import("@/layout/adminProfile/DiseaseManagement.vue").then((m) => m.default),
  MedicineManagement: () =>
    import("@/layout/adminProfile/MedicineManagement.vue").then((m) => m.default),
  FeedbackManagement: () =>
    import("@/layout/adminProfile/FeedbackManagement.vue").then((m) => m.default),
  EchartsStatistics: () =>
    import("@/layout/adminProfile/EchartStatistics.vue").then((m) => m.default),
  HealthyManagement: () =>
    import("@/layout/adminProfile/HealthyManagement.vue").then((m) => m.default),
  UserManagement: () =>
    import("@/layout/adminProfile/UserManagement.vue").then((m) => m.default),
};

const currentComponent = ref(UserInfoForm);

// 处理菜单项点击
const handleMenuItemClick = async (index: string) => {
  switch (index) {
    case "1":
      currentComponent.value = UserInfoForm;
      break;
    case "2":
      currentComponent.value = await asyncComponents.Doctor();
      break;
    case "3":
      currentComponent.value = await asyncComponents.DiseaseManagement();
      break;
    case "4":
      currentComponent.value = await asyncComponents.MedicineManagement();
      break;
    case "5":
      currentComponent.value = await asyncComponents.HealthyManagement();
      break;
    case "6":
      currentComponent.value = await asyncComponents.FeedbackManagement();
      break;
    case "7":
      currentComponent.value = await asyncComponents.UserManagement();
      break;
    case "8":
      currentComponent.value = await asyncComponents.EchartsStatistics();
      break;
    case "9":
      handleLogout();
      break;
    default:
      console.warn(`Unknown menu item selected: ${index}`);
  }
};

// 退出登录
const handleLogout = () => {
  userStore.userLogout().then(() => {
    $router.push({ path: "/home" });
    if ($router.currentRoute.value.path === "/home") {
      location.reload();
    }
  });
};
</script>

<style scoped>
.common-layout {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
}

.container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.profile-sidebar {
  text-align: center;
}

.profile-userpic img {
  width: 200px;
  height: 200px;
  border-radius: 50%;
}

.profile-usertitle {
  margin-top: 20px;
}

.detail {
  font-size: 14px;
  color: #666;
}

.profile-userbuttons {
  margin-top: 20px;
}
</style>
