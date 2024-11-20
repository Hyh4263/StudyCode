<template>
  <el-dialog v-model="dialogTableVisible" title="Shipping address" width="800">
    <el-table :data="gridData">
      <el-table-column property="date" label="Date" width="150" />
      <el-table-column property="name" label="Name" width="200" />
      <el-table-column property="address" label="Address" />
    </el-table>
  </el-dialog>
</template>

<script setup lang="ts">
import { fa } from "element-plus/es/locale/index.mjs";
import { reactive, ref } from "vue";
import { defineProps, defineEmits } from "vue";
// const dialogFormVisible = ref(false);
// const formLabelWidth = "140px";

const props = defineProps({
  isDialogVisible: Boolean,
});

const dialogTableVisible = props.isDialogVisible;
// console.log("数据", dialogTableVisible);

// console.log("父传递数据", props);
const form = reactive({
  name: "",
  region: "",
  date1: "",
  date2: "",
  delivery: false,
  type: [],
  resource: "",
  desc: "",
});

const gridData = [
  {
    date: "2016-05-02",
    name: "John Smith",
    address: "No.1518,  Jinshajiang Road, Putuo District",
  },
  {
    date: "2016-05-04",
    name: "John Smith",
    address: "No.1518,  Jinshajiang Road, Putuo District",
  },
  {
    date: "2016-05-01",
    name: "John Smith",
    address: "No.1518,  Jinshajiang Road, Putuo District",
  },
  {
    date: "2016-05-03",
    name: "John Smith",
    address: "No.1518,  Jinshajiang Road, Putuo District",
  },
];
// import { ref, defineProps, defineEmits } from "vue";
// import type { FormInstance } from "element-plus";

// // 定义 props 类型
// const props = defineProps<{
//   visible: boolean;
//   form: {
//     category?: string;
//     name?: string;
//     specialCondition?: string;
//     lastUpdateTime?: string;
//   };
//   dialogTitle: string;
// }>();

// const emit = defineEmits<{
//   (
//     event: "submit",
//     data: {
//       category?: string;
//       name?: string;
//       specialCondition?: string;
//       lastUpdateTime?: string;
//     }
//   ): void;
//   (event: "close"): void;
// }>();

// const formRef = ref<FormInstance | null>(null);

// // 表单验证规则
// const rules = {
//   category: [{ required: true, message: "请输入分类", trigger: "blur" }],
//   name: [{ required: true, message: "请输入疾病名称", trigger: "blur" }],
// };

// // 提交表单
// const handleSubmit = () => {
//   if (formRef.value) {
//     formRef.value.validate((valid: boolean) => {
//       if (valid) {
//         emit("submit", { ...props.form });
//         handleClose(); // 关闭对话框
//       } else {
//         console.log("表单验证失败");
//       }
//     });
//   }
// };

// // 关闭对话框
// const handleClose = () => {
//   emit("close");
// };
</script>

<style scoped>
/* 自定义样式 */
</style>
