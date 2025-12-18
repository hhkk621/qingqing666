<script setup>
import {
  Clock,
  Delete,
  Edit,
  Search,
  RefreshRight,
  Plus,
} from '@element-plus/icons-vue'
import {ref, onMounted, nextTick} from "vue";
import api from '@/utils/request.js';
import {formatDay, formatLoginTime} from '@/utils/date.js'
import {ElMessage, ElMessageBox} from "element-plus";

let searchTime = ref(["", ""])
let queryParams = ref({})
let list = ref([])
let isSelectRow = ref(false)
let isAdd = ref(false);
const selectedRows = ref([]);
let resetQueryForm = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    name: '',
    phone: '',
    type: '',
    start: '',
    end: '',
    total: 0
  }
}
const handleSelectionChange = (val) => {
  selectedRows.value = val;
  isSelectRow.value = selectedRows.value.length > 0;
};
let query = () => {
  if (searchTime.value[0] !== '') {
    queryParams.value.start = formatDay(searchTime.value[0])
    queryParams.value.end = formatDay(searchTime.value[1])
  }
  api.get("/admin/role/page", queryParams.value).then(result => {
    list.value = result.data.list;
  })
}
let editFormVisible = ref(false)
let editForm = ref({})
let editTitle = ref('编辑')
let addPage = () => {
  editFormVisible.value = true
  isAdd.value = true
  editTitle.value = "新增角色"
  reset()
}
let edit = (row) => {
  editFormVisible.value = true
  isAdd.value = false
  editTitle.value = "编辑角色"
  editForm = ref(row)
}

/** 取消按钮 */
function cancel() {
  editFormVisible.value = false;
  authMenuFormVisible.value = false;
  reset();
}

function reset() {
  editForm.value = {
    rid: '',
    name: '',
    code: ''
  }
}

function save() {
  api.postJson("/admin/role/saveOrUpdate", editForm.value).then(result => {
    if (result.code === 200) {
      ElMessage.success("保存成功")
      editFormVisible.value = false;
      query()
    } else {
      ElMessage.error(result.msg)
    }
  })

}

const deleteSelectedRows = () => {
  const idsArray = selectedRows.value.map(row => row.uid);

  ElMessageBox.confirm('是否确认删除用户编号为[' + idsArray + ']的数据?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    const ids = selectedRows.value.map(row => row.rid).join(',');
    //ids变成,分割的字符串
    api.delete("/admin/role/delete/" + ids).then(result => {
      if (result.code === 200) {
        ElMessage.success("删除成功")
        query()
      } else {
        ElMessage.error(result.msg)
      }
    })
  }).catch(() => {
    ElMessage.info('已取消删除');
  })

};
let deleteOne = (row) => {
  ElMessageBox.confirm('是否确认删除角色编号为[' + row.rid + ']的数据?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    api.delete("/admin/role/delete/" + row.rid).then(result => {
      if (result.code === 200) {
        ElMessage.success("删除成功")
        query()
      } else {
        ElMessage.error(result.msg)
      }
    })
  }).catch(() => {
    ElMessage.info('已取消删除');
  })
}
let authMenuFormVisible = ref(false)
let authMenu = (row) => {
  authMenuFormVisible.value = true
  editForm = ref(row)
  queryMenuByRid()
}

let menuList = ref([])
let queryAllMenu = () => {
  api.get("/admin/menu/list").then(result => {
    menuList.value = result.data
  })
}
let queryMenuByRid = () => {
  api.get("/admin/menu/list", {"rid": editForm.value.rid}).then(result => {
    const mids = result.data.map(item => item.mid);
    // 延迟执行，确保 tree 已渲染
    nextTick(() => {
      treeRef.value.setCheckedKeys(mids);
    });
  })
}
let treeRef = ref(null)
let doAuthMenu = () => {
  // 获取全选节点key数组（包含半选父节点）
  const checkedKeys = treeRef.value.getCheckedKeys(false) // false表示包含半选节点

  // 获取半选节点key数组（需要单独获取）
  const halfCheckedKeys = treeRef.value.getHalfCheckedKeys()

  // 合并选中节点（根据业务需求选择是否需要合并）
  const allSelectedKeys = [...checkedKeys, ...halfCheckedKeys]
  api.postJson("/admin/role/authMenu", {
    "rid": editForm.value.rid,
    "mids": allSelectedKeys
  }).then(result => {
    if (result.code === 200) {
      ElMessage.success("保存成功")
      authMenuFormVisible.value = false
      query()
    } else {
      ElMessage.error(result.msg)
    }
  })
}
onMounted(() => {
  resetQueryForm();
  query();
  queryAllMenu()
})
</script>

<template>
  <div class="app-container">
    <el-row>
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="ID">
          <el-input v-model="queryParams.id" clearable/>
        </el-form-item>
        <el-form-item label="角色名/权限标识">
          <el-input v-model="queryParams.name" clearable/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" @click="query">查询</el-button>
          <el-button type="primary" :icon="RefreshRight" @click="resetQueryForm">重置</el-button>
        </el-form-item>
      </el-form>

    </el-row>
    <el-row>
      <el-col>
        <el-button type="success" :icon="Plus" plain @click="addPage">新增</el-button>
        <el-button type="danger" :icon="Delete" plain @click="deleteSelectedRows" :disabled="!isSelectRow">删除
        </el-button>

      </el-col>
    </el-row>
    <el-row>
      <el-table
          :data="list"
          border
          stripe
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"/>
        <el-table-column
            prop="rid"
            label="ID"
            width="80"
        />
        <el-table-column
            prop="name"
            label="角色名称"
        />
        <el-table-column
            prop="code"
            label="权限标识"
        />
        <el-table-column label="操作" default="scope">
          <template #default="scope">
            <el-button type="primary" size="small" @click="edit(scope.row)" v-if="scope.row.rid!==1">修改</el-button>
            <el-button type="danger" size="small" @click="deleteOne(scope.row)" v-if="scope.row.rid!==1">删除
            </el-button>
            <el-button type="warning" size="small" @click="authMenu(scope.row)" v-if="scope.row.rid!==1">分配菜单
            </el-button>
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

  <!-- 添加角色信息 -->
  <el-dialog v-model="editFormVisible" :title="editTitle" width="600px" append-to-body>
    <el-form :model="editForm" ref="editFormRef" label-width="80px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="角色名" prop="name">
            <el-input v-model="editForm.name" placeholder="请输入角色名" maxlength="30"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="权限标识" prop="code">
            <el-input v-model="editForm.code" placeholder="请输入权限标识" maxlength="30"/>
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

  <!-- 分配菜单信息 -->
  <el-dialog v-model="authMenuFormVisible" title="分配菜单" width="600px" append-to-body>
    <el-form :model="editForm" ref="editFormRef" label-width="80px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="角色名" prop="name">
            <el-input v-model="editForm.name" maxlength="30" disabled/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="权限标识" prop="code">
            <el-input v-model="editForm.code" maxlength="30" disabled/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="权限标识" prop="code">
            <el-tree
                ref="treeRef"
                style="max-width: 600px"
                :data="menuList"
                show-checkbox
                node-key="mid"
                default-expand-all
                :props="{
                      children: 'childList',
                      label: 'name',
                }"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="doAuthMenu">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>

</style>