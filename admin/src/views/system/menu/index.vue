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
// 在icon-select组件中
import IconSelect from "@/components/IconSelect/index.vue";
let searchTime = ref(["",""])
let queryParams =ref({})
let list = ref([])
let optionList = ref([])
let isSelectRow = ref(false)
let isAdd= ref(false);
const selectedRows = ref([]);
let resetQueryForm=()=>{
  queryParams.value = {
    currentPage:1,
    pageSize:10,
    name:'',
    phone:'',
    type:'',
    start:'',
    end:'',
    total:0
  }
}
const handleSelectionChange = (val) => {
  selectedRows.value = val;
  isSelectRow.value = selectedRows.value.length > 0;
};
let query = ()=>{
  if (searchTime.value[0]!==''){
    queryParams.value.start = formatDay(searchTime.value[0])
    queryParams.value.end = formatDay(searchTime.value[1])
  }
  list.value = [
    {
      "mid": 1,
      "name": "系统管理",
      "url": "/system",
      "pid": -1,
      "icon": "Setting",
      "status": 1,
      "sort": 1,
      "childList": [
        {
          "mid": 1001,
          "name": "用户管理",
          "url": "/user/list",
          "pid": 1,
          "icon": "User",
          "status": 1,
          "sort": 11,
          "childList": null
        },
        {
          "mid": 1002,
          "name": "角色管理",
          "url": "/role/list",
          "pid": 1,
          "icon": "UserFilled",
          "status": 1,
          "sort": 12,
          "childList": null
        },
        {
          "mid": 1003,
          "name": "菜单管理",
          "url": "/menu/list",
          "pid": 1,
          "icon": "Menu",
          "status": 1,
          "sort": 13,
          "childList": null
        }
      ]
    },
    {
      "mid": 2,
      "name": "Vip管理",
      "url": "/vip",
      "pid": -1,
      "icon": "ChromeFilled",
      "status": 1,
      "sort": 2,
      "childList": [
        {
          "mid": 2001,
          "name": "会员管理",
          "url": "/vip/customer",
          "pid": 2,
          "icon": "HelpFilled",
          "status": 1,
          "sort": 21,
          "childList": null
        }
      ]
    }
  ];
  //list.value是全部菜单，从中筛选出1级菜单
  let oneList = [];
  list.value.forEach(item=>{
    let menu = {
      name: item.name,
      mid: item.mid,
      childList: []
    }
    oneList.push(menu)
  })
  optionList.value = [{
    name: '无',
    mid: '-1',
    childList:[...oneList]
  }]
}
let editFormVisible = ref(false)
let editForm = ref({
})
let editTitle = ref('编辑')
let addPage = ()=>{
  editFormVisible.value = true
  isAdd.value = true
  editTitle.value = "新增菜单"
  reset()
  // 获取最大sort值
  let maxSort = findMaxSort(list.value)
  editForm.value.sort = maxSort + 1 // 默认新排序为最大+1
}
// 递归查找最大sort的函数
function findMaxSort(menuList) {
  let max = 0
  menuList.forEach(menu => {
    if (menu.sort > max) {
      max = menu.sort
    }
    if (menu.childList && menu.childList.length > 0) {
      const childMax = findMaxSort(menu.childList)
      max = Math.max(max, childMax)
    }
  })
  return max
}
let edit = (row)=>{
  editFormVisible.value = true
  isAdd.value = false
  editTitle.value = "编辑菜单"
  editForm = ref(row)
  editForm.value.status = String(editForm.value.status)
}
/** 取消按钮 */
function cancel() {
  editFormVisible.value = false;
  reset();
}
function reset(){
  editForm.value ={
    status:"1"
  }
}
function save() {


}
const deleteSelectedRows = () => {
  const idsArray = selectedRows.value.map(row => row.mid);

  ElMessageBox.confirm('是否确认删除菜单编号为['+idsArray+']的数据?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    const ids = selectedRows.value.map(row => row.mid).join(',');

  }).catch(() => {
    ElMessage.info('已取消删除');
  })

};
let deleteOne = (row)=>{
  ElMessageBox.confirm('是否确认删除角色编号为['+row.mid+']的数据?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {

  }).catch(() => {
    ElMessage.info('已取消删除');
  })
}
function switchHandler(row){
  ElMessageBox.confirm('是否修改菜单['+row. name+']的状态？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    if (row.status == '1'){
      row.status = '0'
    }else {
      row.status = '1'
    }

  }).catch(() => {
    ElMessage.info('已取消删除');
  })

}

const iconSelectRef = ref(null);
/** 展示下拉图标 */
function showSelectIcon() {
  iconSelectRef.value.reset();
}

/** 选择图标 */
function selected(name) {
  editForm.value.icon = name;
}
onMounted(()=>{
  resetQueryForm();
  query();
})
function searchMenu(){
  //筛选出 list中，name值中包含输入框中输入的值
  list.value = list.value.filter(item=>{
    return item.name.includes(queryParams.name)
  })
}
</script>

<template>
  <div class="app-container">
    <el-row>
      <el-col>
        <el-button type="success" :icon="Plus" plain @click="addPage">新增</el-button>
        <el-button type="danger" :icon="Delete" plain @click="deleteSelectedRows" :disabled="!isSelectRow">删除</el-button>
      </el-col>
    </el-row>
    <el-row >
      <el-table
          :data="list"
          border
          stripe
          row-key="mid"
          @selection-change="handleSelectionChange"
          :tree-props="{ children: 'childList', hasChildren: 'true' }"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
            prop="mid"
            label="ID"
            width="80"

        />
        <el-table-column
            prop="name"
            label="菜单名称"
            width="180"
        />
        <el-table-column
            prop="sort"
            label="排序"
            width="80"

        />
        <el-table-column
            label="图标"
            width="180"
        >
          <template #default="scope">
            <el-icon v-if="scope.row.icon">
              <component :is="scope.row.icon" />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column
            prop="url"
            label="网址"
        />
        <el-table-column
            label="状态"
        >
          <template #default="scope">
            <!--status必须是String类型,否则switch组件不识别-->
            <el-switch
                :model-value="String(scope.row.status)"
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
        <el-table-column label="操作" default="scope">
          <template #default="scope" >
            <el-button type="primary" size="small" @click="edit(scope.row)">修改</el-button>
<!--            删除单条时，如果删除的是一级菜单，还要删除其他二级菜单 -->
<!--            <el-button type="danger" size="small" @click="deleteOne(scope.row)">删除</el-button>-->
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-row>
      <el-col :span="24">

        <el-pagination
            v-model:current-page="queryParams.currentPage"
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

  <!-- 添加菜单信息 -->
  <el-dialog v-model="editFormVisible" :title="editTitle" width="600px" append-to-body>
    <el-form :model="editForm"  ref="editFormRef"  label-width="80px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="父级菜单" prop="pid">
            <el-tree-select
                v-model="editForm.pid"
                :data="optionList"
                :props="{ value: 'mid', label: 'name', children: 'childList' }"
                value-key="mid"
                placeholder="选择上级菜单"
                check-strictly
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="菜单图标" prop="icon">
            <el-popover
                placement="bottom-start"
                :width="540"
                trigger="click"
            >
              <template #reference>
                <el-input v-model="editForm.icon" placeholder="点击选择图标" @blur="showSelectIcon" readonly>
                  <template #prefix>
                    <svg-icon
                        v-if="editForm.icon"
                        :icon-class="editForm.icon"
                        class="el-input__icon"
                        style="height: 32px;width: 16px;"
                    />
                    <el-icon v-else style="height: 32px;width: 16px;"><search /></el-icon>
                  </template>
                </el-input>
              </template>
              <icon-select ref="iconSelectRef" @selected="selected" :active-icon="editForm.icon" />
            </el-popover>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="菜单名称" prop="name">
            <el-input v-model="editForm.name" placeholder="请输入菜单名" maxlength="30" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="菜单地址" prop="url">
            <el-input v-model="editForm.url" placeholder="请输入地址" maxlength="30" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="显示排序" prop="sort">
            <el-input-number v-model="editForm.sort" controls-position="right" :min="0" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="editForm.status" >
              <el-radio-button label="正常" value="1" />
              <el-radio-button label="关闭" value="0" />
            </el-radio-group>
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