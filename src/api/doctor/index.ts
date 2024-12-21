import request from '@/utils/request';

// 定义 API URLs
const API = {

    doctorMessage_URL: '/message/query',
    findDocters_URL: '/message/findOnlineUser',



};

export const reqDoctorMessage = (data: any) => {
    return request.post<any, any>(API.doctorMessage_URL, data);
}
export const fetchRealDoctors = () => {
    return request.get(API.findDocters_URL);
}
