<script setup>
import {ref, onMounted} from 'vue'
//获取登录信息
import userStore from '@/stores/LoginUser.js'
import Breadcrumb from "@/stores/Breadcrumb.js";

const title = import.meta.env.VITE_APP_TITLE;

let menuList = ref([]);
onMounted(() => {
  if (userStore().userModel.role != null) {
    menuList.value = userStore().userModel.role.menuList
  }
})

let handleOpen = (a, b) => {

}
let handleClose = (a, b) => {

}
</script>

<template>
  <el-row class="tac left-menu-main">
    <el-col :span="24">
      <div class="left-menu-title">
        <el-text class="mx-1" type="primary" size="large">{{ title }}</el-text>
      </div>
      <el-menu
          default-active="0"
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
      >
        <router-link to="/index">
          <el-menu-item index="'1'">
            <el-icon>
              <HomeFilled/>
            </el-icon>
            <span>首页 </span>
          </el-menu-item>
        </router-link>
        <template v-for="menu in menuList">
          <el-sub-menu :index="menu.mid.toString()" @click="Breadcrumb().breadcrumb.one=menu.name">
            <template #title>
              <el-icon>
                <component :is="menu.icon"/>
              </el-icon>
              <span>{{ menu.name }}</span>
            </template>
            <template v-for="child in menu.childList">
              <router-link :to="child.url" @click="Breadcrumb().breadcrumb.two=child.name">
                <el-menu-item :index="`${menu.mid}-${child.mid}`">
                  <el-icon>
                    <component :is="child.icon"/>
                  </el-icon>
                  <span>{{ child.name }}</span>
                </el-menu-item>
              </router-link>
            </template>
          </el-sub-menu>
        </template>


      </el-menu>
    </el-col>

  </el-row>
</template>

<style scoped>
.el-menu {
  border-right: 0;
}
.left-menu-title {
  border-bottom: 1px solid var(--el-border-color);
  height: 50px;
  line-height: 50px;
  align-content: center;
  text-align: center;

}
</style>