<template>
  <el-card style="height: 100%">
    <div class="disease-management">
      <el-container>
        <el-header>
          <h2>反馈信息</h2>
          <span> 齐人之智，共创新兴。共收集 {{ total }} 条反馈信息 </span>
        </el-header>

        <el-main>
          <el-table :data="feebacks" style="width: 100%">
            <el-table-column prop="id" label="ID" width="50"></el-table-column>
            <el-table-column prop="email" label="邮箱" width="200"></el-table-column>
            <el-table-column prop="name" label="用户名" width="100"></el-table-column>
            <el-table-column prop="title" label="标题" width="100"></el-table-column>
            <el-table-column prop="content" label="内容" width="250"></el-table-column>
            <el-table-column
              prop="createTime"
              label="反馈时间"
              width="150"
            ></el-table-column>

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
            @current-change="getFeedBackList"
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
            :model="currentFeeback"
            ref="feedBackForm"
            :rules="rules"
            label-width="120px"
          >
            <el-form-item label="ID" prop="id">
              <el-input v-model="currentFeeback.id" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="联系方式" prop="contactMethod">
              <el-input v-model="currentFeeback.contactMethod" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="currentFeeback.title" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="内容" prop="content">
              <el-input v-model="currentFeeback.content" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="反馈时间" prop="createTime">
              <el-input v-model="currentFeeback.createTime" disabled />
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
import { View, Edit, Delete } from "@element-plus/icons-vue";
import {
  reqHasfeedBackByOne,
  reqfeedBackList,
  reqAddOrUpdatefeedBack,
  reqDeletefeedBackById,
} from "@/api/feedback/index";
import { ElNotification } from "element-plus";
import type { effect } from "vue";
import useUserStore from "@/stores/modules/user";

const rules = {
  titel: [{ required: true, message: "请输入标题", trigger: "blur" }],
  content: [{ required: true, message: "请输入内容", trigger: "blur" }],
};

const isDialogVisible = ref(false);
const dialogTitle = ref("");
const isReadOnly = ref(false);
const feedBackForm = ref<any>(null);
const userStore = useUserStore();

// Pagination state
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(0);
const feebacks = ref([
  {
    id: "",
    userId: "",
    name: "",
    contactMethod: "",
    status: "",
    contact: "",
    title: "",
    content: "",
    subject: "",
    createTime: "",
  },
]);

const currentFeeback = reactive({
  id: "",
  userId: userStore.id,
  name: userStore.userName,
  contactMethod: "",
  status: "",
  contact: "",
  title: "",
  content: "",
  subject: "",
  createTime: "",
});

onMounted(() => {
  getFeedBackList();
});

// 获取疾病列表
const getFeedBackList = async (pager = 1) => {
  currentPage.value = pager;
  const result: any = await reqfeedBackList(currentPage.value, pageSize.value);
  console.log("result", result);
  if (result.code === 200) {
    total.value = result.data.totalElements;
    // feebacks.value = result.data.medicineList;
    feebacks.value = result.data.feedBacksList.map((item: any) => {
      return {
        id: item.id,
        name: item.name,
        email: item.email,
        title: item.title,
        content: truncatedText(item.content),
        createTime: formatTime(item.createTime),
      };
    });
    console.log("数据", feebacks.value);
  }
};

// 获取单个反馈信息
const getFeedBackById = async (Id: number) => {
  const result: any = await reqHasfeedBackByOne(Id);
  if (result.code === 200) {
    currentFeeback.id = result.data.FeedBack.id;
    currentFeeback.name = result.data.FeedBack.name;
    currentFeeback.contact = result.data.FeedBack.contact;
    currentFeeback.title = result.data.FeedBack.title;
    currentFeeback.content = result.data.FeedBack.content;
    currentFeeback.createTime = formatTime(result.data.FeedBack.createTime);
  }
};

// 截取文本
const truncatedText = (text: string) =>
  text.length > 15 ? text.slice(0, 15) + "..." : text;

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
  getFeedBackList();
};

const handleView = (row: any) => {
  getFeedBackById(row.id);
  dialogTitle.value = "查看反馈";
  isReadOnly.value = true; // Set form to read-only
  isDialogVisible.value = true;
};

const handleEdit = (row: any) => {
  resetForm();
  getFeedBackById(row.id);
  dialogTitle.value = "编辑反馈";
  isReadOnly.value = false; // Allow form editing
  isDialogVisible.value = true;
};

const handleDelete = async (row: any) => {
  try {
    const result: any = await reqDeletefeedBackById(row.id);
    if (result.code === 200) {
      getFeedBackList();
    }
  } catch (error) {
    ElNotification({
      type: "error",
      message: (error as Error).message,
    });
  }
};

const submitForm = async () => {
  try {
    const valid = await feedBackForm.value.validate;
    if (valid) {
      const updatedFeedBack = ref<any>({
        id: currentFeeback.id || null,
        name: currentFeeback.name || null,
        email: currentFeeback.email || null,
        title: currentFeeback.title || null,
        content: currentFeeback.content || null,
        createTime: currentFeeback.createTime || null,
      });
      console.log("updatedMedicine", updatedFeedBack.value);
      const result: any = await reqAddOrUpdatefeedBack(updatedFeedBack.value);
      if (result.code === 200) {
        closeDialog();
        getFeedBackList();
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

const closeDialog = () => {
  resetForm(); // Reset form and validation before closing
  isDialogVisible.value = false;
};

const resetForm = () => {
  Object.assign(currentFeeback, {
    id: "",
    name: "",
    email: "",
    title: "",
    content: "",
    createTime: "",
  });

  if (feedBackForm.value) {
    feedBackForm.value.resetFields(); // Reset form fields and validation
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
</style>
