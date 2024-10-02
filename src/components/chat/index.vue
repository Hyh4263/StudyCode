<template>
  <div class="chat-message">
    <div
      v-for="(message, index) in messages"
      :key="index"
      :class="['message', message.sender]"
    >
      <div class="message-content">{{ message.content }}</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    messages: {
      type: Array,
      required: true,
    },
  },
};
</script>

<style scoped>
.chat-message .message {
  display: flex;
  padding: 5px;
}
.chat-message .message.user {
  justify-content: flex-end;
}
.chat-message .message.doctor {
  justify-content: flex-start;
}
.message-content {
  max-width: 60%;
  padding: 10px;
  border-radius: 5px;
  background-color: #f1f1f1;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
}
.message.user .message-content {
  background-color: lightskyblue;
}
</style>
