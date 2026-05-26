  <template>
    <div class="ai-service-container">
      <div class="header">
        <h3>AI智能客服</h3>
        <el-button icon="el-icon-close" @click="closeService">关闭</el-button>
      </div>

      <div class="message-container" ref="messageBox">
        <div
          v-for="msg in messages"
          :key="msg.id"
          class="message"
          :class="msg.role"
        >
          <img
            v-if="msg.role !== 'user'"
            :src="aiAvatar"
            class="avatar"
          />

          <div class="content">{{ msg.content }}</div>

          <img
            v-if="msg.role === 'user'"
            src="@/assets/user-avatar.png"
            class="avatar"
          />
        </div>

        <div class="message bot" v-if="loading">
          <img :src="aiAvatar" alt="AI客服" class="avatar" />
          <div class="content loading-animation">
            <div class="loader-dot"></div>
            <div class="loader-dot"></div>
            <div class="loader-dot"></div>
          </div>
        </div>
      </div>

      <div class="input-area">
        <el-input
          v-model="userInput"
          placeholder="请输入经营咨询问题..."
          @keyup.enter.native="sendMessage"
          :disabled="loading"
          size="small"
        ></el-input>
        <el-button
          type="primary"
          size="small"
          @click="sendMessage"
          :loading="loading"
        >发送</el-button>
      </div>
    </div>
  </template>

  <script>
  import axios from 'axios'; // 确保安装并引入了 axios

  export default {
    data() {
      return {
        userInput: '',
        loading: false,
        messages: [
          { id: 1, role: 'bot', content: '您好！我是您的AI助理，有什么可以帮您的吗？' }
        ],
        aiAvatar: require('@/assets/ai-avatar.png'),
      };
    },
    methods: {
      async sendMessage() {
        const text = this.userInput.trim();
        if (!text || this.loading) return;

        // 1. 展现用户消息
        this.messages.push({
          id: Date.now(),
          role: 'user',
          content: text
        });

        this.userInput = '';
        this.loading = true;
        this.scrollToBottom();

        try {
          // ✨ 修正点：对接你刚才写好的后端接口
          const res = await axios({
            method: 'post',
            url: '/api/ai/chat',
            data: text, // 直接传字符串
            headers: {
              'Content-Type': 'text/plain' // 强制指定纯文本格式
            }
          });

          if (res.data.code === 1) {
            this.messages.push({
              id: Date.now() + 1,
              role: 'bot',
              content: res.data.data
            });
          } else {
            throw new Error(res.data.msg || '未知错误');
          }
        } catch (error) {
          this.messages.push({
            id: Date.now() + 1,
            role: 'bot',
            content: '抱歉，我现在连接不上大脑，请检查网络或咨询管理员。'
          });
          console.log(error);
        } finally {
          this.loading = false;
          this.scrollToBottom();
        }
      },
      scrollToBottom() {
        this.$nextTick(() => {
          const container = this.$refs.messageBox;
          container.scrollTop = container.scrollHeight;
        });
      },
      closeService() {
        this.$emit('close');
      }
    }
  };
  </script>
  <style lang="scss" scoped>
  .ai-service-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: calc(100vh - 100px); /* 自动适应屏幕高度 */
    background: #f5f7fa;
    border-radius: 12px;
    box-shadow: 0 8px 24px rgba(0,0,0,0.15);
    overflow: hidden;
    position: relative; /* 改为 relative 或取消 */
    right: 0;
    bottom: 0;
    border: 1px solid #e4e7ed;
    .input-area {
      padding: 15px;
      background: #fff;
      border-top: 1px solid #ebeef5;
      display: flex;
      gap: 10px;
      align-items: center;
    }
    .header {
      background: #409eff;
      color: white;
      padding: 10px 15px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      h3 { margin: 0; font-size: 16px; }
      .el-button { color: white; border: none; background: transparent; padding: 5px; }
    }

    .message-container {
      flex: 1;
      padding: 15px;
      overflow-y: auto;
      background: #fff;

      .message {
        display: flex;
        margin-bottom: 20px;
        align-items: flex-start;

        .avatar {
          width: 35px;
          height: 35px;
          border-radius: 50%;
          background: #eee;
        }

        .content {
          max-width: 70%;
          padding: 10px 14px;
          border-radius: 8px;
          font-size: 14px;
          line-height: 1.5;
          word-break: break-all;
          position: relative;
        }

        // 机器人和他人消息样式
        &.bot, &.human {
          .content {
            margin-left: 10px;
            background: #f0f2f5;
            color: #303133;
            border-top-left-radius: 2px;
          }
        }

        // 用户消息样式（居右）
        &.user {
          flex-direction: row; // 保持头像在内容右侧
          justify-content: flex-end;
          .content {
            margin-right: 10px;
            background: #409eff;
            color: #fff;
            border-top-right-radius: 2px;
          }
        }
      }
    }

    // 输入区域建议增加
    .input-area {
      padding: 10px;
      background: #fff;
      border-top: 1px solid #ebeef5;
      display: flex;
      gap: 10px;
    }

    // 加载动画
    .loading-animation {
      display: flex;
      gap: 4px;
      padding: 12px !important;
      .loader-dot {
        width: 6px;
        height: 6px;
        background: #909399;
        border-radius: 50%;
        animation: blink 1.4s infinite both;
        &:nth-child(2) { animation-delay: 0.2s; }
        &:nth-child(3) { animation-delay: 0.4s; }
      }
    }
  }

  @keyframes blink {
    0% { opacity: 0.2; }
    20% { opacity: 1; }
    100% { opacity: 0.2; }
  }
  </style>
