<template>
  <div class="app-container">
    <!-- 头部操作区域 -->
    <el-row class="mb-3">
      <el-col>
        <el-button type="success" :icon="Plus" plain @click="addPage">新增</el-button>
        <el-button type="danger" :icon="Delete" plain @click="deleteSelectedRows" :disabled="!isSelectRow">删除
        </el-button>
        <el-button type="primary" :icon="RefreshRight" plain @click="resetQueryForm">重置</el-button>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="mb-3" shadow="never">
      <el-form :model="queryParams" label-width="80px" :inline="true">
        <el-form-item label="品牌名称">
          <el-input
              v-model="queryParams.brandName"
              placeholder="请输入品牌名称"
              clearable
              style="width: 200px"
              @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="品牌网址">
          <el-input
              v-model="queryParams.brandUrl"
              placeholder="请输入品牌网址"
              clearable
              style="width: 200px"
              @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <el-table
          :data="list"
          border
          stripe
          @selection-change="handleSelectionChange"
          v-loading="loading"
      >
        <el-table-column type="selection" width="55"/>

        <el-table-column
            prop="id"
            label="ID"
            width="80"
        />

        <el-table-column
            prop="brandName"
            label="品牌名称"
            width="150"
        >
          <template #default="scope">
            <span class="brand-name">{{ scope.row.brandName }}</span>
          </template>
        </el-table-column>

        <el-table-column
            prop="brandUrl"
            label="品牌网址"
        >
          <template #default="scope">
            <el-link
                :href="'http://' + scope.row.brandUrl"
                target="_blank"
                type="primary"
            >
              {{ scope.row.brandUrl }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column
            prop="brandDescribe"
            label="品牌描述"
        />

        <el-table-column label="操作" default="scope" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="edit(scope.row)">修改</el-button>
            <el-button type="danger" size="small" @click="deleteOne(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页 -->
    <el-row class="mt-3">
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

    <!-- 添加/编辑品牌对话框 -->
    <el-dialog v-model="editFormVisible" :title="editTitle" width="600px" append-to-body>
      <el-form :model="editForm" ref="editFormRef" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="品牌名称" prop="brandName" required>
              <el-input
                  v-model="editForm.brandName"
                  placeholder="请输入品牌名称"
                  maxlength="50"
                  show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="品牌网址" prop="brandUrl" required>
              <el-input
                  v-model="editForm.brandUrl"
                  placeholder="请输入品牌网址"
                  maxlength="100"
                  show-word-limit
                  @blur="formatUrl"
              />
              <div class="input-tip">例如：www.example.com 或 example.com</div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="品牌描述" prop="brandDescribe">
              <el-input
                  v-model="editForm.brandDescribe"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入品牌描述"
                  maxlength="200"
                  show-word-limit
              />
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
  </div>
</template>

<script setup>
import {
  Delete,
  Search,
  RefreshRight,
  Plus,
  Edit
} from '@element-plus/icons-vue'
import { ref, onMounted } from "vue";
import api from '@/utils/request.js';
import { ElMessage, ElMessageBox } from "element-plus";

// 搜索查询参数
let queryParams = ref({})
let list = ref([])
let loading = ref(false)
let isSelectRow = ref(false)
const selectedRows = ref([])

// 编辑表单相关
let editFormVisible = ref(false)
let editForm = ref({})
let editTitle = ref('编辑')
let isAdd = ref(false)

// 初始化查询参数
let resetQueryForm = () => {
  queryParams.value = {
    currentPage: 1,
    pageSize: 10,
    brandName: '',
    brandUrl: '',
    total: 0
  }
  query()
}

// 表格选择变化
const handleSelectionChange = (val) => {
  selectedRows.value = val;
  isSelectRow.value = selectedRows.value.length > 0;
};

// 查询数据
let query = () => {
  loading.value = true
  // 构建请求参数
  const params = {
    pageNum: queryParams.value.currentPage,
    pageSize: queryParams.value.pageSize,
    ...queryParams.value
  }
  // 删除不需要的参数
  delete params.currentPage
  delete params.pageSize
  delete params.total

  api.get("/gg/brand/list", params).then(result => {
    if (result.code === 200) {
      list.value = result.data.list || []
      queryParams.value.total = result.data.total || 0
    } else {
      ElMessage.error(result.msg)
    }
  }).finally(() => {
    loading.value = false
  })
}

// 搜索
let handleSearch = () => {
  queryParams.value.currentPage = 1
  query()
}

// 新增
let addPage = () => {
  editFormVisible.value = true
  isAdd.value = true
  editTitle.value = "新增品牌"
  reset()
}

// 编辑
let edit = (row) => {
  editFormVisible.value = true
  isAdd.value = false
  editTitle.value = "编辑品牌"
  editForm.value = { ...row }
}

// 取消编辑
function cancel() {
  editFormVisible.value = false;
  reset();
}

// 重置表单
function reset() {
  editForm.value = {
    brandName: '',
    brandUrl: '',
    brandDescribe: ''
  }
}

// 格式化网址
function formatUrl() {
  if (editForm.value.brandUrl) {
    // 移除http://或https://前缀
    let url = editForm.value.brandUrl.replace(/^https?:\/\//, '')
    // 移除末尾的斜杠
    url = url.replace(/\/$/, '')
    editForm.value.brandUrl = url
  }
}

// 保存
function save() {
  // 基本验证
  if (!editForm.value.brandName || editForm.value.brandName.trim() === '') {
    ElMessage.error("品牌名称不能为空")
    return
  }

  if (!editForm.value.brandUrl || editForm.value.brandUrl.trim() === '') {
    ElMessage.error("品牌网址不能为空")
    return
  }

  // 验证网址格式
  const urlPattern = /^[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/
  if (!urlPattern.test(editForm.value.brandUrl)) {
    ElMessage.error("请输入有效的网址，例如：www.example.com")
    return
  }

  if (isAdd.value) {
    // 先检查品牌名称是否已存在
    api.get("/gg/brand/check/name", { brandName: editForm.value.brandName }).then(checkResult => {
      if (checkResult.code === 200 && checkResult.data) {
        ElMessage.error("品牌名称已存在")
        return
      }

      // 新增品牌
      api.postJson("/gg/brand", editForm.value).then(result => {
        if (result.code === 200) {
          ElMessage.success("新增成功")
          editFormVisible.value = false;
          query()
        } else {
          ElMessage.error(result.msg)
        }
      })
    }).catch(error => {
      ElMessage.error("检查品牌名称失败")
    })
  } else {
    // 编辑品牌
    api.put(`/gg/brand/${editForm.value.id}`, editForm.value).then(result => {
      if (result.code === 200) {
        ElMessage.success("修改成功")
        editFormVisible.value = false;
        query()
      } else {
        ElMessage.error(result.msg)
      }
    })
  }
}

// 批量删除
const deleteSelectedRows = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请先选择要删除的数据")
    return
  }

  const idsArray = selectedRows.value.map(row => row.id);

  ElMessageBox.confirm('是否确认删除选中的' + idsArray.length + '条数据?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    api.postJson("/gg/brand/batch/delete", idsArray).then(result => {
      if (result.code === 200) {
        ElMessage.success("批量删除成功")
        query()
        selectedRows.value = []
      } else {
        ElMessage.error(result.msg)
      }
    })
  }).catch(() => {
    ElMessage.info('已取消删除');
  })
}

// 删除单个
let deleteOne = (row) => {
  ElMessageBox.confirm('是否确认删除品牌[' + row.brandName + ']的数据?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    api.delete(`/gg/brand/${row.id}`).then(result => {
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

// 页面加载
onMounted(() => {
  resetQueryForm();
  query();
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.mb-3 {
  margin-bottom: 20px;
}

.mt-3 {
  margin-top: 20px;
}

.brand-name {
  font-weight: bold;
  color: #333;
}

.input-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style>