import request from '@/utils/request';

// 定义 API URLs
const API = {

    doctor_URL: '/message/query',


};

export const reqDoctorMessage = (data: any) => {
    return request.post<any, any>(API.doctor_URL, data);
}
