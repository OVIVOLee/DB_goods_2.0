<template>
  <div style="height: 100%">
    <el-container style="min-height: 100vh; border: 1px solid #eee">
      <el-aside :width="sideWidth + 'px'" style="background-color: rgb(45,113,220);" >
        <el-menu :default-openeds="['1', '3']" style="min-height: 100%"
                 background-color="rgb(48, 65, 86)"
                 text-color="fff"
                 active-text-color="#ffffff"
                 :collapse-transition="false"
                 :collapse="isCollapse"
        >
          <div style="height: 60px; line-height: 60px; text-align: center">
            <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px; margin-right: 5px">
            <b style="color: white" v-show="logoTextShow">商品管理系统</b>
          </div>
          <el-submenu index="1">
            <template slot="title"><i class="el-icon-message"></i>
              <span slot="title" style="color: #cccccc">导航一</span>
            </template>
            <el-menu-item-group>
              <template slot="title">分组一</template>
              <el-menu-item index="1-1">选项1</el-menu-item>
              <el-menu-item index="1-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="1-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="1-4">
              <template slot="title">选项4</template>
              <el-menu-item index="1-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title"><i class="el-icon-menu"></i>
              <span slot="title" style="color: #cccccc">导航二</span>
            </template>
            <el-menu-item-group>
              <template slot="title">分组一</template>
              <el-menu-item index="2-1">选项1</el-menu-item>
              <el-menu-item index="2-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="2-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="2-4">
              <template slot="title">选项4</template>
              <el-menu-item index="2-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title"><i class="el-icon-setting"></i>
              <span slot="title" style="color: #cccccc">导航三</span>
            </template>
            <el-menu-item-group>
              <template slot="title">分组一</template>
              <el-menu-item index="3-1">选项1</el-menu-item>
              <el-menu-item index="3-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="3-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="3-4">
              <template slot="title">选项4</template>
              <el-menu-item index="3-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style="font-size: 12px; border-bottom: 1px solid #ccc; line-height: 60px; display: flex">
          <div style="flex: 1; font-size: 18px; display: flex; flex-direction: row; align-items: center">
            <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item><a href="/">活动管理</a></el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <el-dropdown style="width: 50px; cursor: pointer">
            <span>王小虎</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人信息</el-dropdown-item>
              <el-dropdown-item>退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-header>

        <el-main>

          <div style="padding: 10px 0">
            <el-input v-model="cid" style="width: 200px" placeholder="请输入编号" suffix-icon="el-icon-search" class="mr-5"></el-input>
            <el-input v-model="clientName" style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-search" class="mr-5"></el-input>
            <el-input v-model="clientMobile" style="width: 200px" placeholder="请输入电话" suffix-icon="el-icon-search" class="mr-5"></el-input>
            <el-input v-model="clientEmail" style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-search" class="mr-5"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
            <div style="margin-top: 10px">
              <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
              <el-button type="danger" @click="delBatch">批量删除 <i class="el-icon-remove-outline"></i></el-button>
              <el-button type="primary">导入 <i class="el-icon-bottom"></i></el-button>
              <el-button type="primary">导出 <i class="el-icon-top"></i></el-button>
            </div>
          </div>
          <el-table :data="tableData" border stripe header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column prop="cid" label="账号" width="140">
            </el-table-column>
            <el-table-column prop="clientName" label="姓名" width="120">
            </el-table-column>
            <el-table-column prop="clientMobile" label="电话">
            </el-table-column>
            <el-table-column prop="clientEmail" label="邮箱">
            </el-table-column>
            <el-table-column>
              <template slot-scope="scope">
                <el-button type="success" style="margin-left: 61%" @click="handleEdit(scope.row)">编辑</el-button>
                <el-popconfirm
                  class="ml-5"
                  confirm-button-text='确定'
                  cancel-button-text='我再想想'
                  icon="el-icon-info"
                  icon-color="red"
                  title="您确定删除吗？"
                  @confirm="del(scope.row.cid)"
                >
                <el-button type="danger" slot="reference">删除</el-button>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
          <div style="padding: 10px 0">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-sizes="[2, 5, 10, 20]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
            </el-pagination>
          </div>

          <!--          新增用弹窗         -->
          <el-dialog title="用户信息" :visible.sync="saveDialogFormVisible" width="30%">
            <el-form label-width="80px">
              <el-form-item label="姓名">
                <el-input v-model="form.clientName" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="电话">
                <el-input v-model="form.clientMobile" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="form.clientEmail" autocomplete="off"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="saveDialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </div>
          </el-dialog>

          <!--          修改用弹窗         -->
          <el-dialog title="用户信息" :visible.sync="updateDialogFormVisible" width="30%">
            <el-form label-width="80px">
              <el-form-item label="姓名">
                <el-input v-model="form.clientName" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="电话">
                <el-input v-model="form.clientMobile" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="form.clientEmail" autocomplete="off"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="updateDialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="update">确 定</el-button>
            </div>
          </el-dialog>

        </el-main>
      </el-container>
    </el-container>

  </div>
</template>

<script>
// @ is an alias to /src
import request from "@/util/request";
export default {
  name: 'HomeView',
  data(){
    console.log(this)
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 2,
      cid: "",
      clientId: "",
      clientName: "",
      clientMobile: "",
      clientEmail: "",
      multipleSelection: "",
      saveDialogFormVisible: false,
      updateDialogFormVisible: false,
      form: {

      },
      msg: "tmd",
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    collapse() {// 点击收缩按钮触发
      this.isCollapse = !this.isCollapse;
      if(this.isCollapse) {
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      }else {
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    load(){
      request.get("/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          cid: this.cid,
          clientName: this.clientName,
          clientMobile: this.clientMobile,
          clientEmail: this.clientEmail
        }
      })
      .then(res => {
        console.log(res)
        this.tableData = res.records
        this.total = res.total
      })
    },
    // 1、增加
    save() {
      request.post("", this.form).then(res => {
        if (res) {
          this.$message.success("保存成功")
          this.saveDialogFormVisible = false
          this.load()
        } else {
          this.$message.success("保存失败，电话号码已存在")
        }
      })
    },
    // 2、删除
    del(cid) {
      request.delete("/id/" + cid).then(res => {
        if(res) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.success("删除失败")
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.cid)//[{}, {}, {}] => [1,2,3]
      request.post("/del/batch", ids).then(res => {
        if(res) {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.success("批量删除失败")
        }
      })
    },
    // 3、修改
    update() {
      request.post("/update", this.form).then(res => {
        if (res) {
          this.$message.success("编辑成功")
          this.updateDialogFormVisible = false
        } else {
          this.$message.success("编辑失败")
        }
      })
    },
    reset() {
      this.clientId = ""
      this.clientName = ""
      this.clientMobile = ""
      this.clientEmail= ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    handleAdd() {
      this.saveDialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = row
      this.updateDialogFormVisible = true
    }
  }
}
</script>
<style>
.headerBg{
  background: rgb(45, 113, 220) !important;
  color: white;
}
</style>