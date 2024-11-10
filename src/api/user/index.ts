// 真实接口
//统一管理咱们项目用户相关的接口
import request from "@/utils/request";
// import { loginFormData, loginResponseData, userInfoResponseData } from './type';
// 项目用户相关的请求地址
enum API {
    // LOGIN_URL = "/admin/acl/index/login",
    // LOGIN_URL = "/admin/acl/index/login",
    // USERINFO_URL = "/admin/acl/index/info",
    // LOGOUT_URL = "/admin/acl/index/logout"
    LOGIN_URL = "/login/login",
    LOGOUT_URL = "/login/logout",
    USERINFO_URL = "/user/profile",
    EMAILCODE_URL = "login/sendEmailCode",
    REGISTER_URL = "/login/register",
    UPDATED_URL = "/user/saveProfile",
    UPDATEDPWD_URL = "/user//savePassword",
    GET_USER_LIST = "/user/findUserList",
    getUserById_URL = "/user/findUserById/",
    DELETE_USER_BY_ID = "/user/deleteUserById/",
    ADD_OR_UPDATE_USER = "/user/saveUser",
    UPDATE_USER = "/user/updateUser",
    GET_ROLE_LIST = "/role/list",
    GET_ROLE_LIST_BY_ID = "/role/getRoleById",

    changeAvatar = "/api/user/changeAvatar",
    uploadAvatar = "/api/user/upload",
    uploadVideo = "/api/user/uploadVideo",
    deleteImg_URL = "/api/user/deleteUploadByUrl",
    deleteVideo_URL = "/api/user/deleteUploadVideoByUrl",
}
// 登录接口
export const reqLogin = (data: any) => request.post<any, any>(API.LOGIN_URL, data);
// 获取用户信息接口
export const reqUserInfo = () => request.get<any, any>(API.USERINFO_URL);
// 退出登录接口
export const reqLogout = () => request.post<any, any>(API.LOGOUT_URL);
// 注册接口
export const reqRegister = (data: any) => request.post<any, any>(API.REGISTER_URL, data);
// export const reqRegister = (data: any) => request.post<any, any>(API.REGISTER_URL, data);
// 发送邮箱验证码
export const reqSendEmailCode = (email: string) =>
    request.get<any, any>(API.EMAILCODE_URL, { params: { email } });
// 更新用户信息
export const reqUpdatedUserInfo = (data: any) =>
    request.post<any, any>(API.UPDATED_URL, data);
// 更新密码
export const reqUpdatedPwd = (data: any) =>
    request.post<any, any>(API.UPDATEDPWD_URL, data);
// 获取用户列表
export const reqGetUserList = (pageNow: number, pageSize: number) =>
    request.get(API.GET_USER_LIST + "?pageNow=" + pageNow + "&pageSize=" + pageSize);
// 更换用户头像
export const reqChangeAvatar = (data: any) =>
    request.post<any, any>(API.changeAvatar, data);
export const reqUploadAvatar = (data: any) =>
    request.post<any, any>(API.uploadAvatar, data);

export const reqUploadVideo = (data: any) =>
    request.post<any, any>(API.uploadVideo, data);

export const reqDeleteImg = (url: string) =>
    request.delete<any, any>(API.deleteImg_URL + "?url=" + url);

export const reqDeleteVideo = (url: string) =>
    request.delete<any, any>(API.deleteVideo_URL + "?url=" + url);

// 根据用户ID获取用户信息
export const reqGetUserById = (Id: number) => request.get(API.getUserById_URL + Id);
// 根据用户ID删除用户信息
export const reqDeleteUserById = (Id: number) =>
    request.delete(API.DELETE_USER_BY_ID + Id);
// 保存用户
export const reqAddOrUpdateUser = (data: any) =>
    request.post<any, any>(API.ADD_OR_UPDATE_USER, data);
