<template>
  <div class="app-container">
    <!-- 头部操作区域 -->
    <el-row class="mb-3">
      <el-col>
        <el-button type="primary" :icon="Plus" plain @click="handleAdd">新增</el-button>
        <el-button type="danger" :icon="Delete" plain @click="deleteSelectedRows" :disabled="!isSelectRow">批量删除</el-button>
        <el-button type="warning" :icon="RefreshRight" plain @click="resetQueryForm">重置</el-button>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="mb-3" shadow="never">
      <el-form :model="queryParams" label-width="80px" :inline="true">
        <el-form-item label="类型名称">
          <el-input
              v-model="queryParams.typeName"
              placeholder="请输入类型名称"
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

    <!-- 主内容区域 -->
    <el-row :gutter="20">
      <!-- 左侧树形结构 -->
      <el-col :span="8">
        <el-card shadow="never" class="h-100">
          <template #header>
            <div class="card-header">
              <span>商品类型树</span>
              <el-button type="primary" :icon="Plus" size="small" @click="handleAddRoot">新增根节点</el-button>
            </div>
          </template>
          <div class="tree-container">
            <el-tree
                ref="treeRef"
                :data="typeTree"
                :props="treeProps"
                node-key="id"
                highlight-current
                :expand-on-click-node="false"
                :default-expanded-keys="expandedKeys"
                @node-click="handleNodeClick"
                v-loading="loadingTree"
            >
              <template #default="{ node, data }">
                <span class="tree-node">
                  <span>{{ node.label }}</span>
                  <span class="node-actions">
                    <el-button
                        type="success"
                        size="small"
                        :icon="Plus"
                        link
                        @click.stop="handleAddChild(data)"
                        title="添加子类型"
                    />
                    <el-button
                        type="primary"
                        size="small"
                        :icon="Edit"
                        link
                        @click.stop="handleEdit(data)"
                        title="编辑"
                    />
                    <el-button
                        v-if="!data.children || data.children.length === 0"
                        type="danger"
                        size="small"
                        :icon="Delete"
                        link
                        @click.stop="handleDelete(data.id)"
                        title="删除"
                    />
                  </span>
                </span>
              </template>
            </el-tree>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧表格 -->
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>类型列表</span>
              <span v-if="currentNode" class="current-node">当前父级: {{ currentNode.typeName }}</span>
            </div>
          </template>

          <el-table
              :data="tableData"
              border
              stripe
              @selection-change="handleSelectionChange"
              v-loading="loadingTable"
          >
            <el-table-column type="selection" width="55" />

            <el-table-column prop="id" label="ID" width="80" sortable />

            <el-table-column prop="typeName" label="类型名称" width="150">
              <template #default="scope">
                <el-tag type="primary">{{ scope.row.typeName }}</el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="desc" label="类型描述" />

            <el-table-column prop="pid" label="父级ID" width="100">
              <template #default="scope">
                <el-tag v-if="scope.row.pid === '-1'" type="success">根节点</el-tag>
                <span v-else>{{ scope.row.pid }}</span>
              </template>
            </el-table-column>

            <el-table-column prop="createTime" label="创建时间" width="180" sortable>
              <template #default="scope">
                {{ formatDateTime(scope.row.createTime) }}
              </template>
            </el-table-column>

            <el-table-column prop="updateTime" label="更新时间" width="180" sortable>
              <template #default="scope">
                {{ formatDateTime(scope.row.updateTime) }}
              </template>
            </el-table-column>

            <el-table-column label="操作" width="180" fixed="right">
              <template #default="scope">
                <el-button type="primary" size="small" :icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button
                    type="danger"
                    size="small"
                    :icon="Delete"
                    @click="deleteOne(scope.row)"
                    :disabled="hasChildren(scope.row.id)"
                >
                  删除
                </el-button>
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
              :total="total"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              class="mt-3"
          />
        </el-card>
      </el-col>
    </el-row>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="editFormVisible" :title="editTitle" width="500px" append-to-body>
      <el-form :model="editForm" ref="editFormRef" label-width="100px" :rules="formRules">
        <el-form-item label="类型名称" prop="typeName" required>
          <el-input v-model="editForm.typeName" placeholder="请输入类型名称" maxlength="50" />
        </el-form-item>

        <el-form-item label="类型描述" prop="desc">
          <el-input
              v-model="editForm.desc"
              type="textarea"
              :rows="3"
              placeholder="请输入类型描述"
              maxlength="200"
              show-word-limit
          />
        </el-form-item>

        <el-form-item label="父级类型" prop="pid" required>
          <el-tree-select
              v-model="editForm.pid"
              :data="parentOptions"
              :props="selectTreeProps"
              check-strictly
              placeholder="请选择父级类型"
              clearable
              :render-after-expand="false"
              style="width: 100%"
              :disabled="editForm.id && editForm.pid === '-1'"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="save" :loading="saving">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
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
  Plus
} from '@element-plus/icons-vue'
import { ref, reactive, onMounted, computed, nextTick } from "vue";
import request from '@/utils/request.js';
import { ElMessage, ElMessageBox } from "element-plus";

// API路径配置
const API = {
  tree: '/gg/goodsType/tree',
  page: '/gg/goodsType/page',
  get: (id) => `/gg/goodsType/${id}`,
  add: '/gg/goodsType',
  update: '/gg/goodsType',
  delete: (id) => `/gg/goodsType/${id}`,
  parentOptions: '/gg/goodsType/parentOptions',
  search: '/gg/goodsType/search'
}

// 响应式数据
const treeRef = ref()
const editFormRef = ref()
const typeTree = ref([])
const tableData = ref([])
const parentOptions = ref([])
const currentNode = ref(null)
const expandedKeys = ref([])
const loadingTree = ref(false)
const loadingTable = ref(false)
const editFormVisible = ref(false)
const saving = ref(false)
const total = ref(0)
const isSelectRow = ref(false)
const selectedRows = ref([])

// 树形配置
const treeProps = {
  children: 'children',
  label: 'typeName'
}

// 选择树配置
const selectTreeProps = {
  label: 'typeName',
  value: 'id',
  children: 'children'
}

// 搜索查询参数
const queryParams = reactive({
  currentPage: 1,
  pageSize: 10,
  typeName: '',
  pid: '-1'
})

// 编辑表单
const editForm = reactive({
  id: '',
  typeName: '',
  desc: '',
  pid: '-1'
})

// 编辑标题
const editTitle = computed(() => {
  return editForm.id ? '编辑商品类型' : '新增商品类型'
})

// 表单验证规则
const formRules = {
  typeName: [
    {required: true, message: '类型名称不能为空', trigger: 'blur'},
    {min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur'}
  ],
  pid: [
    {required: true, message: '请选择父级类型', trigger: 'change'}
  ]
}

// 初始化查询参数
const resetQueryForm = () => {
  queryParams.currentPage = 1
  queryParams.pageSize = 10
  queryParams.typeName = ''
  queryParams.pid = '-1'
  currentNode.value = null
  loadData()
}

// 表格选择变化
const handleSelectionChange = (val) => {
  selectedRows.value = val;
  isSelectRow.value = selectedRows.value.length > 0;
};

// 加载数据
const loadData = async () => {
  try {
    loadingTree.value = true
    loadingTable.value = true

    // 并行加载树和选项
    const [treeRes, optionsRes] = await Promise.all([
      request.get(API.tree),
      request.get(API.parentOptions)
    ])

    if (treeRes.code === 200) {
      typeTree.value = treeRes.data
      // 默认展开第一级
      if (typeTree.value.length > 0) {
        expandedKeys.value = [typeTree.value[0].id]
        currentNode.value = typeTree.value[0]
        queryParams.pid = currentNode.value.id
      }
    }

    if (optionsRes.code === 200) {
      // 添加根节点选项
      parentOptions.value = [
        {id: '-1', typeName: '根节点', children: []},
        ...optionsRes.data
      ]
    }

    // 加载表格数据
    await loadTableData()

  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loadingTree.value = false
    loadingTable.value = false
  }
}

// 加载表格数据
const loadTableData = async () => {
  try {
    loadingTable.value = true

    const params = {
      pageNum: queryParams.currentPage,
      pageSize: queryParams.pageSize,
      pid: queryParams.pid
    }

    if (queryParams.typeName) {
      params.typeName = queryParams.typeName
    }

    const res = await request.get(API.page, params)

    if (res.code === 200) {
      tableData.value = res.data.records || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error(res.msg || '加载列表失败')
    }
  } catch (error) {
    console.error('加载表格数据失败:', error)
    ElMessage.error('加载列表失败')
  } finally {
    loadingTable.value = false
  }
}

// 搜索
const handleSearch = () => {
  queryParams.currentPage = 1
  loadTableData()
}

// 树节点点击
const handleNodeClick = (data) => {
  currentNode.value = data
  queryParams.pid = data.id
  queryParams.currentPage = 1
  loadTableData()
}

// 新增根节点
const handleAddRoot = () => {
  resetEditForm()
  editForm.pid = '-1'
  editFormVisible.value = true
}

// 新增
const handleAdd = () => {
  resetEditForm()
  editFormVisible.value = true
}

// 新增子类型
const handleAddChild = (data) => {
  resetEditForm()
  editForm.pid = data.id
  editFormVisible.value = true
}

// 编辑
const handleEdit = (data) => {
  resetEditForm()
  Object.assign(editForm, {
    id: data.id,
    typeName: data.typeName,
    desc: data.desc || '',
    pid: data.pid || '-1'
  })
  editFormVisible.value = true
}

// 取消编辑
const cancel = () => {
  editFormVisible.value = false
  resetEditForm()
}

// 重置表单
const resetEditForm = () => {
  if (editFormRef.value) {
    editFormRef.value.resetFields()
  }
  Object.assign(editForm, {
    id: '',
    typeName: '',
    desc: '',
    pid: currentNode.value?.id || '-1'
  })
}

// 保存
const save = async () => {
  try {
    // 表单验证
    if (!await editFormRef.value.validate()) {
      return
    }

    saving.value = true

    let res
    if (editForm.id) {
      // 编辑 - 使用put方法
      res = await request.putJson(API.update, editForm)
    } else {
      // 新增 - 使用post方法
      res = await request.postJson(API.add, editForm)
    }

    if (res.code === 200) {
      ElMessage.success(editForm.id ? '编辑成功' : '新增成功')
      editFormVisible.value = false
      loadData() // 重新加载所有数据
    } else {
      ElMessage.error(res.msg || '保存失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

// 批量删除
const deleteSelectedRows = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请先选择要删除的数据")
    return
  }

  // 检查是否有包含子节点的类型
  const hasChildrenRows = selectedRows.value.filter(row => hasChildren(row.id))
  if (hasChildrenRows.length > 0) {
    const names = hasChildrenRows.map(row => row.typeName).join('、')
    ElMessage.warning(`以下类型存在子节点，无法删除：${names}`)
    return
  }

  const idsArray = selectedRows.value.map(row => row.id)

  try {
    await ElMessageBox.confirm(
        `是否确认删除选中的 ${idsArray.length} 条数据?`,
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
    )

    // 逐个删除
    const deletePromises = idsArray.map(id =>
        request.delete(API.delete(id))
    )

    const results = await Promise.all(deletePromises)
    const allSuccess = results.every(result => result.code === 200)

    if (allSuccess) {
      ElMessage.success("删除成功")
      loadData()
      selectedRows.value = []
    } else {
      const failedResults = results.filter(result => result.code !== 200)
      const errorMsg = failedResults.map(r => r.msg).join('; ')
      ElMessage.error(`部分删除失败: ${errorMsg}`)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error("删除失败")
    }
  }
}

// 删除单个
const deleteOne = async (row) => {
  // 检查是否有子节点
  if (hasChildren(row.id)) {
    ElMessage.warning('该类型存在子节点，无法删除')
    return
  }

  try {
    await ElMessageBox.confirm(
        `是否确认删除类型 [${row.typeName}]?`,
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
    )

    const res = await request.delete(API.delete(row.id))

    if (res.code === 200) {
      ElMessage.success("删除成功")
      loadData()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error("删除失败")
    }
  }
}

// 检查是否有子节点
const hasChildren = (id) => {
  const findNode = (nodes) => {
    for (const node of nodes) {
      if (node.id === id) {
        return node.children && node.children.length > 0
      }
      if (node.children) {
        const found = findNode(node.children)
        if (found) return true
      }
    }
    return false
  }
  return findNode(typeTree.value)
}

// 分页大小改变
const handleSizeChange = (size) => {
  queryParams.pageSize = size
  queryParams.currentPage = 1
  loadTableData()
}

// 页码改变
const handleCurrentChange = (page) => {
  queryParams.currentPage = page
  loadTableData()
}

// 格式化日期时间
const formatDateTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  })
}

// 页面加载
onMounted(() => {
  loadData()
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

.h-100 {
  height: calc(100vh - 200px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tree-container {
  height: calc(100vh - 280px);
  overflow-y: auto;
}

.tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 5px 0;
  flex: 1;
}

.node-actions {
  opacity: 0;
  transition: opacity 0.3s;
  display: flex;
  gap: 5px;
}

:deep(.el-tree-node__content:hover) .node-actions {
  opacity: 1;
}

.current-node {
  font-size: 14px;
  color: #666;
  margin-left: 10px;
}

.text-gray {
  color: #909399;
}
</style>