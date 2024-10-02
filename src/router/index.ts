import { createRouter, createWebHistory } from "vue-router";
import UserProfile from "../layout/adminProfile/UserProfile.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "layout",
      redirect: "/home", //跟路由重定向二级路由/home
      component: () => import("../views/home/index.vue"),
      children: [
        {
          path: "/home",
          component: () => import("../views/HomeView.vue"),
          meta: {
            title: "首页", //菜单标题
          },
        },
      ],
    },
    {
      path: "/medicine",
      name: "medicine",
      component: () => import("../views/medicine/index.vue"),
    },
    {
      path: "/medicine/:id",
      name: "medicineDetail",
      component: () => import("../views/medicine/detail/index.vue"),
    },

    {
      path: "/news",
      name: "news",
      component: () => import("@/views/news/index.vue"),
    },
    {
      path: "/news/:id",
      name: "newsDetail",
      component: () => import("@/views/news/detail/index.vue"),
    },
    // {
    //   path: '/user/index',
    //   name: 'userinfo',
    //   component: () => import('@/layout/userProfile/index.vue'),
    // },
    {
      path: "/user/doctor",
      name: "userinfo",
      component: () => import("../layout/userProfile/index.vue"),
    },

    {
      path: "/user/login",
      name: "login",
      component: () => import("@/views/login/index.vue"),
    },
    {
      path: "/user/register",
      name: "register",
      component: () => import("@/views/register/index.vue"),
    },
    {
      path: "/user/index",
      name: "UserProfile",
      component: UserProfile,
    },

    {
      path: "/user/echarts",
      name: "Echarts",
      component: () => import("@/layout/adminProfile/DiseaseStatistics.vue"),
    },
  ],
});

export default router;
