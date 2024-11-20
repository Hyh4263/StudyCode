import type { RouteRecordRaw } from "vue-router";
import type { CategoryObj } from "@/api/product/attr/type";
// 定义用户小仓库state对象的ts类型
export interface UserState {
    token: string | null;
    menuRoutes: RouteRecordRaw[]
    username: string,
    avatar: string,
    buttons: any

}
// 定义分类小仓库state对象的ts类型
export interface CategoryState {
    c1Id: number | string,
    c2Id: number | string,
    c3Id: number | string,
    c1Arr: CategoryObj[]
    c2Arr: CategoryObj[]
    c3Arr: CategoryObj[]
}

// 定义spu品牌分类小仓库state对象的ts类型
export interface SpuState {
    c3Id: number | string,
    c3Arr: CategoryObj[]
}