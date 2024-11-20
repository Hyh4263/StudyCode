// import router from "@/router";
// import nprogress from "nprogress";
// import "nprogress/nprogress.css";
// import useUserStore from "./stores/modules/user";
// import pinia from "./stores/index";

// nprogress.configure({ showSpinner: false });

// let userStore = useUserStore(pinia);

// router.beforeEach(async (to, _from, next) => {
//   nprogress.start();

//   let token = userStore.token;
//   let username = userStore.username;

//   if (token) {
//     console.log("token存在123");
//     if (to.path === "/login") {
//       console.log("登录成功，不能访问/login");
//       next({ path: "/" });
//     } else {
//       if (username) {
//         console.log("已有用户信息");
//         next();
//       } else {
//         console.log("没有用户信息，尝试获取");
//         try {
//           await userStore.userInfo();
//           console.log("成功获取用户信息");
//           next({ ...to, replace: true });
//         } catch (error) {
//           console.error("获取用户信息失败:", error);
//           await userStore.userLogout();
//           next({ path: "/login", query: { redirect: to.path } });
//         }
//       }
//     }
//   } else {
//     if (to.path === "/login") {
//       next();
//     } else {
//       next({ path: "/login", query: { redirect: to.path } });
//     }
//   }
// });

// router.afterEach((to) => {
//   nprogress.done();
//   document.title = `${import.meta.env.VITE_APP_TITLE} - ${to.meta.title}`;
// });
