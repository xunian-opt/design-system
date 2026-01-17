<template>
  <div class="home-container">
    <div class="dashboard-grid">
      <el-card 
        v-for="(item, index) in stats" 
        :key="index" 
        shadow="hover" 
        :body-style="{ padding: '20px' }"
        class="stat-card"
      >
        <div class="stat-content">
          <div class="stat-icon" :style="{ backgroundColor: item.color }">
            <i :class="item.icon"></i>
          </div>
          <div class="stat-info">
            <h3>{{ item.title }}</h3>
            <p>{{ item.value }}</p>
          </div>
        </div>
      </el-card>
    </div>

    <el-card class="welcome-card" shadow="never">
      <div class="welcome-content">
        <img src="https://img.icons8.com/clouds/200/000000/company.png" alt="Welcome" class="welcome-img">
        <h2>欢迎使用装修公司管理系统</h2>
        <p>今天是 <span class="date-highlight">{{ currentDate }}</span>，祝您工作愉快！</p>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      currentDate: '',
      // 模拟统计数据，后续可对接后端 API
      stats: [
        { 
          title: '总用户数', 
          value: '1,203', 
          icon: 'el-icon-s-custom', 
          color: '#1890ff' // 蓝色
        },
        { 
          title: '本月订单', 
          value: '58', 
          icon: 'el-icon-s-order', 
          color: '#52c41a' // 绿色
        },
        { 
          title: '商品库存', 
          value: '8,540', 
          icon: 'el-icon-s-goods', 
          color: '#faad14' // 橙色
        },
        { 
          title: '总交易额', 
          value: '¥ 320,000', 
          icon: 'el-icon-money', 
          color: '#f5222d' // 红色
        }
      ]
    }
  },
  created() {
    this.initDate();
  },
  methods: {
    initDate() {
      const now = new Date();
      const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' };
      this.currentDate = now.toLocaleDateString('zh-CN', options);
    }
  }
}
</script>

<style scoped>
.home-container {
  padding: 10px;
}

/* 使用 CSS Grid 实现响应式布局 */
.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  flex-shrink: 0; /* 防止图标被压缩 */
}

.stat-info h3 {
  margin: 0;
  font-size: 14px;
  color: #909399; /* Element UI 辅助文字颜色 */
  font-weight: normal;
}

.stat-info p {
  margin: 8px 0 0;
  font-size: 24px;
  font-weight: bold;
  color: #303133; /* Element UI 主要文字颜色 */
}

/* 欢迎卡片样式 */
.welcome-card {
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.welcome-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.welcome-img {
  width: 150px;
  margin-bottom: 20px;
  opacity: 0.8;
}

.welcome-card h2 {
  color: #409EFF; /* Element UI 主题蓝 */
  margin-bottom: 15px;
  font-weight: 600;
}

.welcome-card p {
  color: #606266;
  font-size: 16px;
}

.date-highlight {
  color: #303133;
  font-weight: bold;
  margin: 0 5px;
}
</style>