<template>
  <el-card style="height: 100%">
    <div class="disease-management">
      <el-container>
        <el-header>
          <h2>全部药品</h2>
          <span>
            共收集 {{ total }} 条药品信息。你可以继续
            <el-link type="primary" @click="openDialog('新增药品')">新增药品</el-link>
          </span>
        </el-header>

        <el-main>
          <el-table :data="medicines" style="width: 100%">
            <el-table-column prop="name" label="名称" width="150"></el-table-column>
            <el-table-column prop="band" label="品牌" width="150"></el-table-column>
            <el-table-column
              label="分类"
              width="150"
              :formatter="formatTypeLabel"
            ></el-table-column>
            <el-table-column prop="price" label="价格" width="150"></el-table-column>
            <el-table-column prop="updatedTime" label="更新时间" width="150">
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-link @click="handleView(scope.row)" type="primary">
                  <el-icon><View /></el-icon>
                </el-link>
                <el-link @click="handleEdit(scope.row)" type="primary">
                  <el-icon><Edit /></el-icon>
                </el-link>
                <el-link @click="handleDelete(scope.row)" type="primary">
                  <el-icon><Delete /></el-icon>
                </el-link>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            @current-change="getMedicineList"
            @size-change="sizeChange"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[3, 5, 7, 9]"
            :background="true"
            layout="prev, pager, next, jumper, ->, sizes, total"
            :total="total"
          />
        </el-main>

        <!-- Dialog for Viewing/Editing/Adding Diseases -->
        <el-dialog v-model="isDialogVisible" :title="dialogTitle">
          <el-form
            :model="currentMedicine"
            ref="medicineForm"
            :rules="rules"
            label-width="120px"
          >
            <el-form-item label="图片" prop="img">
              <div class="edit-profile-photo">
                <el-image
                  style="width: 150px; height: 150px; border-radius: 50%"
                  :src="currentMedicine.img"
                  alt="图片预览"
                  id="img-preview"
                />
                <div class="change-photo-btn">
                  <div class="photoUpload">
                    <span><i class="fa fa-upload"></i> 上传图片</span>
                    <input type="file" class="upload" @change="handleFileChange" />
                  </div>
                </div>
              </div>
            </el-form-item>

            <el-form-item label="名称" prop="name">
              <el-input v-model="currentMedicine.name" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="品牌" prop="band">
              <el-input v-model="currentMedicine.band" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="分类" prop="type">
              <el-select v-model="currentMedicine.type" :disabled="isReadOnly">
                <el-option label="西药" value="0"></el-option>
                <el-option label="中药" value="1"></el-option>
                <el-option label="中成药" value="2"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="功效" prop="effect">
              <el-input
                type="textarea"
                :rows="3"
                v-model="currentMedicine.effect"
                :disabled="isReadOnly"
              />
            </el-form-item>
            <el-form-item label="禁忌" prop="taboo">
              <el-input
                type="textarea"
                v-model="currentMedicine.taboo"
                :disabled="isReadOnly"
                :rows="3"
              />
            </el-form-item>
            <el-form-item label="使用方法" prop="use">
              <el-input
                type="textarea"
                v-model="currentMedicine.use"
                :disabled="isReadOnly"
                :rows="3"
              />
            </el-form-item>

            <el-form-item label="药品的相互作用" prop="interaction">
              <el-input
                type="textarea"
                v-model="currentMedicine.interaction"
                :disabled="isReadOnly"
                :rows="3"
              />
            </el-form-item>

            <el-form-item label="价格" prop="price">
              <el-input v-model="currentMedicine.price" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="更新时间" prop="updatedTime">
              <el-input v-model="currentMedicine.updatedTime" disabled />
            </el-form-item>
          </el-form>
          <div v-if="!isReadOnly" slot="footer">
            <el-button @click="closeDialog">取消</el-button>
            <el-button type="primary" @click="submitForm">提交</el-button>
          </div>
        </el-dialog>
      </el-container>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, reactive } from "vue";
import {
  View,
  Edit,
  Delete,
  UploadFilled,
  Download,
  Plus,
  ZoomIn,
} from "@element-plus/icons-vue";
import {
  reqMedicineList,
  reqHasMedicineByOne,
  reqAddOrUpdateMedicine,
  reqDeleteMedicineById,
  reqDeleteImg,
} from "@/api/medicine/index";
import { ElNotification, ElMessage } from "element-plus";
import type { UploadFile } from "element-plus";
import useUserStore from "@/stores/modules/user";

const rules = {
  name: [{ required: true, message: "请输入药品名称", trigger: "blur" }],
  band: [{ required: true, message: "请输入品牌", trigger: "blur" }],
  type: [{ required: true, message: "请输入分类", trigger: "blur" }],
  price: [{ required: true, message: "请输入价格", trigger: "blur" }],
};

const isDialogVisible = ref(false);
const dialogTitle = ref("");
const isReadOnly = ref(false);
const medicineForm = ref<any>(null);
const userStore = useUserStore();

const user = computed(() => ({
  id: userStore.id,
}));

// 将数字类型转换为中文显示
const formatTypeLabel = (row: any) => {
  const typeMap = {
    "0": "西药",
    "1": "中药",
    "2": "中成药",
  };
  return typeMap[row.type] || "";
};

// Pagination state
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(0);
const medicines = ref([
  {
    createTime: "",
    id: "",
    img: "",
    keyword: "",
    band: "",
    effect: "",
    name: "",
    price: "",
    type: "",
    taboo: "",
    updatedTime: "",
    use: "",
  },
]);

const currentMedicine = reactive({
  createTime: "",
  id: "",
  img: "",
  keyword: "",
  band: "",
  effect: "",
  name: "",
  price: "",
  type: "",
  taboo: "",
  updatedTime: "",
  use: "",
  interaction: "",
  file: null as File | null, // 修改这里，允许存储 File 对象或 null
});

onMounted(() => {
  getMedicineList();
});

const dialogImageUrl = ref("");
const dialogVisible = ref(false);
const disabled = ref(false);

const handleFileChange = (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0];
  if (file) {
    currentMedicine.img = URL.createObjectURL(file); // 本地预览
    currentMedicine.file = file; // 保存文件对象用于上传
  }
};

const submitForm = async () => {
  try {
    const valid = await medicineForm.value.validate();
    if (valid) {
      let imagePath = currentMedicine.img;

      // 如果有选择的图片文件，则上传图片
      if (currentMedicine.file) {
        const formData = new FormData();
        formData.append("image", currentMedicine.file);
        formData.append("userId", user.value.id);

        try {
          const response = await userStore.upload(formData); // 假设上传图片方法位于 userStore
          imagePath = response.data;
          console.log("Image date:", response);
          console.log("Image uploaded successfully:", imagePath);
        } catch (error) {
          ElNotification({
            type: "error",
            message: "图片上传失败",
          });
          return;
        }
      }

      // 构建药品数据对象
      const updatedMedicine = {
        id: currentMedicine.id || null,
        medicineName: currentMedicine.name || null,
        keyword: currentMedicine.keyword || null,
        medicineBrand: currentMedicine.band || null,
        interaction: currentMedicine.interaction || null,
        taboo: currentMedicine.taboo || null,
        useAge: currentMedicine.use || null,
        medicineType: currentMedicine.type || null,
        imgPath: imagePath, // 使用图片上传后的路径
        medicinePrice: currentMedicine.price || null,
        medicineEffect: currentMedicine.effect || null,
      };

      const result: any = await reqAddOrUpdateMedicine(updatedMedicine);
      if (result.code === 200) {
        closeDialog();
        getMedicineList();
        ElNotification({
          type: "success",
          message: "操作成功",
        });
      } else {
        ElNotification({
          type: "error",
          message: "操作失败",
        });
      }
    } else {
      ElNotification({
        type: "error",
        message: "验证失败",
      });
    }
  } catch (error) {
    ElNotification({
      type: "error",
      message: "提交失败",
    });
  }
};

const handleRemove = (file: UploadFile) => {
  console.log(file);
};

const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url!;
  dialogVisible.value = true;
};

const handleDownload = (file: UploadFile) => {
  console.log(file);
};

// 获取药品列表
const getMedicineList = async (pager = 1) => {
  currentPage.value = pager;
  const result: any = await reqMedicineList(currentPage.value, pageSize.value);
  if (result.code === 200) {
    total.value = result.data.totalElements;
    medicines.value = result.data.medicineList.map((item: any) => {
      return {
        createTime: item.create_time,
        id: item.id,
        img: item.img_path,
        keyword: item.keyword,
        band: item.medicine_brand,
        effect: item.medicine_effect,
        name: item.medicine_name,
        price: item.medicine_price,
        type: item.medicine_type,
        taboo: item.taboo,
        use: item.us_age,
        updatedTime: formatTime(item.update_time),
      };
    });
    console.log("数据", medicines.value);
  }
};

// 获取单个药品信息
const getMedicineById = async (Id: number) => {
  const result: any = await reqHasMedicineByOne(Id);
  console.log("当前药品", result);
  if (result.code === 200) {
    currentMedicine.id = result.data.medicine.id;
    currentMedicine.name = result.data.medicine.medicineName;
    currentMedicine.keyword = result.data.medicine.keyword;
    currentMedicine.band = result.data.medicine.medicineBrand;
    currentMedicine.interaction = result.data.medicine.interaction;
    currentMedicine.taboo = result.data.medicine.taboo;
    currentMedicine.use = result.data.medicine.usAge;
    currentMedicine.type = result.data.medicine.medicineType;
    currentMedicine.img = result.data.medicine.imgPath;
    currentMedicine.price = result.data.medicine.medicinePrice;
    currentMedicine.effect = result.data.medicine.medicineEffect;
    currentMedicine.createTime = formatTime(result.data.medicine.createTime);
    currentMedicine.updatedTime = formatTime(result.data.medicine.updateTime);
  }
  console.log("当前药品", currentMedicine);
};

// 时间格式化
const formatTime = (timeString: string): string => {
  const date = new Date(timeString);
  return `${date.getFullYear()}-${(date.getMonth() + 1)
    .toString()
    .padStart(2, "0")}-${date
    .getDate()
    .toString()
    .padStart(2, "0")} ${date
    .getHours()
    .toString()
    .padStart(2, "0")}:${date
    .getMinutes()
    .toString()
    .padStart(2, "0")}:${date.getSeconds().toString().padStart(2, "0")}`;
};

// 分页器
const sizeChange = (newSize: number) => {
  pageSize.value = newSize;
  currentPage.value = 1;
  getMedicineList();
};

const handleView = (row: any) => {
  getMedicineById(row.id);
  dialogTitle.value = "查看药品";
  isReadOnly.value = true; // Set form to read-only
  isDialogVisible.value = true;
};

const handleEdit = (row: any) => {
  resetForm();
  getMedicineById(row.id);
  dialogTitle.value = "编辑药品";
  isReadOnly.value = false; // Allow form editing
  isDialogVisible.value = true;
};

const handleDelete = async (row: any) => {
  try {
    const result: any = await reqDeleteMedicineById(row.id);
    if (result.code === 200) {
      getMedicineList();
    }
  } catch (error) {
    ElNotification({
      type: "error",
      message: (error as Error).message,
    });
  }
};

const openDialog = (title: string) => {
  resetForm();
  dialogTitle.value = title;
  isReadOnly.value = title === "查看疾病";
  isDialogVisible.value = true;
};

const closeDialog = () => {
  resetForm(); // Reset form and validation before closing
  isDialogVisible.value = false;
};

const resetForm = () => {
  Object.assign(currentMedicine, {
    createTime: "",
    id: "",
    img: "",
    keyword: "",
    band: "",
    effect: "",
    name: "",
    price: "",
    type: "",
    taboo: "",
    updatedTime: "",
    use: "",
    interaction: "",
    file: null, // 重置时也设置为 null
  });

  if (medicineForm.value) {
    medicineForm.value.resetFields(); // Reset form fields and validation
  }
};
</script>
<style scoped>
.disease-management {
  padding: 20px;
}

.el-header {
  padding: 0 20px;
  background-color: #f5f7fa;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-header h2 {
  margin: 0;
}

.el-header span {
  color: #606266;
}

.el-main {
  padding: 20px;
}
.upload-demo {
  width: 148px;
  height: 148px;
  border: 1px dashed #d9d9d9;
  border-radius: 4px;
  overflow: hidden;
}

.uploaded-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
