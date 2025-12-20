<template>
  <div class="app-container">
    <!-- 🔍 搜索栏：时间范围 + 多条件过滤 -->
    <el-form :inline="true" class="search-form">
      <el-form-item label="操作时间">
        <el-date-picker
            v-model="searchTimeRange"
            type="daterange"
            value-format="YYYY-MM-DD HH:mm:ss"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleTimeChange"
        />
      </el-form-item>
      <el-form-item label="操作人">
        <el-input
            v-model="queryParams.username"
            placeholder="请输入操作人"
            clearable
        />
      </el-form-item>
      <el-form-item label="操作模块">
        <el-input
            v-model="queryParams.module"
            placeholder="请输入操作模块"
            clearable
        />
      </el-form-item>
      <el-form-item label="操作内容">
        <el-input
            v-model="queryParams.opContent"
            placeholder="请输入操作内容（如：获取）"
            clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button type="primary" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 📊 数据表格 -->
    <el-table :data="logList" border stripe>
      <el-table-column prop="opTime" label="操作时间" width="270">
        <template #default="{ row }">
          {{ formatTime(row.opTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="username" label="操作人" width="250" />
      <el-table-column prop="module" label="操作模块" width="260" />
      <el-table-column prop="opContent" label="操作内容" />
    </el-table>

    <!-- 📃 分页组件 -->
    <el-pagination
        v-model:current-page="queryParams.currentPage"
        v-model:page-size="queryParams.pageSize"
        :total="queryParams.total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleQuery"
        @current-change="handleQuery"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import api from '@/utils/request.js' // 封装好的axios请求（需确保支持GET参数传递）
import { formatTime } from '@/utils/date.js' // 时间格式化工具（需自行实现）
import { ElMessage } from 'element-plus'

// -------------------- 1. 响应式数据 --------------------
// 查询参数（含分页 + 过滤条件）
const queryParams = ref({
  currentPage: 1,    // 当前页码
  pageSize: 10,      // 每页条数
  startTime: '',     // 开始时间（后端需格式：yyyy-MM-dd HH:mm:ss）
  endTime: '',       // 结束时间（后端需格式：yyyy-MM-dd HH:mm:ss）
  opContent: '',     // 操作内容（Postman示例参数）
  username: '',      // 操作人
  module: '',        // 操作模块
  total: 0           // 总条数（后端返回后赋值）
})

// 时间范围选择器（前端可视化选择）
const searchTimeRange = ref([])

// 操作日志列表（表格数据）
const logList = ref([])


// -------------------- 2. 时间范围处理：前端→后端格式转换 --------------------
// 监听时间范围变化，自动格式化给 queryParams
const handleTimeChange = () => {
  if (searchTimeRange.value?.length === 2) {
    // 补全「时分秒」（如：2024-01-01 → 2024-01-01 00:00:00）
    queryParams.value.startTime = `${searchTimeRange.value[0]} 00:00:00`
    queryParams.value.endTime = `${searchTimeRange.value[1]} 23:59:59`
  } else {
    // 清空时间条件
    queryParams.value.startTime = ''
    queryParams.value.endTime = ''
  }
}

// 初始化时触发一次（避免首次加载不触发 change）
watch(searchTimeRange, handleTimeChange, { immediate: true })


// -------------------- 3. 核心方法：查询日志 --------------------
const handleQuery = () => {
  api.get('/admin/log/page', queryParams.value // GET请求参数通过 params 传递
  )
      .then((res) => {
        if (res.code === 200) {
          // 后端返回结构：{ code, msg, timestamp, data: { total, list, ... } }
          queryParams.value.total = res.data.total
          logList.value = res.data.list
        } else {
          ElMessage.error(res.msg || '查询失败')
        }
      })
      .catch((err) => {
        console.error('查询日志异常：', err)
        ElMessage.error('查询日志失败，请稍后重试')
      })
}


// -------------------- 4. 重置搜索条件 --------------------
const handleReset = () => {
  // 重置分页和过滤条件
  queryParams.value = {
    currentPage: 1,
    pageSize: 10,
    startTime: '',
    endTime: '',
    opContent: '',
    username: '',
    module: '',
    total: 0
  }
  // 重置时间选择器
  searchTimeRange.value = []
  // 重置后立即查询（显示第一页全部数据）
  handleQuery()
}


// -------------------- 5. 页面加载时自动查询 --------------------
onMounted(() => {
  handleQuery()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.search-form {
  margin-bottom: 20px;
}
/* 优化表格行高（可选） */
.el-table td {
  padding: 12px 0;
}
</style>