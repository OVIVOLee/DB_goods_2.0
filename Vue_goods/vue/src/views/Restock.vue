<template>
  <div>

    <div style="padding: 1px 0px 9px 0px;">

      <div>
        <el-input v-model="restockId" style="width: 200px" placeholder="请输入商品编号" suffix-icon="el-icon-search" class="mr-5"></el-input>
        <el-input v-model="goodName" style="width: 200px" placeholder="请输入商品名" suffix-icon="el-icon-search" class="mr-5"></el-input>
        <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
        <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
      </div>

      <div style="margin-top: 10px">
        <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
        <el-button type="danger" @click="delBatch">批量删除 <i class="el-icon-remove-outline"></i></el-button>
        <el-upload action="http://localhost:9090/restock/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
          <el-button class="ml-5" type="primary">导入 <i class="el-icon-bottom"></i></el-button>
        </el-upload>
        <el-button class="ml-5" type="primary" @click="exp">导出 <i class="el-icon-top"></i></el-button>
      </div>

    </div>

    <!--          商品信息         -->
    <el-table :data="tableData" border stripe header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column prop="restockId" label="进货编号" width="140">
      </el-table-column>
      <el-table-column prop="goodId" label="商品编号" width="140">
      </el-table-column>
      <el-table-column prop="goodName" label="商品名">
      </el-table-column>
      <el-table-column prop="restockNum" label="进货数量" width="120">
      </el-table-column>
      <el-table-column prop="restockPrice" label="进货单价">
      </el-table-column>
      <el-table-column prop="restockSum" label="进货总价">
      </el-table-column>
      <el-table-column prop="restockDate" label="进货时间">
      </el-table-column>
      <el-table-column>
        <template slot-scope="scope">
<!--          <el-button type="success" style="margin-left: 17%" @click="handleEdit(scope.row)">编辑</el-button>-->
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.restockId)"
          >
            <el-button type="danger" slot="reference" style="margin-left: 57%">删除</el-button>
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
    <el-dialog title="商品信息" :visible.sync="saveDialogFormVisible" width="30%">
      <el-form label-width="80px">
        <el-form-item label="商品编号">
          <el-input v-model="form.goodId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="进货数量">
          <el-input v-model="form.restockNum" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="进货单价">
          <el-input v-model="form.restockPrice" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="saveDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

<!--    &lt;!&ndash;          修改用弹窗         &ndash;&gt;-->
<!--    <el-dialog title="用户信息" :visible.sync="updateDialogFormVisible" width="30%">-->
<!--      <el-form label-width="80px">-->
<!--        <el-form-item label="商品编号">-->
<!--          <el-input v-model="form.goodId" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="进货数量">-->
<!--          <el-input v-model="form.restockNum" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="进货单价">-->
<!--          <el-input v-model="form.restockPrice" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="进货日期">-->
<!--          <el-input v-model="form.restockDate" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="updateDialogFormVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="update">确 定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->

  </div>
</template>

<script>
import request from "@/util/request";

export default {
  name: "Restock",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      restockId: "",
      goodId: "",
      restockNum: "",
      restockPrice: "",
      restockSum: "",
      restockDate: "",
      goodName: "",
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
      request.get("/restock/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          restockId: this.restockId,
          goodName: this.goodName,
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
      request.post("/restock", this.form).then(res => {
        if (res == 1) {
          this.$message.success("保存成功")
          this.saveDialogFormVisible = false
          this.load()
        }
        else if(res == -1){
          this.$message.error("商品数量错误")
        }
        else if(res == -2){
          this.$message.error("商品价格错误")
        }
        else if(res == -3){
          this.$message.error("该商品不存在")
        }
      })
    },
    // 2、删除
    del(restockId) {
      request.delete("/restock/id/" + restockId).then(res => {
        if(res) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.success("删除失败")
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.restockId)//[{}, {}, {}] => [1,2,3]
      request.post("/restock/del/batch", ids).then(res => {
        if(res) {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    // // 3、修改
    // update() {
    //   request.post("/restock/update", this.form).then(res => {
    //     if (res) {
    //       this.$message.success("编辑成功")
    //       this.updateDialogFormVisible = false
    //       this.load()
    //     } else {
    //       this.$message.error("编辑失败")
    //     }
    //   })
    // },
    reset() {
      this.restockId = ""
      this.goodId = ""
      this.goodName = ""
      this.restockNum = ""
      this.restockPrice= ""
      this.restockSum= ""
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
    exp() {
      window.open("http://localhost:9090/restock/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
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