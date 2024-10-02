// store/user.ts
import { defineStore } from "pinia";
import { reqLogin, reqUserInfo, reqLogout, reqSendEmailCode, reqUpdatedPwd, reqRegister, reqUpdatedUserInfo } from "@/api/user/index";
import { SET_TOKEN, GET_TOKEN, REMOVE_TOKEN } from "@/utils/token";
import { cloneDeep } from "lodash";

function filterAsyncRoute(asnycRoute: any, routers: any) {
    return asnycRoute.filter((item: any) => {
        if (routers.includes(item.name)) {
            if (item.children && item.children.length > 0) {
                item.children = filterAsyncRoute(item.children, routers);
            }
            return true;
        }
    });
}

const useUserStore = defineStore("User", {
    state: () => ({
        token: GET_TOKEN(),
        id: "",
        userAccount: "",
        userName: "",
        userAge: "",
        userSex: "",
        userEmail: "",
        userTel: "",
        roleStatus: "",
        avatar: "",
        buttons: [],
        code: "",
        createTime: "",
    }),

    actions: {
        async userLogin(data: any) {
            let result: any = await reqLogin(data);
            if (result.code == 200) {
                this.token = result.data;
                SET_TOKEN(result.data);
                return "ok";
            } else {
                return Promise.reject(new Error(result.data));
            }
        },

        async userInfo() {
            let result: any = await reqUserInfo(); // 传递token
            if (result.code == 200) {
                this.id = result.data.id;
                this.userAccount = result.data.userAccount;
                this.userName = result.data.userName;
                this.userAge = result.data.userAge;
                this.userSex = result.data.userSex;
                this.userEmail = result.data.userEmail;
                this.userTel = result.data.userTel;
                this.roleStatus = result.data.roleStatus;
                this.avatar = result.data.imgPath;
                this.createTime = result.data.createTime;
                return "ok";
            } else {
                return Promise.reject(new Error(result.message));
            }
        },

        async userLogout() {
            let result: any = await reqLogout();
            if (result.code == 200) {
                this.token = "";
                this.userAccount = "";
                this.userName = "";
                this.userAge = "";
                this.userSex = "";
                this.userEmail = "";
                this.userTel = "";
                this.roleStatus = "";
                this.avatar = "";
                this.id = "";
                REMOVE_TOKEN();
                return "ok";
            } else {
                return Promise.reject(new Error(result.message));
            }
        },

        async userRegister(data: any) {
            let result: any = await reqRegister(data);
            if (result.code == 200) {
                return "ok";
            } else {
                return Promise.reject(new Error(result.message));
            }
        },

        async updateProfile(data: any) {
            let result: any = await reqUpdatedUserInfo(data);
            console.log(result);
            if (result.code == 200) {
                this.userName = result.data.userName;
                this.userAge = result.data.userAge;
                this.userSex = result.data.userSex;
                this.userEmail = result.data.userEmail;
                this.userTel = result.data.userTel;
                this.roleStatus = result.data.roleStatus;
                this.avatar = result.data.imgPath;
                return "ok";
            } else {
                return Promise.reject(new Error(result.message));
            }
        },
        async updatePassword(data: any) {
            let result: any = await reqUpdatedPwd(data);
            console.log(result);
            if (result.code == 200) {
                return "ok";
            } else {
                return Promise.reject(new Error(result.message));
            }
        },
    },
    getters: {},
});

export default useUserStore;
