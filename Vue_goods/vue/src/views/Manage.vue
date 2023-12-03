<template>
  <div style="height: 100%">
    <el-container style="min-height: 100vh; border: 1px solid #eee">

      <el-aside :width="sideWidth + 'px'" style="background-color: burlywood" >
        <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" />
      </el-aside>

      <el-container>

        <el-header style="border-bottom: 1px solid #ccc;">
          <Header :collapseBtnClass="collapseBtnClass" :collapse="isCollapse"/>
        </el-header>

        <el-main>
          <router-view />
        </el-main>

      </el-container>
    </el-container>

  </div>
</template>

<script>
// @ is an alias to /src

import Header from "@/components/Header.vue";
import Aside from "@/components/Aside.vue";
import User from "@/views/User.vue";

export default {
  name: 'Manage',
  data(){
    console.log(this)
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
    }
  },
  components: {
    Aside,
    Header,
    User
  },
  methods: {
    collapse() {// 点击收缩按钮触发
      this.isCollapse = !this.isCollapse;
      if(this.isCollapse) {
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    }
  }
}
</script>
