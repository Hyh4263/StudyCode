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
                    <!-- <el-image
                      style="width: 150px; height: 150px"
                      :src="user.imgPath"
                      alt="相关图片"
                    /> -->
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
                      <el-menu-item index="3" disabled>
                        <el-icon>
                          <document />
                        </el-icon>
                        <span>Navigator Three</span>
                      </el-menu-item>
                      <el-menu-item index="7">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>大屏</span>
                      </el-menu-item>
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
                      <el-menu-item index="3">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>疾病管理</span>
                      </el-menu-item>
                      <el-menu-item index="4">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>药品管理</span>
                      </el-menu-item>
                      <el-menu-item index="5">
                        <el-icon>
                          <setting />
                        </el-icon>
                        <span>反馈管理</span>
                      </el-menu-item>
                      <el-menu-item index="6">
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

            <el-col :span="18">
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
import { ref, onMounted } from "vue";
import Header from "@/components/header/index.vue";
import Footer from "@/components/footer/index.vue";
import UserInfoForm from "@/components/UserInfoForm/UserInfoForm.vue";
import Doctor from "@/layout/adminProfile/IntelligentDoctor.vue";
import useUserStore from "@/stores/modules/user";
import { useRouter } from "vue-router";

//获取存储用户信息的仓库对象
let userStore = useUserStore();
let $router = useRouter();

const relativePath = userStore.avatar; // 后端返回的相对路径
const avatarUrl = ref("");

const user = ref({
  imgPath: userStore.avatar, // 默认图片路径
  userName: userStore.userName,
  userTel: userStore.userTel,
  userEmail: userStore.userEmail,
  userAccount: userStore.userAccount,
  userSex: userStore.userSex,
  userAge: userStore.userAge,
  userRole: userStore.roleStatus,
});

// 异步导入其他组件
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
  DiseaseStatistics: () =>
    import("@/layout/adminProfile/EchartStatistics.vue").then((m) => m.default),
};

// const loadAvatar = async () => {
//   try {
//     // 使用 import.meta.glob 导入所有头像
//     const avatars = import.meta.glob("@/assets/images/userImg/*.png");
//     const avatarPath = Object.keys(avatars).find((path) => path.includes(relativePath));

//     if (avatarPath) {
//       // 使用 new URL 构建绝对路径
//       const fullPath = new URL(avatarPath, import.meta.url).href;
//       avatarUrl.value = fullPath;
//     } else {
//       // 使用 new URL 构建默认头像的绝对路径
//       const defaultPath = new URL("@/assets/images/userImg/img_99png", import.meta.url)
//         .href;
//       avatarUrl.value = defaultPath;
//     }
//   } catch (error) {
//     console.error("加载头像时发生错误:", error);
//     // 设置默认头像路径
//     const defaultPath = new URL("@/assets/images/userImg/img_99.png", import.meta.url)
//       .href;
//     avatarUrl.value = defaultPath;
//   }
// };

// onMounted(() => {
//   loadAvatar();
// });

// Default component to display
const currentComponent = ref(UserInfoForm);

// Handles the selection of menu items
// 处理菜单项点击
const handleMenuItemClick = async (index: string) => {
  switch (index) {
    case "1":
      // 设置为 UserInfoForm，这里可以直接使用 import 引入的组件
      currentComponent.value = UserInfoForm;
      break;
    case "2":
      // 异步加载并设置 Doctor 组件
      currentComponent.value = await asyncComponents.Doctor();
      break;
    case "3":
      // 异步加载并设置 DiseaseManagement 组件
      currentComponent.value = await asyncComponents.DiseaseManagement();
      break;
    case "4":
      // 异步加载并设置 MedicineManagement 组件
      currentComponent.value = await asyncComponents.MedicineManagement();
      break;
    case "5":
      // 异步加载并设置 FeedbackManagement 组件  '
      currentComponent.value = await asyncComponents.FeedbackManagement();
      break;
    case "7":
      // 异步加载并设置 DiseaseStatistics 组件
      currentComponent.value = await asyncComponents.DiseaseStatistics();
      break;
    case "6":
      handleLogout();
      break;
    default:
      console.warn(`Unknown menu item selected: ${index}`);
  }
};

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

.profile-usertitle-name {
  font-size: 16px;
  font-weight: 600;
  color: #5a7391;
}

.profile-usertitle-job {
  font-size: 12px;
  color: #5b9bd1;
}

.profile-edit-btn,
.profile-logout-btn {
  margin-top: 10px;
}

.dashboard-list-static {
  padding: 20px;
}

.edit-profile-photo img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
}

.change-photo-btn {
  margin-top: 10px;
}

.my-profile {
  margin-top: 20px;
}

.dashboard-content {
  margin-top: 20px;
}

.message {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 300px;
}

.message-header {
  display: flex;
  align-items: center;
}

.inbox-image img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.inbox-content {
  margin-left: 10px;
}

.msg-received {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
}

.msg-received .msg-image img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}

.msg-received .msg-content {
  margin-left: 10px;
}

.msg-reply {
  margin-top: 10px;
}
</style>
