<template>

  <el-menu :default-openeds="['1', '3']" style="min-height: 100%"
           background-color="rgb(48, 65, 86)"
           text-color="fff"
           active-text-color="#ffffff"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
           @select="handleSelect"
  >

    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px; margin-right: 5px">
      <b style="color: white" v-show="logoTextShow">商品管理系统</b>
    </div>

    <el-menu-item style="color: white" index="/echarts">
      <template slot="title">
        <i class="el-icon-pie-chart"></i>
        <span slot="title">数据分析</span>
      </template>
    </el-menu-item>

    <el-menu-item style="color: white" index="/client">
      <template slot="title">
        <i class="el-icon-user"></i>
        <span slot="title">客户管理</span>
      </template>
    </el-menu-item>

    <el-menu-item style="color: white" index="/good">
      <template slot="title">
        <i class="el-icon-goods"></i>
        <span slot="title">商品管理</span>
      </template>
    </el-menu-item>

    <el-menu-item style="color: white" index="/restock">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">进货记录</span>
      </template>
    </el-menu-item>

    <el-menu-item style="color: white" index="/sale">
      <template slot="title">
        <i class="el-icon-bangzhu"></i>
        <span slot="title">销售记录</span>
      </template>
    </el-menu-item>


  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
  methods: {
    handleSelect() {

    }
  }
}
</script>

<style scoped>

</style>