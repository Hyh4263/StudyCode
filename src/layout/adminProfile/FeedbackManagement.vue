<template>
  <el-empty v-if="loading" description="正在加载..." class="empty-state" />

  <el-card class="feedback-management-card" v-else>
    <div class="feedback-management">
      <el-container>
        <el-header>
          <h2>反馈信息管理</h2>
          <span> 共收集 {{ total }} 条反馈信息 </span>
        </el-header>

        <el-main>
          <el-table :data="feedbacks" stripe style="width: 100%">
            <el-table-column prop="id" label="ID" width="50"></el-table-column>
            <el-table-column prop="name" label="用户名" width="100"></el-table-column>
            <el-table-column
              prop="contactMethod"
              label="联系方式"
              width="100"
              :formatter="formatContactMethod"
            ></el-table-column>
            <el-table-column
              prop="contact"
              label="联系信息"
              width="150"
            ></el-table-column>
            <el-table-column prop="title" label="标题" width="150"></el-table-column>
            <el-table-column
              prop="content"
              label="内容"
              width="250"
              :show-overflow-tooltip="true"
            ></el-table-column>
            <el-table-column
              prop="createTime"
              label="反馈时间"
              width="180"
            ></el-table-column>
            <el-table-column
              prop="status"
              label="状态"
              width="100"
              :formatter="formatStatus"
            >
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'warning' : 'success'" effect="dark">
                  {{ formatStatus(row) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="类型" width="100" :formatter="formatType">
              <template #default="{ row }">
                <el-tag :type="row.type === 1 ? 'info' : 'danger'" effect="dark">
                  {{ formatType(row) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" align="center">
              <template #default="scope">
                <el-tooltip content="查看" placement="top">
                  <el-link
                    @click="handleView(scope.row)"
                    type="primary"
                    icon="View"
                  ></el-link>
                </el-tooltip>
                <el-tooltip content="编辑" placement="top">
                  <el-link
                    @click="handleEdit(scope.row)"
                    type="primary"
                    icon="Edit"
                  ></el-link>
                </el-tooltip>
                <el-tooltip content="删除" placement="top">
                  <el-link
                    @click="handleDelete(scope.row)"
                    type="danger"
                    icon="Delete"
                  ></el-link>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            @current-change="getFeedbackList"
            @size-change="sizeChange"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[5, 10, 15, 20]"
            :background="true"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          />
        </el-main>

        <!-- Dialog for Viewing/Editing Feedback -->
        <el-dialog v-model="isDialogVisible" :title="dialogTitle" width="800px">
          <el-form
            :model="currentFeedback"
            ref="feedbackForm"
            :rules="rules"
            label-width="90px"
          >
            <el-form-item label="用户名" prop="name">
              <el-input v-model="currentFeedback.name" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="联系方式" prop="contactMethod">
              <el-input :value="contactMethodFormatted" :disabled="true" />
            </el-form-item>
            <el-form-item label="联系信息" prop="contact">
              <el-input v-model="currentFeedback.contact" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <el-input v-model="currentFeedback.title" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="内容" prop="content">
              <el-input
                type="textarea"
                v-model="currentFeedback.content"
                :disabled="isReadOnly"
                rows="3"
              />
            </el-form-item>

            <el-form-item label="状态" prop="status">
              <template v-if="isReadOnly">
                <el-input :value="formatStatus(currentFeedback.status)" disabled />
              </template>
              <el-select v-else v-model="currentFeedback.status" placeholder="请选择状态">
                <el-option label="未处理" :value="1"></el-option>
                <el-option label="已处理" :value="2"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="类型" prop="type">
              <el-input :value="typeFormatted" :disabled="true" />
            </el-form-item>
            <el-form-item label="反馈时间" prop="createTime">
              <el-input v-model="currentFeedback.createTime" disabled />
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="closeDialog">取消</el-button>
            <el-button type="primary" @click="submitForm" v-if="!isReadOnly"
              >提交</el-button
            >
          </template>
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

const rules = {
  title: [{ required: true, message: "请输入标题", trigger: "blur" }],
  content: [{ required: true, message: "请输入内容", trigger: "blur" }],
};

const isDialogVisible = ref(false);
const dialogTitle = ref("");
const isReadOnly = ref(false);
const feedbackForm = ref(null);
const loading = ref(true);
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(0);
const feedbacks = ref([]);
const currentFeedback = reactive({
  id: "",
  userId: "",
  name: "",
  contactMethod: "",
  contact: "",
  title: "",
  content: "",
  createTime: "",
  status: "",
  type: "",
});

onMounted(() => {
  getFeedbackList();
});

// 计算属性用于格式化字段
const contactMethodFormatted = computed(() => {
  return currentFeedback.contactMethod === "1" ? "Email" : "Phone";
});

const typeFormatted = computed(() => {
  return currentFeedback.type === "1" ? "建议" : "问题";
});

const formatContactMethod = (row: any) => {
  return row.contactMethod === 1 ? "Email" : "Phone";
};

const formatStatus = (row: any) => {
  return row.status === 1 ? "未处理" : "已处理";
};

const formatType = (row: any) => {
  return row.type === 1 ? "建议" : "问题";
};

const handleView = (row: any) => {
  getFeedbackById(row.id);
  dialogTitle.value = "查看反馈";
  isReadOnly.value = true;
  isDialogVisible.value = true;
};

const handleEdit = (row: any) => {
  getFeedbackById(row.id);
  dialogTitle.value = "编辑反馈";
  isReadOnly.value = false;
  isDialogVisible.value = true;
};

const getFeedbackList = async (page = 1) => {
  currentPage.value = page;
  const result: any = await reqfeedBackList(currentPage.value, pageSize.value);
  if (result.code === 200) {
    total.value = result.data.totalElements;
    feedbacks.value = result.data.feedBacksList;
    loading.value = false;
  }
};
// 分页器
const sizeChange = (newSize: number) => {
  pageSize.value = newSize;
  currentPage.value = 1;
  getFeedbackList();
};
const handleDelete = async (row: any) => {
  const result: any = await reqDeletefeedBackById(row.id);
  if (result.code === 200) {
    getFeedbackList();
    ElNotification({ type: "success", message: "删除成功" });
  }
};

const getFeedbackById = async (id: any) => {
  const result: any = await reqHasfeedBackByOne(id);
  if (result.code === 200) {
    Object.assign(currentFeedback, result.data.FeedBack);
  }
};

const submitForm = async () => {
  const result = await reqAddOrUpdatefeedBack(currentFeedback);
  if (result.code === 200) {
    closeDialog();
    getFeedbackList();
    ElNotification({ type: "success", message: "操作成功" });
  }
};

const closeDialog = () => {
  isDialogVisible.value = false;
  resetForm();
};

const resetForm = () => {
  Object.assign(currentFeedback, {
    id: "",
    name: "",
    contactMethod: "",
    contact: "",
    title: "",
    content: "",
    createTime: "",
    status: "",
    type: "",
  });
};
</script>

<style scoped>
.feedback-management {
  padding: 20px;
}

.feedback-management-card {
  padding: 0;
  border-radius: 12px;
  height: 790.22px;
}

.el-header {
  padding: 10px 20px;
  background-color: #f5f7fa;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ebeef5;
}

.el-header h2 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.el-header span {
  color: #606266;
}

.el-main {
  padding: 20px;
}

.el-table th,
.el-table td {
  text-align: center;
  font-size: 14px;
}

.el-dialog__footer {
  display: flex;
  justify-content: flex-end;
  padding: 20px 15px;
}

.el-dialog__header {
  font-size: 18px;
  color: #303133;
}

.el-dialog {
  border-radius: 12px;
}
</style>
