import request from '@/utils/request';

// 定义 API URLs
const API = {
    MEDICINE_URL: '/medicine/findMedicines',
    MEDICINEBYONE_URL: '/medicine/findMedicineOne/',
    medicineList_url: '/medicine/findMedicines/',
    medicineSave_URL: '/medicine/saveMedicine',
    medicineDelete_URL: '/medicine/deleteMedicine/',
    medicineUpload_URL: '/api/user/upload',
    medicineAll_URL: '/medicine/allMedicines'




};

// 获取药品信息
export const reqHasAllMedicines = () => request.get(API.medicineAll_URL);
export const reqHasMedicineByOne = (Id: number) => request.get(API.MEDICINEBYONE_URL + Id);

//分页查询药品信息
export const reqMedicineList = (pageNow: number, pageSize: number) =>
    request.get(API.medicineList_url + "?pageNow=" + pageNow + "&pageSize=" + pageSize);
//添加/修改药品信息
export const reqAddOrUpdateMedicine = (data: any) => {
    return request.post<any, any>(API.medicineSave_URL, data);
};
//根据ID删除药品信息
export const reqDeleteMedicineById = (Id: number) =>
    request.delete(API.medicineDelete_URL + Id);

// 上传药品图片
export const reqUploadMedicineImg = (data: any) => request.post<any, any>(API.medicineUpload_URL, data);

