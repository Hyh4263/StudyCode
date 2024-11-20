<template>
  <div class="common-layout">
    <el-container>
      <!-- Header Area -->
      <Header></Header>

      <!-- Content Area -->
      <el-main class="content">
        <el-card class="box-card">
          <template #header>
            <span class="strong">搜索结果:</span>
          </template>

          <!-- Loading State -->
          <el-empty v-if="loading" description="正在加载..." class="empty-state" />

          <!-- Empty State for No Results -->
          <el-empty
            v-else-if="!highlightedResults.length"
            description="暂无搜索结果"
            class="empty-state"
          />

          <!-- Search Results Display with Highlighting -->
          <div v-else>
            <div
              v-for="(result, index) in highlightedResults"
              :key="index"
              class="result-item"
              @click="navigateToDetail(result)"
              style="cursor: pointer"
            >
              <!-- Disease Results -->
              <template v-if="result.illnessId !== null">
                <p>
                  <strong>疾病名称:</strong>
                  <span v-html="result.highlightedIllnessName"></span>
                </p>
                <p>
                  <strong>包含原因:</strong>
                  <span v-html="result.highlightedIncludeReason"></span>
                </p>
                <p>
                  <strong>症状描述:</strong>
                  <span v-html="result.highlightedIllnessSymptom"></span>
                </p>
                <p><strong>创建时间:</strong> {{ result.createTime }}</p>
              </template>

              <!-- Medicine Results -->
              <template v-else>
                <p>
                  <strong>药品名称:</strong>
                  <span v-html="result.highlightedMedicineName"></span>
                </p>
                <p>
                  <strong>药品功效:</strong>
                  <span v-html="result.highlightedMedicineEffect"></span>
                </p>
                <p>
                  <strong>品牌:</strong>
                  <span v-html="result.highlightedMedicineBrand"></span>
                </p>
                <p>
                  <strong>药物相互作用:</strong>
                  <span v-html="result.highlightedInteraction"></span>
                </p>
                <p>
                  <strong>禁忌:</strong> <span v-html="result.highlightedTaboo"></span>
                </p>
                <p><strong>使用方法:</strong> {{ result.usAge }}</p>
                <p><strong>价格:</strong> {{ result.medicinePrice }}</p>
                <p><strong>创建时间:</strong> {{ result.createTime }}</p>
              </template>
            </div>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import Header from "@/components/header/index.vue";
import { ref, watch, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { reqSearch } from "@/api/serach";

const searchQuery = ref("");
const filteredResults = ref([]);
const loading = ref(false);
const route = useRoute();
const router = useRouter();

// Initialize search keyword and perform search
onMounted(() => {
  searchQuery.value = route.query.keyword?.toString() || "";
  performSearch();
});

// Watch for keyword changes and perform search only after performSearch is defined
watch(
  () => route.query.keyword,
  (newQuery) => {
    searchQuery.value = newQuery?.toString() || "";
    performSearch();
  }
);

// Perform search
const performSearch = async () => {
  const trimmedQuery = searchQuery.value.trim();
  if (!trimmedQuery) {
    filteredResults.value = [];
    return;
  }

  loading.value = true;
  try {
    const res: any = await reqSearch(trimmedQuery);
    filteredResults.value = res.code === 200 ? res.data : [];
  } catch (error) {
    console.error("搜索请求失败:", error);
    filteredResults.value = [];
  } finally {
    loading.value = false;
  }
};

// Highlight text based on search query
const highlightText = (text: string, query: string) => {
  if (!text) return "";
  return text.replace(
    new RegExp(query, "gi"),
    (match) => `<span class="highlight">${match}</span>`
  );
};

// Compute highlighted results
const highlightedResults = computed(() => {
  const query = searchQuery.value.trim();
  return !query
    ? filteredResults.value
    : filteredResults.value.map((result: any) => {
        if (result.illnessId !== null) {
          return {
            ...result,
            highlightedIllnessName: highlightText(result.illnessName, query),
            highlightedIncludeReason: highlightText(result.includeReason, query),
            highlightedIllnessSymptom: highlightText(result.illnessSymptom, query),
          };
        } else {
          return {
            ...result,
            highlightedMedicineName: highlightText(result.medicineName, query),
            highlightedMedicineEffect: highlightText(result.medicineEffect, query),
            highlightedMedicineBrand: highlightText(result.medicineBrand, query),
            highlightedInteraction: highlightText(result.interaction, query),
            highlightedTaboo: highlightText(result.taboo, query),
          };
        }
      });
});

// Navigate to detail page based on result type
const navigateToDetail = (result: any) => {
  if (result.illnessId !== null) {
    router.push(`/illness/${result.illnessId}`);
  } else {
    router.push(`/medicine/${result.medicineId}`);
  }
};
</script>

<style scoped>
.common-layout {
  position: absolute;
  inset: 0;
}

.el-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.content {
  background-color: #f3f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: calc(100vh - 64px);
  padding: 0px;
  box-sizing: border-box;
}

.box-card {
  width: 100%;
  height: 100%;
  overflow-y: auto;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  padding: 0px;
  transition: transform 0.3s ease-in-out;
}

.empty-state {
  color: #8e99ab;
  font-size: 18px;
  text-align: center;
  padding: 50px 0;
}

.result-item {
  margin-bottom: 20px;
  padding: 18px;
  border: 1px solid #dde2eb;
  border-radius: 6px;
  background-color: #ffffff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  transition: box-shadow 0.3s, transform 0.3s;
}

.result-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-3px);
}

.result-item p {
  margin: 10px 0;
  font-size: 16px;
  line-height: 1.5;
  color: #444;
}

.strong {
  color: #333;
  font-weight: bold;
  font-size: 18px;
}

.result-item strong {
  color: #333;
  font-weight: bold;
}

.highlight {
  background-color: #ffeb3b;
  color: #222;
  font-weight: bold;
  padding: 2px 4px;
  border-radius: 3px;
  box-shadow: 0px 0px 2px rgba(0, 0, 0, 0.15);
  transition: background-color 0.2s ease-in-out;
}

.highlight:hover {
  background-color: #ffc107;
}
</style>
