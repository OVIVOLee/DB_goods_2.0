<template>
  <div>

    <div style="padding: 1px 0px 9px 0px;">

      <div>
        <el-input v-model="cid" style="width: 200px" placeholder="请输入编号" suffix-icon="el-icon-search" class="mr-5"></el-input>
        <el-input v-model="clientName" style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-search" class="mr-5"></el-input>
        <el-input v-model="clientMobile" style="width: 200px" placeholder="请输入电话" suffix-icon="el-icon-search" class="mr-5"></el-input>
        <el-input v-model="clientEmail" style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-search" class="mr-5"></el-input>
        <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
        <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
      </div>

      <div style="margin-top: 10px">
        <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
        <el-button type="danger" @click="delBatch">批量删除 <i class="el-icon-remove-outline"></i></el-button>
        <el-upload action="http://localhost:9090/client/import">
            <el-button type="primary">导入 <i class="el-icon-bottom"></i></el-button>
        </el-upload>
        <el-button type="primary" @click="exp">导出 <i class="el-icon-top"></i></el-button>
      </div>

    </div>

    <!--          用户信息         -->
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

    <!--          页数设置         -->
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

  </div>
</template>

<script>
import request from "@/util/request";

export default {
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      cid: "",
      clientName: "",
      clientMobile: "",
      clientEmail: "",
      multipleSelection: [],
      saveDialogFormVisible: false,
      updateDialogFormVisible: false,
      form: {},
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      request.get("/client/page",{
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
      if(isFinite(this.form.clientMobile))
      {
        request.post("/client", this.form).then(res => {
          if (res ==  1) {
            this.$message.success("保存成功")
            this.saveDialogFormVisible = false
            this.load()
          }
          else if(res ==  -1) {
            this.$message.error("请输入姓名")
          }
          else if(res ==  -2) {
            this.$message.error("该客户已存在")
          }
          else if(res ==  -3) {
            this.$message.error("电话号码具有唯一性")
          }
        })
      } else {
        this.$message.error("请输入正确的电话号码")
      }

    },
    // 2、删除
    del(cid) {
      request.delete("/client/id/" + cid).then(res => {
        if(res) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.success("删除失败")
        }
      })
    },
    delBatch() {//批量删除
      let ids = this.multipleSelection.map(v => v.cid)//[{}, {}, {}] => [1,2,3]
      request.post("/client/del/batch", ids).then(res => {
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
      if(this.isNumeric(this.form.clientMobile)){
        request.post("/client/update", this.form).then(res => {
          if (res) {
            this.$message.success("编辑成功")
            this.updateDialogFormVisible = false
          } else {
            this.$message.error("编辑失败")
          }
        })
      } else {
        this.$message.error("电话号码只能为数字")
      }
    },
    // 重置
    reset() {
      this.cid = ""
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
    },
    isNumeric(str) {
      return /^\d+$/.test(str);
    },
    exp() {
      window.open("/client/export")
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