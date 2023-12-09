<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 60px; text-align: center;">
      <el-col :span="6" :offset="3">
        <el-card style="color: #409EFF">
          <div><i class="el-icon-user-solid" /> 用户总数</div>
          <div style="padding: 10px 0; font-weight: bold">
            {{ clientCount }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C">
          <div><i class="el-icon-money" /> 商品种类</div>
          <div style="padding: 10px 0; font-weight: bold">
            {{ goodCount }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67C23A">
          <div><i class="el-icon-bank-card" /> 营业额</div>
          <div style="padding: 10px 0; font-weight: bold">
            {{ saleSum }}
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div id="pie" style="width: 100%; height: 500px"></div>
      </el-col>
      <el-col :span="12">
        <div id="line" style="width: 100%; height: 500px"></div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div id="bar" style="width: 100%; height: 500px"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Echarts",
  data() {
    return {
      clientCount: 0,  // 用户总数
      goodCount: 0,    // 商品种类
      saleSum: 0    // 收益总额
    }
  },
  mounted() {
    this.loadData();
    this.loadPieChartData();
    this.loadBarChartData();
    this.loadLineChartData();
  },
  methods: {
    loadData() {
      this.request.get("/echarts/three").then(res => {
        if (res && res.data) {
          this.clientCount = res.data[0];
          this.goodCount = res.data[1];
          this.saleSum = res.data[2];
        }
      });
    },
    loadPieChartData() {
      var pieOption = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: '营业额',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [] // 数据待填充
          }
        ]
      };

      var pieDom = document.getElementById('pie');
      var pieChart = echarts.init(pieDom);

      this.request.get("/echarts/one").then(res => {
        let total = res.data.reduce((a, b) => a + b, 0); // 计算总营业额
        pieOption.series[0].data = [
          {name: "第一季度", value: res.data[0]},
          {name: "第二季度", value: res.data[1]},
          {name: "第三季度", value: res.data[2]},
          {name: "第四季度", value: res.data[3]}
        ].map(item => ({
          ...item,
          value: (item.value / total * 100).toFixed(2) // 转化为占比
        }));
        pieChart.setOption(pieOption);
      })
    },
    loadBarChartData() {
      var barOption = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['销售量', '进货量']
        },
        xAxis: {
          type: 'category',
          data: [] // 商品名称
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '销售量',
            type: 'bar',
            data: [] // 销售量数据
          },
          {
            name: '进货量',
            type: 'bar',
            data: [] // 进货量数据
          }
        ]
      };

      var barDom = document.getElementById('bar');
      var barChart = echarts.init(barDom);

      this.request.get("/echarts/two").then(res => {
        barOption.xAxis.data = res.data.name;
        barOption.series[0].data = res.data.sale;
        barOption.series[1].data = res.data.restock;
        barChart.setOption(barOption);
      });
    },
    loadLineChartData() {
      var lineOption = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['第一季度', '第二季度', '第三季度', '第四季度']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [],
          type: 'line',
          areaStyle: {}
        }]
      };

      var lineDom = document.getElementById('line');
      var lineChart = echarts.init(lineDom);

      this.request.get("/echarts/one").then(res => {
        lineOption.series[0].data = res.data;
        lineChart.setOption(lineOption);
      });
    }
  }
}
</script>

<style scoped>

</style>
