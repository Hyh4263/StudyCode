import request from '@/utils/request';

// 定义 API URLs
const API = {

    feedBackById_URL: '/feedback/findFeedBackOne/',
    feedBackList_URL: '/feedback/findFeedBacks/',
    feedBackSave_URL: '/feedback/saveFeedBack',
    feedBackDelete_URL: '/feedback/deleteFeedBack/',

};

export const reqHasfeedBackByOne = (Id: number) => request.get(API.feedBackById_URL + Id);

//分页查询药品信息
export const reqfeedBackList = (pageNow: number, pageSize: number) =>
    request.get(API.feedBackList_URL + "?pageNow=" + pageNow + "&pageSize=" + pageSize);
//添加/修改药品信息
export const reqAddOrUpdatefeedBack = (data: any) => {
    return request.post<any, any>(API.feedBackSave_URL, data);
};
//根据ID删除药品信息
export const reqDeletefeedBackById = (Id: number) =>
    request.delete(API.feedBackDelete_URL + Id);