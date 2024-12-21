<template>
  <div :class="['message-item', { 'message-self': isSelf }]">
    <div class="message-avatar">
      <img :src="message.avatar" alt="头像" />
    </div>
    <div class="message-content">
      <div class="message-text">{{ message.message }}</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    message: {
      type: Object,
      required: true,
    },
    isSelf: {
      type: Boolean,
      required: true,
    },
  },
};
</script>

<style scoped>
.message-item {
  display: flex;
  gap: 10px;
  margin: 5px 0;
}

.message-self {
  flex-direction: row-reverse;
}

.message-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.message-content {
  max-width: 60%;
}

.message-text {
  padding: 10px;
  border-radius: 10px;
  background-color: #e6e6e6;
}

.message-self .message-text {
  background-color: #409eff;
  color: #fff;
}
</style>
