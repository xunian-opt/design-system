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

    <div class="charts-grid">
      <el-card shadow="never" class="chart-card">
        <div slot="header">
          <span>商品分类统计</span>
        </div>
        <div id="categoryChart" style="width: 100%; height: 400px;"></div>
      </el-card>

      <el-card shadow="never" class="chart-card">
        <div slot="header">
          <span>商品品牌分布</span>
        </div>
        <div id="brandChart" style="width: 100%; height: 400px;"></div>
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
import * as echarts from 'echarts'
import request from '@/utils/request'

export default {
  name: 'Home',
  data() {
    return {
      currentDate: '',
      stats: [
        { title: '总用户数', value: '1,203', icon: 'el-icon-s-custom', color: '#1890ff' },
        { title: '本月订单', value: '58', icon: 'el-icon-s-order', color: '#52c41a' },
        { title: '商品库存', value: '8,540', icon: 'el-icon-s-goods', color: '#faad14' },
        { title: '总交易额', value: '¥ 320,000', icon: 'el-icon-money', color: '#f5222d' }
      ],
      categoryChart: null,
      brandChart: null
    }
  },
  created() {
    this.initDate();
  },
  mounted() {
    this.initCharts();
    window.addEventListener('resize', this.handleResize);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize);
    if (this.categoryChart) this.categoryChart.dispose();
    if (this.brandChart) this.brandChart.dispose();
  },
  methods: {
    initDate() {
      const now = new Date();
      const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' };
      this.currentDate = now.toLocaleDateString('zh-CN', options);
    },
    
    initCharts() {
      this.initCategoryChart();
      this.initBrandChart();
    },

    // 1. 商品分类统计 (饼图)
    initCategoryChart() {
      request.get('/zhuangxiushangpin/group/shangpinfenlei').then(res => {
        if (res && res.code === 0 && res.data) {
          // 后端返回的数据格式通常为 [{shangpinfenlei: 'xx', total: 10}, ...]
          // 我们需要将其转换为 Echarts 需要的 {name: 'xx', value: 10}
          const data = res.data.map(item => ({
            name: item.shangpinfenlei,
            value: item.total
          }));

          const dom = document.getElementById('categoryChart');
          this.categoryChart = echarts.init(dom);
          
          const option = {
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left'
            },
            series: [
              {
                name: '商品分类',
                type: 'pie',
                radius: '50%',
                data: data,
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          };
          this.categoryChart.setOption(option);
        }
      });
    },

    // 2. 商品品牌分布 (柱状图) - 作为库存构成统计
    initBrandChart() {
      request.get('/zhuangxiushangpin/group/shangpinpinpai').then(res => {
        if (res && res.code === 0 && res.data) {
          const xData = res.data.map(item => item.shangpinpinpai);
          const yData = res.data.map(item => item.total);

          const dom = document.getElementById('brandChart');
          this.brandChart = echarts.init(dom);

          const option = {
            tooltip: {
              trigger: 'axis',
              axisPointer: { type: 'shadow' }
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              containLabel: true
            },
            xAxis: [
              {
                type: 'category',
                data: xData,
                axisTick: { alignWithLabel: true },
                axisLabel: { interval: 0, rotate: 30 } // 防止标签重叠
              }
            ],
            yAxis: [
              {
                type: 'value'
              }
            ],
            series: [
              {
                name: '商品数量',
                type: 'bar',
                barWidth: '60%',
                data: yData,
                itemStyle: {
                  color: '#409EFF'
                }
              }
            ]
          };
          this.brandChart.setOption(option);
        }
      });
    },

    handleResize() {
      if (this.categoryChart) this.categoryChart.resize();
      if (this.brandChart) this.brandChart.resize();
    }
  }
}
</script>

<style scoped>
.home-container {
  padding: 10px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

/* 统计卡片网格 */
.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

/* 图表网格 */
.charts-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .dashboard-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .charts-grid {
    grid-template-columns: 1fr; /* 小屏幕下图表垂直排列 */
  }
}

@media (max-width: 768px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
}

.stat-card, .chart-card, .welcome-card {
  border-radius: 4px;
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
  flex-shrink: 0;
}

.stat-info h3 {
  margin: 0;
  font-size: 14px;
  color: #909399;
  font-weight: normal;
}

.stat-info p {
  margin: 8px 0 0;
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

/* 欢迎卡片 */
.welcome-card {
  min-height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  margin-top: 20px;
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
  color: #409EFF;
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