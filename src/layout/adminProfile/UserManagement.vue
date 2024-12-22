<template>
  <el-card style="height: 100%">
    <div class="user-management">
      <el-container>
        <el-header>
          <h2>用户管理</h2>
          <span>
            共收集 {{ total }} 条用户信息。
            <el-link type="primary" @click="openDialog('新增用户')">新增用户</el-link>
          </span>
        </el-header>
        <el-main>
          <el-table :data="users" width="100%">
            <el-table-column
              prop="userAccount"
              label="用户账号"
              width="150"
            ></el-table-column>
            <el-table-column prop="userName" label="用户名" width="150"></el-table-column>
            <el-table-column prop="email" label="邮箱" width="160"></el-table-column>
            <el-table-column prop="role" label="角色" width="150">
              <template #default="{ row }">
                <el-tag
                  :type="row.role === 2 ? 'danger' : row.role === 1 ? 'warning' : 'info'"
                  effect="dark"
                >
                  {{ formatRole(row.role) }}
                </el-tag>
              </template>
            </el-table-column>

            <el-table-column
              prop="status"
              label="状态"
              width="120"
              :formatter="formatStatus"
            >
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'info'" effect="dark">
                  {{ formatStatus(row) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" align="center">
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
                    @click="confirmDelete(scope.row)"
                    type="danger"
                    icon="Delete"
                  ></el-link>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            @current-change="getUserList"
            @size-change="sizeChange"
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[5, 10, 15, 20]"
            :background="true"
            layout="prev, pager, next, jumper, ->, sizes, total"
            :total="total"
          />
        </el-main>

        <!-- Dialog for Viewing/Editing/Adding Users -->
        <el-dialog v-model="isDialogVisible" :title="dialogTitle">
          <el-form :model="currentUser" ref="userForm" :rules="rules" label-width="120px">
            <el-form-item label="用户账号" prop="userAccount">
              <el-input v-model="currentUser.userAccount" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="密码" prop="userPwd">
              <el-input v-model="currentUser.userPwd" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="用户名" prop="userName">
              <el-input v-model="currentUser.userName" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="邮箱" prop="userEmail">
              <el-input v-model="currentUser.userEmail" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="电话" prop="userTel">
              <el-input v-model="currentUser.userTel" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="年龄" prop="userAge">
              <el-input v-model="currentUser.userAge" :disabled="isReadOnly" />
            </el-form-item>
            <el-form-item label="性别" prop="userSex">
              <el-input v-model="currentUser.userSex" :disabled="isReadOnly" />
            </el-form-item>

            <el-form-item label="角色" prop="roleStatus">
              <template v-if="isReadOnly">
                <el-input :value="formatRole(currentUser.roleStatus)" disabled />
              </template>
              <el-select v-else v-model="currentUser.roleStatus" placeholder="请选择角色">
                <el-option label="普通用户" :value="0"></el-option>
                <el-option label="医师" :value="1"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="状态" prop="status">
              <template v-if="isReadOnly">
                <el-input :value="formatStatus(currentUser)" disabled />
              </template>
              <el-select v-else v-model="currentUser.status" placeholder="请选择状态">
                <el-option label="正常" :value="1"></el-option>
                <el-option label="禁用" :value="2"></el-option>
              </el-select>
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
import { ref, reactive, computed, onMounted } from "vue";
import { ElNotification, ElMessageBox } from "element-plus";
import {
  reqGetUserList,
  reqGetUserById,
  reqAddOrUpdateUser,
  reqDeleteUserById,
} from "@/api/user/index"; // 假设API请求文件

// 表单验证规则
const rules = {
  userName: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  email: [{ required: true, message: "请输入邮箱", trigger: "blur" }],
  role: [{ required: true, message: "请选择角色", trigger: "change" }],
  status: [{ required: true, message: "请选择状态", trigger: "change" }],
};

const isDialogVisible = ref(false);
const dialogTitle = ref("");
const isReadOnly = ref(false);
const userForm = ref<any>(null);
const currentPage = ref(1);
const pageSize = ref(5);
const total = ref(0);
const users = ref([]);
const currentUser = reactive({
  id: "",
  userName: "",
  userEmail: "",
  userPwd: "",
  roleStatus: 1,
  status: 1,
  imgPath: "",
  userAge: "",
  userSex: "",
  userTel: "",
  userAccount: "",
});

const sizeChange = (newSize: number) => {
  pageSize.value = newSize;
  currentPage.value = 1;
  getUserList();
};

// 获取角色的文本显示
const formatRole = (role: number) => {
  return role === 2 ? "超级管理员" : role === 1 ? "医师" : "普通用户";
};

// 格式化状态
const formatStatus = (row: any) => {
  return row.status === 1 ? "正常" : "禁用";
};

// 获取用户列表
const getUserList = async (page = 1) => {
  currentPage.value = page;
  const result: any = await reqGetUserList(currentPage.value, pageSize.value);
  console.log("res:", result);
  if (result.code === 200) {
    total.value = result.data.totalElements;
    users.value = result.data.userList.map((item: any) => ({
      id: item.id,
      userAccount: item.userAccount,
      imgPath: item.imgPath,
      userAge: item.userAge,
      userName: item.userName,
      email: item.userEmail,
      userSex: item.userSex,
      userTel: item.userTel,
      role: item.roleStatus,
      status: item.status,
      createTime: item.createTime,
      updateTime: item.updateTime,
    }));
  }
};

// 获取用户详情
const getUserById = async (id: any) => {
  const result: any = await reqGetUserById(id);
  console.log("resdata:", result);
  if (result.code === 200) {
    Object.assign(currentUser, result.data);
  }
};

// 新增或编辑提交
const submitForm = async () => {
  const valid = await userForm.value.validate();
  if (!valid) return;

  const result: any = await reqAddOrUpdateUser(currentUser);
  if (result.code === 200) {
    ElNotification({ type: "success", message: "操作成功" });
    closeDialog();
    getUserList();
  } else {
    ElNotification({ type: "error", message: "操作失败" });
  }
};

// 删除用户
const confirmDelete = (row: any) => {
  ElMessageBox.confirm("确定删除该用户吗？", "删除确认", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => handleDelete(row))
    .catch(() => {});
};

const handleDelete = async (row: any) => {
  const result: any = await reqDeleteUserById(row.id);
  if (result.code === 200) {
    ElNotification({ type: "success", message: "删除成功" });
    getUserList();
  } else {
    ElNotification({ type: "error", message: "删除失败" });
  }
};

// 查看和编辑用户
const handleView = (row: any) => {
  getUserById(row.id);
  dialogTitle.value = "查看用户";
  isReadOnly.value = true;
  isDialogVisible.value = true;
};

const handleEdit = (row: any) => {
  getUserById(row.id);
  dialogTitle.value = "编辑用户";
  isReadOnly.value = false;
  isDialogVisible.value = true;
};

// 打开新增对话框
const openDialog = (title: string) => {
  resetForm();
  dialogTitle.value = title;
  isReadOnly.value = false;
  isDialogVisible.value = true;
};

// 关闭对话框
const closeDialog = () => {
  resetForm();
  isDialogVisible.value = false;
};

// 重置表单
const resetForm = () => {
  Object.assign(currentUser, {
    id: "",
    userName: "",
    email: "",
    roleStatus: 1,
    status: 1,
  });
  if (userForm.value) userForm.value.resetFields();
};

onMounted(() => {
  getUserList();
});
</script>

<style scoped>
.user-management {
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
