import request from "@/utils/request";
import { da } from "element-plus/es/locales.mjs";

// 定义 API URLs
const API = {
    // http://localhost:8080/illness/findIllness
    NEWS_URL: "/illness/findIllness",
    IllnessDetails_URL: "/illness/findIllnessOne/",
    Illness_URL: "/illness/findIllness",
    // http://localhost:8080/illness/findIllnessOne/13
    ILLNESSBYID_URL: "/illness/findIllnessOne/",
    illnessSave_URL: "/illness/saveIllness",
    illnessUpdate_URL: "/illness/updateIllness",
    illnessDelete_URL: "/illness/deleteIllness/",
    // http://localhost:8080/illness_kind/findList
    illnessKindList_URL: "/illness_kind/findList",

};

// 获取药品信息
export const reqHasNewsList = () => request.get(API.NEWS_URL);
export const reqIllnessDetails = (Id: number) => request.get(API.IllnessDetails_URL + Id);
//分页查询获取疾病信息（api为：http://localhost:8080/illness/findIllness?pageNow=1&pageSize=4）
export const reqIllnessList = (pageNow: number, pageSize: number) =>
    request.get(API.Illness_URL + "?pageNow=" + pageNow + "&pageSize=" + pageSize);

//根据ID查询疾病信息
export const reqIllnessById = (Id: number) => request.get(API.ILLNESSBYID_URL + Id);
// 根据名称查询疾病信息
export const reqIllnessByName = (name: string) =>
    request.get(API.Illness_URL + "?illnessName=" + name);

export const reqAddOrUpdateIllness = (data: any) => {
    return request.post<any, any>(API.illnessSave_URL, data);
};
// 根据ID删除疾病信息
export const reqDeleteIllnessById = (Id: number) =>
    request.delete(API.illnessDelete_URL + Id);

// 获取所有疾病种类信息
export const reqIllnessKindList = () => request.get(API.illnessKindList_URL);