import request from "@/utils/request";
import { da } from "element-plus/es/locales.mjs";

// 定义 API URLs
const API = {
    // http://localhost:8080/healthy/findhealthy
    // healthy_URL: "/healthy/findhealthy",
    healthyDetails_URL: "/healthy/findHealthyOne/",
    healthy_URL: "/healthy/findHealthy",
    // http://localhost:8080/healthy/findhealthyOne/13
    healthyById_URL: "/healthy/findHealthyOne/",
    healthySave_URL: "/healthy/saveHealthy",
    healthyUpdate_URL: "/healthy/updateHealthy",
    healthyDelete_URL: "/healthy/deleteHealthy/",
    // http://localhost:8080/healthy_kind/findList
    healthyKindList_URL: "/healthy_kind/findList",
    deleteImg_URL: "/api/user/deleteUploadByUrl",
};

// 获取健康话题信息
export const reqHealthy = () => request.get(API.healthy_URL);
export const reqHealthyDetails = (healthyId: number) => request.get(API.healthyDetails_URL + healthyId);
//分页查询获取健康话题信息（api为：http://localhost:8080/healthy/findhealthy?pageNow=1&pageSize=4）
export const reqHealthyList = (pageNow: number, pageSize: number) =>
    request.get(API.healthy_URL + "?pageNow=" + pageNow + "&pageSize=" + pageSize);

//根据ID查询健康话题信息
export const reqHealthyById = (Id: number) => request.get(API.healthyById_URL + Id);
// 根据名称查询健康话题信息
export const reqHealthyByName = (name: string) =>
    request.get(API.healthy_URL + "?healthyName=" + name);

export const reqAddOrUpdateHealthy = (data: any) => {
    return request.post<any, any>(API.healthySave_URL, data);
};
// 根据ID删除健康话题信息
export const reqDeleteHealthyById = (Id: number) =>
    request.delete(API.healthyDelete_URL + Id);

// 获取所有健康话题种类信息
export const reqHealthyKindList = () => request.get(API.healthyKindList_URL);

export const reqDeleteImg = (url: string) =>
    request.delete<any, any>(API.deleteImg_URL + "?url=" + url);
