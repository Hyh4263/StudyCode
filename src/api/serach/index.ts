import request from "@/utils/request";
import { da } from "element-plus/es/locales.mjs";

// 定义 API URLs
const API = {
    // http://localhost:8080/search?keyword=感冒
    SERACH_URL: "/search",

};


export const reqSearch = (keyword: string) =>
    request.get(API.SERACH_URL + "?keyword=" + keyword);

