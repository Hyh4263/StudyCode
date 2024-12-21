<template>
  <!-- 底部区域 -->
  <el-footer class="footer">
    <div class="footer-content">
      <!-- 公司 Logo 和版权信息 -->
      <div class="footer-section footer-logo">
        <p class="logo-text">Hyh</p>
        <p>© {{ currentYear }} Powered by Hyh. All Rights Reserved.</p>
      </div>

      <!-- 友链区域 -->
      <div class="footer-section footer-links">
        <h3>友情链接</h3>
        <ul class="friend-links">
          <li>
            <a href="https://jbk.39.net/" target="_blank" rel="noopener">39健康网</a>
          </li>
          <li>
            <a href="https://ypk.39.net/" target="_blank" rel="noopener">药品通</a>
          </li>
          <li>
            <a href="https://www.baiji.com.cn/" target="_blank" rel="noopener"
              >康德乐大药房</a
            >
          </li>
        </ul>
      </div>

      <!-- 联系方式 -->
      <div class="footer-section footer-contact">
        <h3>联系我们</h3>
        <ul>
          <li>Email: <a href="mailto:2670914429@qq.com">2670914429@qq.com</a></li>
          <li>电话: <a href="tel:+1234567890">+1 234 567 890</a></li>
          <li>地址:Wuzhou University</li>
        </ul>
      </div>
    </div>
  </el-footer>
</template>

<script setup lang="ts">
import { ref } from "vue";

const currentYear = ref(new Date().getFullYear());
</script>

<style scoped>
.footer {
  background-color: #333333;
  color: #ffffff;
  text-align: center;
  padding: 20px 0;
  font-size: 14px;
  width: 100%;
  height: 150px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  max-width: 1200px;
  margin: 0 auto;
}

/* 栏目通用样式 */
.footer-section {
  flex: 1;
  min-width: 200px;
}

.footer-section h3 {
  font-size: 16px;
  color: #ffffff;
  margin-bottom: 10px;
  font-weight: bold;
  text-transform: uppercase;
}

.footer-section p,
.footer-section ul {
  margin: 0;
  padding: 0;
}

.footer-section ul {
  list-style: none;
}

.footer-section ul li {
  margin: 8px 0;
}

.footer-section a {
  color: #f5f5f5;
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-section a:hover {
  color: #00bcd4; /* 添加悬停颜色 */
  text-decoration: underline;
}

/* Logo 样式 */
.footer-logo .logo-text {
  font-size: 20px;
  font-weight: bold;
  /* color: #00bcd4; */
  color: #f5f5f5;
  margin-bottom: 5px;
}
</style>
