<script setup>

import LeftMenu from "@/components/LeftMenu.vue";
import Header from "@/components/Header.vue";
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-aside><LeftMenu/></el-aside>
      <el-container>
        <el-header ><Header/></el-header>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.el-header{
  border-bottom: 1px solid var(--el-border-color);
  height: 60.67px;
  align-content: center;

}
.el-aside{
  width: 200px;
  height: 100vh;
  box-shadow: 4px 0 6px -1px rgba(0, 0, 0, 0.1); /* 右侧阴影 */
  border-right: none; /* 移除原有边框 */
}
</style>