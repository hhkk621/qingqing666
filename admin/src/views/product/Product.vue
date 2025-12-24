<template>
  <div class="app-container">
    <!-- 头部操作区域 -->
    <el-row class="mb-3">
      <el-col>
        <el-button type="primary" :icon="Plus" plain @click="handleAdd">新增</el-button>
        <el-button type="danger" :icon="Delete" plain @click="deleteSelectedRows" :disabled="!isSelectRow">删除</el-button>
        <el-button type="success" :icon="Upload" plain @click="showImportDialog = true">导入Excel</el-button>
        <el-button type="info" :icon="Download" plain @click="downloadTemplate">下载模板</el-button>
        <el-button type="warning" :icon="RefreshRight" plain @click="resetQueryForm">重置</el-button>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="mb-3" shadow="never">
      <el-form :model="queryParams" label-width="80px" :inline="true">
        <el-form-item label="商品编码">
          <el-input
              v-model="queryParams.coding"
              placeholder="请输入商品编码"
              clearable
              style="width: 200px"
              @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="品牌">
          <el-input
              v-model="queryParams.brand"
              placeholder="请输入品牌"
              clearable
              style="width: 200px"
              @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="分类">
          <el-select
              v-model="queryParams.classify"
              placeholder="请选择分类"
              clearable
              style="width: 200px"
          >
            <el-option label="5G手机" value="5G手机" />
            <el-option label="充电线" value="充电线" />
            <el-option label="充电头" value="充电头" />
            <el-option label="耳机" value="耳机" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>

        <el-form-item label="采购模式">
          <el-select
              v-model="queryParams.purchasingPattern"
              placeholder="请选择采购模式"
              clearable
              style="width: 200px"
          >
            <el-option label="集团采购" value="集团采购" />
            <el-option label="个人采购" value="个人采购" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <el-table
          :data="tableData"
          border
          stripe
          @selection-change="handleSelectionChange"
          v-loading="loading"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="coding" label="商品编码" width="150">
          <template #default="scope">
            <el-tag type="primary">{{ scope.row.coding }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="classify" label="分类" width="120" />
        <el-table-column prop="brand" label="品牌" width="120" />
        <el-table-column prop="model" label="型号" width="180" />
        <el-table-column prop="business" label="业务" width="100">
          <template #default="scope">
            <span :class="scope.row.business === '无' ? 'text-gray' : ''">
              {{ scope.row.business }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="color" label="颜色" width="100">
          <template #default="scope">
            <el-tag :type="getColorType(scope.row.color)" size="small">
              {{ getColorText(scope.row.color) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="purchasingPattern" label="采购模式" width="120" />
        <el-table-column prop="distribution" label="铺货" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.distribution === '铺货' ? 'success' : 'info'" size="small">
              {{ scope.row.distribution }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="standby" label="备机" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.standby === '备机' ? 'warning' : 'info'" size="small">
              {{ scope.row.standby }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDay(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" :icon="Edit" @click="handleEdit(scope.row)">修改</el-button>
            <el-button type="danger" size="small" :icon="Delete" @click="deleteOne(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
          v-model:current-page="queryParams.currentPage"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="queryParams.total"
          @size-change="query"
          @current-change="query"
          class="mt-3"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="editFormVisible" :title="editTitle" width="600px" append-to-body>
      <el-form :model="editForm" ref="editFormRef" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="商品编码" prop="coding" required>
              <el-input v-model="editForm.coding" placeholder="请输入商品编码" maxlength="64" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="分类" prop="classify" required>
              <el-select v-model="editForm.classify" placeholder="请选择分类" style="width: 100%">
                <el-option label="5G手机" value="5G手机" />
                <el-option label="充电线" value="充电线" />
                <el-option label="充电头" value="充电头" />
                <el-option label="耳机" value="耳机" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="品牌" prop="brand" required>
              <el-input v-model="editForm.brand" placeholder="请输入品牌" maxlength="32" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="型号" prop="model" required>
              <el-input v-model="editForm.model" placeholder="请输入型号" maxlength="128" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务信息">
              <el-input v-model="editForm.business" placeholder="请输入业务信息" maxlength="255" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="颜色">
              <el-select v-model="editForm.color" placeholder="请选择颜色" style="width: 100%">
                <el-option label="空" :value="0" />
                <el-option label="黑色" :value="1" />
                <el-option label="白色" :value="2" />
                <el-option label="其他" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购模式">
              <el-select v-model="editForm.purchasingPattern" placeholder="请选择采购模式" style="width: 100%">
                <el-option label="集团采购" value="集团采购" />
                <el-option label="个人采购" value="个人采购" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="铺货信息">
              <el-radio-group v-model="editForm.distribution">
                <el-radio label="铺货">铺货</el-radio>
                <el-radio label="非铺货">非铺货</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否备机">
              <el-radio-group v-model="editForm.standby">
                <el-radio label="备机">备机</el-radio>
                <el-radio label="非备机">非备机</el-radio>
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

    <!-- 导入Excel对话框 -->
    <el-dialog v-model="showImportDialog" title="导入Excel" width="500px" append-to-body>
      <el-upload
          class="upload-demo"
          drag
          :before-upload="handleBeforeUpload"
          :show-file-list="false"
          accept=".xlsx,.xls"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">只能上传 .xlsx, .xls 格式的文件，且不超过10MB</div>
        </template>
      </el-upload>

      <div v-if="importResult" class="mt-3">
        <el-alert :title="importResult.title" :type="importResult.type" :closable="false" show-icon>
          <template #default>
            <div v-html="importResult.message"></div>
            <div v-if="importResult.errors" class="error-details">
              <h4>错误详情：</h4>
              <pre>{{ importResult.errors }}</pre>
            </div>
          </template>
        </el-alert>
      </div>

      <div class="mt-3">
        <el-button type="text" @click="downloadTemplate">
          <el-icon><Download /></el-icon>下载导入模板
        </el-button>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showImportDialog = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {
  Delete,
  Edit,
  Search,
  RefreshRight,
  Plus,
  Upload,
  Download,
  UploadFilled
} from '@element-plus/icons-vue'
import { ref, onMounted } from "vue";
import api from '@/utils/request.js';
import { formatDay } from '@/utils/date.js'
import { ElMessage, ElMessageBox } from "element-plus";

// 响应式数据
let queryParams = ref({})
let tableData = ref([])
let loading = ref(false)
let isSelectRow = ref(false)
const selectedRows = ref([])
let editFormVisible = ref(false)
let editForm = ref({})
let editTitle = ref('编辑')
let isAdd = ref(false)
let showImportDialog = ref(false)
let importResult = ref(null)

// 颜色映射
const colorMap = {
  0: { text: '空', type: 'info' },
  1: { text: '黑色', type: '' },
  2: { text: '白色', type: 'success' },
  3: { text: '其他', type: 'warning' }
}

// 初始化查询参数
let resetQueryForm = () => {
  queryParams.value = {
    currentPage: 1,
    pageSize: 10,
    coding: '',
    brand: '',
    classify: '',
    purchasingPattern: '',
    total: 0
  }
}

// 获取颜色文本
const getColorText = (color) => {
  return colorMap[color]?.text || '未知'
}

// 获取颜色类型
const getColorType = (color) => {
  return colorMap[color]?.type || 'info'
}

// 表格选择变化
const handleSelectionChange = (val) => {
  selectedRows.value = val;
  isSelectRow.value = selectedRows.value.length > 0;
};

// 查询数据
let query = () => {
  loading.value = true
  api.get("/gg/product/list", queryParams.value).then(result => {
    if (result.code === 200) {
      tableData.value = result.data.list || []
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
let handleAdd = () => {
  editFormVisible.value = true
  isAdd.value = true
  editTitle.value = "新增产品"
  reset()
}

// 编辑
let handleEdit = (row) => {
  editFormVisible.value = true
  isAdd.value = false
  editTitle.value = "编辑产品"
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
    business: "无",
    color: 0,
    purchasingPattern: "集团采购",
    distribution: "非铺货",
    standby: "非备机"
  }
}

// 保存
function save() {
  if (!editForm.value.coding) {
    ElMessage.error("商品编码不能为空")
    return
  }
  if (!editForm.value.classify) {
    ElMessage.error("分类不能为空")
    return
  }
  if (!editForm.value.brand) {
    ElMessage.error("品牌不能为空")
    return
  }
  if (!editForm.value.model) {
    ElMessage.error("型号不能为空")
    return
  }

  if (isAdd.value) {
    api.postJson("/gg/product", editForm.value).then(result => {
      if (result.code === 200) {
        ElMessage.success("新增成功")
        editFormVisible.value = false;
        query()
      } else {
        ElMessage.error(result.msg)
      }
    })
  } else {
    api.put(`/gg/product/${editForm.value.id}`, editForm.value).then(result => {
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
    const deletePromises = selectedRows.value.map(row =>
        api.delete(`/gg/product/${row.id}`)
    )
    Promise.all(deletePromises).then(results => {
      const allSuccess = results.every(result => result.code === 200)
      if (allSuccess) {
        ElMessage.success("删除成功")
        query()
        selectedRows.value = []
      } else {
        ElMessage.error("部分数据删除失败")
      }
    }).catch(() => {
      ElMessage.error("删除失败")
    })
  }).catch(() => {
    ElMessage.info('已取消删除');
  })
}

// 删除单个
let deleteOne = (row) => {
  ElMessageBox.confirm('是否确认删除商品编码为[' + row.coding + ']的数据?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    api.delete(`/gg/product/${row.id}`).then(result => {
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

// 自定义上传处理
const handleBeforeUpload = async (file) => {
  const isExcel = file.name.endsWith('.xlsx') || file.name.endsWith('.xls')
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isExcel) {
    ElMessage.error('只能上传 Excel 文件!')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  }

  try {
    loading.value = true
    const formData = new FormData()
    formData.append('file', file)

    const result = await api.postFile('/gg/product/import', formData)

    if (result.code === 200) {
      const data = result.data
      if (data.success) {
        importResult.value = {
          type: 'success',
          title: '导入成功',
          message: data.message
        }
        if (data.errorMessages) {
          importResult.value.errors = data.errorMessages
        }
        query()
        setTimeout(() => { importResult.value = null }, 3000)
      } else {
        importResult.value = {
          type: 'error',
          title: '导入失败',
          message: data.message || '导入过程中发生错误',
          errors: data.errorMessages
        }
      }
    } else {
      importResult.value = {
        type: 'error',
        title: '导入失败',
        message: result.msg || '导入过程中发生错误'
      }
    }
  } catch (error) {
    importResult.value = {
      type: 'error',
      title: '上传失败',
      message: error.message || '上传过程中发生错误'
    }
    ElMessage.error('上传失败: ' + error.message)
  } finally {
    loading.value = false
  }

  return false
}

// 下载模板
// const downloadTemplate = () => {
//   window.open('/admin/gg/product/template/download', '_blank')
//   ElMessage.success('开始下载模板')
// }

// 在您的组件中，修改downloadTemplate函数
const downloadTemplate = async () => {
  try {
    // 通过api.download下载文件
    const result = await api.download('/gg/product/template/download', null, '产品导入模板.xlsx')

    // 创建下载链接
    const url = window.createObjectURL(new Blob([result.data]))
    const link = document.createElement('a')
    link.href = url
    link.download = result.filename
    document.body.appendChild(link)
    link.click()

    // 清理
    window.URL.revokeObjectURL(url)
    document.body.removeChild(link)

    ElMessage.success('模板下载开始')
  } catch (error) {
    console.error('下载失败:', error)
    if (error.response && error.response.status === 401) {
      ElMessage.error('未登录，请先登录')
    } else {
      ElMessage.error('下载失败: ' + (error.message || '未知错误'))
    }
  }
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

.text-gray {
  color: #909399;
}

.error-details {
  margin-top: 10px;
  background: #f8f8f8;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #e8e8e8;
  max-height: 200px;
  overflow-y: auto;
}

.error-details h4 {
  margin: 0 0 10px 0;
  color: #f56c6c;
}

.error-details pre {
  margin: 0;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: Consolas, Monaco, 'Andale Mono', 'Ubuntu Mono', monospace;
  font-size: 12px;
  line-height: 1.5;
}
</style>