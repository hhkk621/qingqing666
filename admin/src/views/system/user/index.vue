<script setup>
import {
  Clock,
  Delete,
  Edit,
  Search,
  RefreshRight,
  Plus,
} from '@element-plus/icons-vue'
import {ref,onMounted} from "vue";
import api from '@/utils/request.js';
import {formatDay, formatLoginTime} from '@/utils/date.js'
import {ElMessage, ElMessageBox} from "element-plus";
import loginUser from "@/stores/LoginUser.js";
let searchTime = ref(["",""])
let queryParams =ref({})
let userList = ref([])
let isSelectRow = ref(false)
let isAdd= ref(false);
const tableRef = ref(null);
const selectedRows = ref([]);
let resetQueryForm=()=>{
  queryParams.value = {
    pageNum:1,
    pageSize:10,
    name:'',
    phone:'',
    type:'',
    start:'',
    end:'',
    total:0
  }
}
let query = ()=>{
  if (searchTime.value[0]!==''){
    queryParams.value.start = formatDay(searchTime.value[0])
    queryParams.value.end = formatDay(searchTime.value[1])
  }
  api.get("/admin/user/page",queryParams.value).then(result=>{
    if (result.code === 200){
      userList.value = result.data.list;
      queryParams.value.total = result.data.total;
    }else {
      ElMessage.error(result.msg)
    }
  })

}
let switchHandler = (row)=>{
  ElMessageBox.confirm('是否修改用户['+row. nickname+']的状态？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    if (row.isvalid == 1){
      row.isvalid = '0'
    }else {
      row.isvalid = '1'
    }
  }).catch(() => {
    ElMessage.info('已取消修改');
  })

}
let editFormVisible = ref(false)
let editForm = ref({
  password : 123456
})
let editTitle = ref('编辑')
let addPage = ()=>{
  editFormVisible.value = true
  isAdd.value = true
  editTitle.value = "新增用户"
  reset()
}
let editFormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  nickname: [
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 3, max: 12, message: '长度在 3 到 12 个字符', trigger: 'blur' }
  ]
}
let edit = (row)=>{
  console.log(row)
  editFormVisible.value = true
  isAdd.value = false
  editTitle.value = "编辑用户"
  editForm = ref(row)
  editForm.value.password = ''
  console.log(editForm.value.isvalid)
  editForm.value.isvalid = String(editForm.value.isvalid)
}
/** 取消按钮 */
function cancel() {
  editFormVisible.value = false;
  reset();
}
function reset(){
  editForm.value ={
    uid: '',
    username: '',
    nickname: '',
    phone: '',
    isvalid: '1',
    password: '123456'
  }
}
function save() {
  delete editForm.value.loginTime;
  delete editForm.value.regTime;
  delete editForm.value.role;
  api.post("/admin/user/save/update",editForm.value).then(result=>{
      if (result.code === 200){
        ElMessage.success("更新成功")
        cancel();
        query();
      }else {
        ElMessage.error(result.msg)
      }
  })

}

const handleSelectionChange = (val) => {
  selectedRows.value = val;
  isSelectRow.value = selectedRows.value.length > 0;
};
const deleteSelectedRows = () => {
  const idsArray = selectedRows.value.map(row => row.uid);

  ElMessageBox.confirm('是否确认删除用户编号为['+idsArray+']的数据?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    //ids变成,分割的字符串
    const ids = selectedRows.value.map(row => row.uid).join(',');
    api.delete("/admin/user/del/"+ids).then(result=>{
      if (result.code === 200){
        ElMessage.success("删除成功")
        query();
      }else {
        ElMessage.error(result.msg)
      }
    })
  }).catch(() => {
    ElMessage.info('已取消删除');
  })

};
let deleteOne = (row)=>{
  ElMessageBox.confirm('是否确认删除用户编号为['+row.uid+']的数据?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    api.delete("/admin/user/del/"+row.uid).then(result=>{
      if (result.code === 200){
        ElMessage.success("删除成功")
        query();
      }else {
        ElMessage.error(result.msg)
      }
    })
  }).catch(() => {
    ElMessage.info('已取消删除');
  })
 }
 let roleList = ref([])
 function queryRoleList(){
  api.get("/admin/role/all").then(result=>{
    if (result.code === 200){
      roleList.value = result.data;
    }else {
      ElMessage.error(result.msg)
    }
  })
 }
 function resetPassword (row) {
   const idsArray = selectedRows.value.map(row => row.uid);
  ElMessageBox.confirm('是否为ID为['+idsArray+']的用户重置密码为[123456]?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(({ value }) => {
    const ids = selectedRows.value.map(row => row.uid).join(',');

  })
 }

onMounted(()=>{
  resetQueryForm();
  queryRoleList();
  query();
})




</script>

<template>
  <div class="app-container">
    <el-row>
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="ID/昵称/用户名">
          <el-input v-model="queryParams.name"  clearable />
        </el-form-item>
        <el-form-item label="手机号/邮箱">
          <el-input v-model="queryParams.phone"  clearable />
        </el-form-item>
        <el-form-item label="角色" >
          <el-select v-model="queryParams.id" placeholder="请选择角色" maxlength="30">
            <el-option
                v-for="role in roleList"
                :key="role.rid"
                :label="role.name"
                :value="role.rid"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
              v-model="queryParams.type"
              placeholder="状态"
              clearable
          >
            <el-option label="封停" value="0" />
            <el-option label="正常" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
              v-model="searchTime"
              type="daterange"
              unlink-panels
              range-separator="到"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              clearable
          />
        </el-form-item>
        <el-form-item >
          <el-button type="primary" :icon="Search" @click="query">查询</el-button>
          <el-button type="primary" :icon="RefreshRight" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>

    </el-row>
    <el-row>
      <el-col>

        <el-button  type="success" :icon="Plus" plain @click="addPage">新增</el-button>
        <el-button type="danger" :icon="Delete" plain @click="deleteSelectedRows" :disabled="!isSelectRow">删除</el-button>
        <el-button type="warning" :icon="Clock" plain @click="resetPassword" :disabled="!isSelectRow">重置密码</el-button>

      </el-col>
    </el-row>
    <el-row >
      <el-table
          :data="userList"
          border
          stripe
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
            prop="uid"
            label="ID"
            width="80"
        />
        <el-table-column
            prop="username"
            label="用户名"
            width="180"
        />
        <el-table-column
            prop="nickname"
            label="昵称"
            width="180"
        />
        <el-table-column
            prop="phone"
            label="手机号"
        />
        <el-table-column
            label="状态"
        >
          <template #default="scope">
            <!--status必须是String类型,否则switch组件不识别-->
            <el-switch
                :model-value="String(scope.row.isvalid)"
                inline-prompt
                active-value="1"
                active-text="正常"
                inactive-value="0"
                inactive-text="停用"
                class="ml-2"
                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                @change="switchHandler(scope.row)"
            />
          </template>

        </el-table-column>
        <el-table-column label="创建时间" >
          <template #default="scope">
            {{ formatDay(scope.row.regTime) }}
          </template>
        </el-table-column>
        <el-table-column label="最后登录时间" >
          <template #default="scope">
            {{ formatLoginTime(scope.row.loginTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" default="scope">
          <template #default="scope">
            <el-button type="primary" size="small" @click="edit(scope.row)">修改</el-button>
            <el-button type="danger" size="small" @click="deleteOne(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-row>
      <el-col :span="24">

        <el-pagination
            v-model:current-page="queryParams.pageNum"
            v-model:page-size="queryParams.pageSize"
            :page-sizes="[5, 10, 20, 50]"
            :background="true"
            layout="total, sizes, prev, pager, next, jumper"
            :total="queryParams.total"
            @size-change="query"
            @current-change="query"
        />
      </el-col>
    </el-row>
  </div>
  <!-- 添加用户信息 -->
  <el-dialog v-model="editFormVisible" :title="editTitle" width="600px" append-to-body>
    <el-form :model="editForm" :rules="editFormRules" ref="editFormRef"  label-width="80px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="editForm.username" placeholder="请输入用户名" maxlength="30" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="密码" prop="password">
            <el-input v-model="editForm.password" placeholder="请输入密码" maxlength="30" show-password/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="editForm.phone" placeholder="手机号" maxlength="30" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="editForm.email" placeholder="邮箱" maxlength="30" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" v-if="!isAdd">
          <el-form-item label="昵称" prop="nickname">
            <el-input v-model="editForm.nickname" placeholder="请输入用户昵称" maxlength="30" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="isvalid">
            <el-radio-group v-model="editForm.isvalid" >
              <el-radio-button label="正常" value="1" />
              <el-radio-button label="关闭" value="0" />
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="岗位" prop="phone">
            <el-input  placeholder="岗位" maxlength="30" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="角色" prop="roleId">
            <el-select v-model="editForm.roleId" placeholder="请选择角色" maxlength="30">
              <el-option
                  v-for="role in roleList"
                  :key="role.rid"
                  :label="role.name"
                  :value="role.rid"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="nickname">
            <el-input v-model="editForm.remark" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="save">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>


</style>