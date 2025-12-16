`
<script setup>
import {defineComponent, ref, computed} from 'vue'
import breadcrumb from "@/stores/Breadcrumb.js";
import router from "@/router/index.js";
import loginUser from '@/stores/LoginUser.js'
let userModel = ref(loginUser().userModel)
const goBack = () => {
  // 更新面包屑状态
  breadcrumb().popBreadcrumb()

  // 路由回退（优先尝试路由历史后退）
  if (window.history.state.back) {
    router.back()
  } else {
    // 没有历史记录时回退到首页
    router.push('/index')
  }
}
const logout = () => {
  loginUser().userModel = {
    uid: -1,//未登录,
    role:{
      menuList:[]
    }
  }
  router.push("/login");
}
</script>

<template>
  <el-row :gutter="20" justify="center" class="top-main">
    <el-col :span="16">
      <el-page-header @back="goBack">
        <template #content>
          <span class="text-large font-600 mr-3">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item>首页</el-breadcrumb-item>
              <el-breadcrumb-item v-if="breadcrumb().breadcrumb.one">
                {{ breadcrumb().breadcrumb.one }}
              </el-breadcrumb-item>
              <el-breadcrumb-item v-if="breadcrumb().breadcrumb.two">
                {{ breadcrumb().breadcrumb.two }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </span>
        </template>
      </el-page-header>

    </el-col>
    <el-col :span="2"></el-col>
    <el-col :span="4" :offset="2">
      <el-avatar :size="30">
        <img :src="userModel.headImg" />
      </el-avatar>
      <el-dropdown>
      <span class="el-dropdown-link">
         {{ (userModel.nickname || userModel.username).slice(0, 10) }}
        <el-icon class="el-icon--right">
          <arrow-down />
        </el-icon>
      </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="router.push('/user/info')">个人中心</el-dropdown-item>
            <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-col>
  </el-row>
</template>

<style scoped>
.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}

.el-dropdown {
   margin-top: 5px;
   margin-left: 5px;
  /* 移除自身轮廓 */
  :deep(.el-dropdown-selfdefine:focus) {
    outline: none !important;
    box-shadow: none !important;
  }

  /* 移除弹出层轮廓 */
  :deep(.el-popper) {
    outline: none !important;
  }
}
.el-dropdown:focus-visible {
  outline: 2px solid transparent;
  background-color: var(--el-fill-color-light);
}
/* 优化下拉菜单视觉效果 */
.example-showcase .el-dropdown-link {
  transition: all 0.3s;
  &:hover {
    background-color: var(--el-fill-color-light);
  }
}
.demo-type{
  display: inline;
}
</style>`