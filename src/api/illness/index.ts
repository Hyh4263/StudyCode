import request from "@/utils/request";
import { da } from "element-plus/es/locales.mjs";

// 定义 API URLs
const API = {
  // http://localhost:8080/illness/findIllness
  // illness_URL: "/illness/findIllness",
  illnessDetails_URL: "/illness/findIllnessOne/",
  illness_URL: "/illness/findIllness",
  // http://localhost:8080/illness/findIllnessOne/13
  illnessById_URL: "/illness/findIllnessOne/",
  illnessSave_URL: "/illness/saveIllness",
  illnessUpdate_URL: "/illness/updateIllness",
  illnessDelete_URL: "/illness/deleteIllness/",
  // http://localhost:8080/illness_kind/findList
  illnessKindList_URL: "/illness_kind/findList",
  deleteImg_URL: "/api/user/deleteUploadByUrl"

};

// 获取疾病信息
export const reqIllness = () => request.get(API.illness_URL);
export const reqIllnessDetails = (Id: number) => request.get(API.illnessDetails_URL + Id);
//分页查询获取疾病信息（api为：http://localhost:8080/illness/findIllness?pageNow=1&pageSize=4）
export const reqIllnessList = (pageNow: number, pageSize: number) =>
  request.get(API.illness_URL + "?pageNow=" + pageNow + "&pageSize=" + pageSize);

//根据ID查询疾病信息
export const reqIllnessById = (Id: number) => request.get(API.illnessById_URL + Id);
// 根据名称查询疾病信息
export const reqIllnessByName = (name: string) =>
  request.get(API.illness_URL + "?illnessName=" + name);

export const reqAddOrUpdateIllness = (data: any) => {
  return request.post<any, any>(API.illnessSave_URL, data);
};
// 根据ID删除疾病信息
export const reqDeleteIllnessById = (Id: number) =>
  request.delete(API.illnessDelete_URL + Id);

// 获取所有疾病种类信息
export const reqIllnessKindList = () => request.get(API.illnessKindList_URL);

export const reqDeleteImg = (url: string) =>
  request.delete<any, any>(API.deleteImg_URL + "?url=" + url);
