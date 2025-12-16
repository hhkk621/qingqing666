
<template>
  <!-- 图标选择容器 -->
  <div class="icon-body">
    <!-- 搜索框 -->
    <el-input
        v-model="iconName"
        class="icon-search"
        clearable
        placeholder="请输入图标名称"
        @clear="filterIcons"
    @input="filterIcons"
    >
    <!-- 搜索图标 -->
    <template #suffix><i class="el-icon-search el-input__icon" /></template>
    </el-input>

    <!-- 图标列表区域 -->
    <div class="icon-list">
      <div class="list-container">
        <!-- 循环渲染图标项 -->
        <div
            v-for="(item, index) in iconList"
            class="icon-item-wrapper"
            :key="index"
            @click="selectedIcon(item)"
        >
        <!-- 单个图标容器 -->
        <div :class="['icon-item', { active: activeIcon === item }]">
          <!-- Element Plus图标组件 -->
          <el-icon v-if="item">
            <component :is="item" />
          </el-icon>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<script setup>// 导入Element Plus所有图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 获取图标名称数组
const icons = Object.keys(ElementPlusIconsVue)

import { ref } from "vue";

// 定义组件props
const props = defineProps({
  activeIcon: {  // 当前激活的图标
    type: String
  }
});

// 响应式数据
const iconName = ref('');       // 搜索关键词
const iconList = ref(icons);    // 当前显示的图标列表
const emit = defineEmits(['selected']);  // 定义事件

// 过滤图标方法
function filterIcons() {
  iconList.value = icons  // 重置为全量图标
  if (iconName.value) {
    // 根据关键词过滤（简单包含匹配）
    iconList.value = icons.filter(item => item.indexOf(iconName.value) !== -1)
  }
}

// 图标选择处理
function selectedIcon(name) {
  emit('selected', name)   // 触发选择事件
  document.body.click()    // 关闭弹出层（通过模拟点击body）
}

// 重置方法（供父组件调用）
function reset() {
  iconName.value = ''      // 清空搜索
  iconList.value = icons   // 重置图标列表
}

// 暴露方法给父组件
defineExpose({
  reset
})
</script>

<style lang='scss' scoped>/* 容器样式 */
.icon-body {
  width: 100%;
  padding: 10px;

  /* 搜索框样式 */
  .icon-search {
    position: relative;
    margin-bottom: 5px;
  }

  /* 图标列表容器 */
  .icon-list {
    height: 200px;
    overflow: auto;  // 超出高度滚动

    /* 列表布局 */
    .list-container {
      display: flex;
      flex-wrap: wrap;  // 换行显示

      /* 单个图标容器 */
      .icon-item-wrapper {
        width: calc(100% / 5);  // 每行5个
        height: 25px;
        line-height: 25px;
        cursor: pointer;
        display: flex;

        /* 图标项样式 */
        .icon-item {
          display: flex;
          max-width: 100%;
          height: 100%;
          padding: 0 5px;

          /* 悬停效果 */
          &:hover {
            background: #ececec;
            border-radius: 5px;
          }

          /* 激活状态 */
          &.active {
            background: #ececec;
            border-radius: 5px;
          }
        }
      }
    }
  }
}
</style>