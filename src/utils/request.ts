import axios from "axios";
import { ElMessage } from "element-plus";
import { GET_TOKEN } from '@/utils/token';


//创建axios实例
let request = axios.create({
    baseURL: 'http://localhost:5173/api', // Use the proxy URL
    timeout: 50000
});

//请求拦截器
// request.interceptors.request.use(config => {
//     return config;
// });

// // 请求拦截器
// axios.interceptors.request.use(
//     (config) => {
//         const token = GET_TOKEN();
//         if (token) {
//             config.headers['Authorization'] = `Bearer ${token}`;  // 在请求头中加入token
//         }
//         return config;
//     },
//     (error) => {
//         return Promise.reject(error);
//     }
// );

// 使用你自定义的 axios 实例（request），并确保拦截器正确作用于该实例
request.interceptors.request.use(
    (config) => {
        const token = GET_TOKEN();
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;  // 在请求头中加入token
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);


//响应拦截器
request.interceptors.response.use((response) => {
    return response.data;
}, (error) => {
    //处理网络错误
    let msg = '';
    let status = error.response.status;
    switch (status) {
        case 401:
            msg = "token过期";
            break;
        case 403:
            msg = '无权访问';
            break;
        case 404:
            msg = "请求地址错误";
            break;
        case 500:
            msg = "服务器出现问题";
            break;
        default:
            msg = "无网络";
    }
    ElMessage({
        type: 'error',
        message: msg
    });
    return Promise.reject(error);
});

export default request;
