<template>
  <el-card style="height: 100%">
    <div class="healthy-management">
      <el-container>
        <el-header>
          <h2>全部健康话题</h2>
          <span>
            共收集 {{ total }} 条健康话题。你可以继续
            <el-link type="primary" @click="openDialog('新增话题')">新增话题</el-link>
          </span>
        </el-header>
        <el-main>
          <el-table :data="topics" width="100%">
            <el-table-column prop="title" label="标题" width="200"></el-table-column>
            <el-table-column
              prop="topicContent"
              label="话题内容"
              width="350"
              :formatter="formatTopicContent"
            ></el-table-column>

            <el-table-column
              prop="status"
              label="状态"
              width="100"
              :formatter="formatStatus"
            >
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'info'" effect="dark">
                  {{ formatStatus(row) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column
              prop="viewNumber"
              label="浏览量"
              width="100"
            ></el-table-column>
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
            @current-change="getTopicList"
            @size-change="sizeChange"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[3, 5, 7, 9]"
            :background="true"
            layout="prev, pager, next, jumper, ->, sizes, total"
            :total="total"
          />
        </el-main>
        <el-dialog v-model="isDialogVisible" :title="dialogTitle">
          <el-form
            :model="currentTopic"
            ref="topicForm"
            :rules="rules"
            label-width="120px"
          >
            <el-form-item label="标题" prop="title">
              <el-input v-model="currentTopic.title" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="话题内容" prop="topicContent">
              <el-input
                type="textarea"
                v-model="currentTopic.topicContent"
                :disabled="isReadOnly"
              />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <template v-if="isReadOnly">
                <el-input :value="formatStatus(currentTopic)" disabled />
              </template>
              <el-select v-else v-model="currentTopic.status" placeholder="请选择状态">
                <el-option label="正常" :value="1"></el-option>
                <el-option label="下架" :value="2"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="浏览量" prop="viewNumber">
              <el-input v-model="currentTopic.viewNumber" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="图片" prop="imgPath">
              <div class="edit-profile-photo">
                <el-image
                  style="width: 200px; height: 200px; border-radius: 50%"
                  :src="currentTopic.imgPath"
                  alt="图片预览"
                />
                <div v-if="!isReadOnly" class="change-photo-btn">
                  <div class="photoUpload">
                    <span><i class="fa fa-upload"></i> 上传图片</span>
                    <input type="file" class="upload" @change="handleImageChange" />
                  </div>
                </div>
              </div>
            </el-form-item>
            <el-form-item label="视频" prop="videoPath">
              <div class="edit-profile-photo">
                <video
                  v-if="currentTopic.videoPath"
                  style="width: 350px; height: 350px; border-radius: 8px"
                  :src="currentTopic.videoPath"
                  controls
                >
                  您的浏览器不支持 video 标签。
                </video>
                <div v-if="!isReadOnly" class="change-photo-btn">
                  <div class="photoUpload">
                    <span><i class="fa fa-upload"></i> 上传视频</span>
                    <input type="file" class="upload" @change="handleVideoChange" />
                  </div>
                </div>
              </div>
            </el-form-item>
            <el-form-item label="更新时间" prop="updateTime">
              <el-input v-model="currentTopic.updateTime" disabled />
            </el-form-item>
            <el-form-item label="创建时间" prop="createTime">
              <el-input v-model="currentTopic.createTime" disabled />
            </el-form-item>
          </el-form>
          <div slot="footer" v-if="!isReadOnly">
            <el-button @click="closeDialog">取消</el-button>
            <el-button type="primary" @click="submitForm">提交</el-button>
          </div>
        </el-dialog>
      </el-container>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from "vue";
import { View, Edit, Delete } from "@element-plus/icons-vue";
import { ElNotification, ElMessageBox } from "element-plus";
import {
  reqHealthyList,
  reqHealthyById,
  reqAddOrUpdateHealthy,
  reqDeleteHealthyById,
} from "@/api/healthy/index";
import useUserStore from "@/stores/modules/user";

const rules = {
  title: [{ required: true, message: "请输入话题标题", trigger: "blur" }],
  topicContent: [{ required: true, message: "请输入话题内容", trigger: "blur" }],
  preView: [{ required: true, message: "请输入话题预览内容", trigger: "blur" }],
  status: [{ required: true, message: "请选择状态", trigger: "change" }],
  viewNumber: [{ required: true, message: "请输入浏览量", trigger: "blur" }],
};

const isDialogVisible = ref(false);
const dialogTitle = ref("");
const isReadOnly = ref(false);
const topicForm = ref<any>(null);
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(0);
const topics = ref([]);
const userStore = useUserStore();
const user = computed(() => ({ id: userStore.id }));

const currentTopic = reactive({
  id: "",
  imgPath: "",
  videoPath: "",
  title: "",
  topicContent: "",
  status: "",
  viewNumber: 0,
  createTime: "",
  updateTime: "",
  file: null as File | null,
  videoFile: null as File | null,
});

const formatStatus = (row: any) => {
  return row.status === 1 ? "正常" : "下架";
};

const formatTopicContent = (row: any) => {
  return row.topicContent.length > 30
    ? row.topicContent.slice(0, 30) + "..."
    : row.topicContent;
};

const sizeChange = (newSize: number) => {
  pageSize.value = newSize;
  currentPage.value = 1;
  getTopicList();
};

const getTopicList = async (page = 1) => {
  currentPage.value = page;
  const result: any = await reqHealthyList(currentPage.value, pageSize.value);
  if (result.code === 200) {
    total.value = result.data.totalElements;
    topics.value = result.data.healthyList.map((item: any) => ({
      id: item.id,
      imgPath: item.imgPath,
      videoPath: item.videoPath,
      title: item.title,
      topicContent: item.topicContent,
      dynamic: item.dynamic,
      preView: item.preView,
      status: item.status,
      viewNumber: item.viewNumber,
      createTime: item.createTime,
      updateTime: item.updateTime,
    }));
  }
};

const getHealthyById = async (id: any) => {
  const res: any = await reqHealthyById(id);

  if (res.code === 200) {
    Object.assign(currentTopic, res.data.Healthy);
  }
};

const handleImageChange = (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0];
  if (file) {
    currentTopic.imgPath = URL.createObjectURL(file);
    currentTopic.file = file;
  }
};

const handleVideoChange = (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0];
  if (file) {
    currentTopic.videoPath = URL.createObjectURL(file);
    currentTopic.videoFile = file;
  }
};

const uploadFile = async (file: File, type: "image" | "video") => {
  const formData = new FormData();
  formData.append(type, file);
  formData.append("userId", user.value.id);

  try {
    const response =
      type === "image"
        ? await userStore.upload(formData)
        : await userStore.uploadVideo(formData);
    return response.data;
  } catch {
    ElNotification({
      type: "error",
      message: `${type === "image" ? "图片" : "视频"}上传失败`,
    });
    throw new Error("上传失败");
  }
};

const submitForm = async () => {
  try {
    const valid = await topicForm.value.validate();
    if (!valid) return ElNotification({ type: "error", message: "验证失败" });

    let imagePath = currentTopic.imgPath;
    let videoPath = currentTopic.videoPath;

    if (currentTopic.file) imagePath = await uploadFile(currentTopic.file, "image");
    if (currentTopic.videoFile)
      videoPath = await uploadFile(currentTopic.videoFile, "video");

    const updatedTopic = {
      id: currentTopic.id || null,
      title: currentTopic.title || null,
      topicContent: currentTopic.topicContent || null,
      status: currentTopic.status || 1,
      viewNumber: currentTopic.viewNumber || 0,
      imgPath: imagePath,
      videoPath: videoPath,
      userId: user.value.id,
    };

    const result: any = await reqAddOrUpdateHealthy(updatedTopic);
    if (result.code === 200) {
      closeDialog();
      getTopicList();
      ElNotification({ type: "success", message: "操作成功" });
    } else {
      ElNotification({ type: "error", message: "操作失败" });
    }
  } catch {
    ElNotification({ type: "error", message: "提交失败" });
  }
};

const confirmDelete = (row: any) => {
  ElMessageBox.confirm("确定删除该话题吗？", "删除确认", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => handleDelete(row))
    .catch(() => {});
};

const handleDelete = async (row: any) => {
  try {
    const deleteImgRes = await userStore.deleteImg(row.imgPath);
    const deleteVideoRes = await userStore.deleteVideo(row.videoPath);
    if (deleteImgRes.code === 200 && deleteVideoRes.code === 200) {
      const deleteMedicineRes: any = await reqDeleteHealthyById(row.id);
      if (deleteMedicineRes.code === 200) {
        getTopicList();
        ElNotification({
          type: "success",
          message: "删除成功",
        });
      }
    } else {
      ElNotification({
        type: "error",
        message: "删除失败,图片或视频没有被删除",
      });
    }
  } catch (error) {
    ElNotification({
      type: "error",
      message: (error as Error).message,
    });
  }
};

// 操作函数
const handleView = (row: any) => {
  getHealthyById(row.id);
  dialogTitle.value = "查看话题";
  isReadOnly.value = true;
  isDialogVisible.value = true;
};

const handleEdit = (row: any) => {
  getHealthyById(row.id);
  dialogTitle.value = "编辑话题";
  isReadOnly.value = false;
  isDialogVisible.value = true;
};

const openDialog = (title: string) => {
  resetForm();
  dialogTitle.value = title;
  isReadOnly.value = false;
  isDialogVisible.value = true;
};

const closeDialog = () => {
  resetForm();
  isDialogVisible.value = false;
};

const resetForm = () => {
  Object.assign(currentTopic, {
    id: "",
    imgPath: "",
    videoPath: "",
    title: "",
    topicContent: "",
    preView: "",
    status: 1,
    viewNumber: 0,
    createTime: "",
    updateTime: "",
    file: null,
  });
  if (topicForm.value) topicForm.value.resetFields();
};

onMounted(() => {
  getTopicList();
});
</script>

<style scoped>
.healthy-management {
  padding: 20px;
}
.el-table {
  width: 100%;
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
