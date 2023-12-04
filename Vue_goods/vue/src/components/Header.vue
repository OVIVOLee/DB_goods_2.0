<template>
  <div style="font-size: 12px; line-height: 60px; display: flex">

    <div style="flex: 1; font-size: 20px; display: flex; align-items: center">
      <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>

      <el-breadcrumb separator="/" style="margin-left: 10px; font-size: 15px">
        <el-breadcrumb-item :to="'/'">管理</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
      <h1>TheShy</h1>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    collapse: Boolean,
  },
  computed: {
    currentPathName() {
      return this.$store.state.currentPathName;  //需要监听的数据
    }
  },
  watch: { //监听路由变化
    currentPathName (newVal, oldVal) {
      console.log(newVal)
    }
  },


}
</script>

<style scoped>

</style>