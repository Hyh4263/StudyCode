// store/user.ts
import { defineStore } from "pinia";
import {
    reqLogin,
    reqUserInfo,
    reqLogout,
    reqSendEmailCode,
    reqChangeAvatar,
    reqUpdatedPwd,
    reqRegister,
    reqUpdatedUserInfo,
    reqUploadAvatar,
    reqUploadVideo,
    reqDeleteVideo,
    reqDeleteImg,
} from "@/api/user/index";
import { SET_TOKEN, GET_TOKEN, REMOVE_TOKEN, SET_USER, GET_USER, REMOVE_USER } from "@/utils/token";
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
        userInfo: GET_USER(),
        id: "",
        userAccount: "",
        status: "",
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
                SET_USER(result.data);
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
                this.status = result.data.status;
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
                REMOVE_USER();
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
            if (result.code == 200) {
                // 更新用户信息
                this.userName = result.data.userName;
                this.avatar = `/images/${result.data.imgPath}`; // 使用相对路径
                return "ok";
            } else {
                return Promise.reject(new Error(result.message));
            }
        },
        async changeAvatar(formData: FormData) {
            try {
                const response = await reqChangeAvatar(formData);
                this.avatar = `/images/${response.data.imgPath}`; // 更新返回的图片路径
                return response;
            } catch (error) {
                throw new Error("头像上传失败");
            }
        },

        async upload(formData: FormData) {
            try {
                const response = await reqUploadAvatar(formData);
                return response;
            } catch (error) {
                throw new Error("图片上传失败");
            }
        },

        async uploadVideo(formData: FormData) {
            try {
                const response = await reqUploadVideo(formData);
                return response;
            } catch (error) {
                throw new Error("视频上传失败");
            }
        },

        async deleteImg(url: string) {
            try {
                const response = await reqDeleteImg(url);
                return response;
            } catch (error) {
                throw new Error("图片删除失败");
            }
        },
        async deleteVideo(url: string) {
            try {
                const response = await reqDeleteVideo(url);
                return response;
            } catch (error) {
                throw new Error("视频删除失败");
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
    getters: {
        getUser: (state) => ({
            id: state.id,
            userName: state.userName,
            userAccount: state.userAccount,
            userAge: state.userAge,
            userSex: state.userSex,
            userEmail: state.userEmail,
            avatar: state.avatar,
            createTime: state.createTime,
        }),
    },
});

export default useUserStore;
