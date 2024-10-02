<template>
  <el-card style="height: 100%">
    <div class="disease-management">
      <el-container>
        <el-header>
          <h2>全部疾病</h2>
          <span>
            共收集 {{ total }} 条疾病信息。你可以继续
            <el-link type="primary" @click="openDialog('新增疾病')">新增疾病</el-link>
          </span>
        </el-header>

        <el-main>
          <el-table :data="diseases" style="width: 100%">
            <el-table-column label="分类" width="150">
              <template #default="{ row }">
                <div v-for="(kindName, index) in splitText(row.kindName)" :key="index">
                  <span>{{ kindName }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="名称" width="200px">
              <template #default="{ row }">
                <div v-for="(name, index) in splitText(row.illness_name)" :key="index">
                  <span>{{ name }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="疾病症状">
              <template #default="{ row }">
                <div
                  v-for="(symptom, index) in splitText(row.illness_symptom)"
                  :key="index"
                >
                  <span>{{ truncatedText(symptom) }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="更新时间" width="200">
              <template #default="{ row }">
                <span>{{ formatTime(row.update_time) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-link @click="viewDisease(row)" type="primary"
                  ><el-icon><View /></el-icon
                ></el-link>
                <el-link @click="editDisease(row)" type="primary"
                  ><el-icon><Edit /></el-icon
                ></el-link>
                <el-link @click="deleteDisease(row)" type="primary"
                  ><el-icon><Delete /></el-icon
                ></el-link>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            @current-change="getIllnesses"
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
            :model="currentDisease"
            :rules="rules"
            ref="diseaseForm"
            label-width="120px"
          >
            <el-form-item label="分类" prop="kind_id">
              <el-select
                v-model="currentDisease.kind_id"
                :disabled="isReadOnly"
                placeholder="选择分类"
              >
                <el-option
                  v-for="category in diseaseCategories"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="名称" prop="illness_name">
              <el-input v-model="currentDisease.illness_name" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="特殊症状" prop="illness_symptom">
              <el-input
                type="textarea"
                v-model="currentDisease.illness_symptom"
                :disabled="isReadOnly"
              />
            </el-form-item>
            <el-form-item label="病因" prop="include_reason">
              <el-input
                type="textarea"
                v-model="currentDisease.include_reason"
                :disabled="isReadOnly"
              />
            </el-form-item>
            <el-form-item label="更新时间" prop="update_time">
              <el-input v-model="currentDisease.update_time" disabled />
            </el-form-item>
          </el-form>
          <template v-slot:footer>
            <el-button @click="closeDialog">取消</el-button>
            <el-button type="primary" @click="submitForm">提交</el-button>
          </template>
        </el-dialog>
      </el-container>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { View, Edit, Delete } from "@element-plus/icons-vue";
import {
  reqIllnessList,
  reqIllnessByName,
  reqAddOrUpdateIllness,
  reqDeleteIllnessById,
  reqIllnessKindList,
} from "@/api/news/index";
import { ElNotification } from "element-plus";

const rules = {
  kind_id: [{ required: true, message: "请选择分类", trigger: "blur" }],
  illness_name: [{ required: true, message: "请输入疾病名称", trigger: "blur" }],
  illness_symptom: [{ required: true, message: "请输入疾病症状", trigger: "blur" }],
  include_reason: [{ required: true, message: "请输入病因", trigger: "blur" }],
};

// 初始化状态
const diseases = ref<any>([]);
const currentDisease = ref<any>({});
const isDialogVisible = ref(false);
const dialogTitle = ref("");
const isReadOnly = ref(false);
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref<number>(0);
const diseaseCategories = ref<any>([]);
const diseaseForm = ref<any>(null);

onMounted(() => {
  getIllnesses();
  loadDiseaseCategories();
});

// 获取疾病分类
const loadDiseaseCategories = async () => {
  try {
    const result: any = await reqIllnessKindList();
    if (result.code === 200) {
      diseaseCategories.value = result.data;
    }
  } catch (error) {
    console.error("Failed to load disease categories:", error);
  }
};

// 获取疾病列表
const getIllnesses = async (pager = 1) => {
  currentPage.value = pager;
  const result: any = await reqIllnessList(currentPage.value, pageSize.value);
  if (result.code === 200) {
    total.value = result.data.totalElements;
    diseases.value = result.data.illness;
  }
};

// 分页器
const sizeChange = (newSize: number) => {
  pageSize.value = newSize;
  currentPage.value = 1;
  getIllnesses();
};

// 公用函数
const truncatedText = (text: string) =>
  text.length > 10 ? text.slice(0, 10) + "..." : text;

const splitText = (text: string) => text.split(";").map((s) => s.trim());

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

const resetForm = () => {
  currentDisease.value = {};
  if (diseaseForm.value) {
    diseaseForm.value.resetFields(); // Reset form fields and validation
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

// 操作函数
const viewDisease = async (row: any) => {
  dialogTitle.value = "查看疾病详情";
  isReadOnly.value = true;
  isDialogVisible.value = true;
  const result: any = await reqIllnessByName(row.illness_name);
  if (result.code === 200) {
    currentDisease.value = result.data.illness[0];
  }
};

const editDisease = async (row: any) => {
  dialogTitle.value = "编辑疾病";
  isReadOnly.value = false;
  isDialogVisible.value = true;
  const result: any = await reqIllnessByName(row.illness_name);
  if (result.code === 200) {
    currentDisease.value = result.data.illness[0];
  }
};

// const submitForm = () => {
//   diseaseForm.value.validate(async (valid: boolean) => {
//     if (valid) {
//       const result: any = await reqAddOrUpdateIllness(currentDisease.value);
//       if (result.code === 200) {
//         closeDialog();
//         getIllnesses();
//       } else {
//         console.error("Failed to save disease:", result.message);
//       }
//     } else {
//       console.log("Form validation failed");
//     }
//   });
// };
const submitForm = async () => {
  try {
    const valid = await diseaseForm.value.validate;
    if (valid) {
      const updatedDisease = ref<any>({
        id: currentDisease.value.id || null,
        kindId: currentDisease.value.kind_id || null,
        illnessName: currentDisease.value.illness_name || null,
        includeReason: currentDisease.value.include_reason || null,
        illnessSymptom: currentDisease.value.illness_symptom || null,
        specialSymptom: currentDisease.value.special_symptom || null,
      });
      console.log("Form validation passed", updatedDisease.value);
      const result: any = await reqAddOrUpdateIllness(updatedDisease.value);
      if (result.code === 200) {
        closeDialog();
        getIllnesses();
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
      message: "失败",
    });
  }
};

const deleteDisease = async (row: any) => {
  try {
    const result: any = await reqDeleteIllnessById(row.id);
    if (result.code === 200) {
      getIllnesses();
    }
  } catch (error) {
    ElNotification({
      type: "error",
      message: (error as Error).message,
    });
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
  .el-table {
    .el-table-column {
      width: 100%;
      height: 60px;
    }
  }
}
</style>
