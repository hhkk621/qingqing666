<script setup>
import {ref, onMounted} from 'vue'
import { storeToRefs } from 'pinia'
import Breadcrumb from "@/stores/Breadcrumb.js";
import {onBeforeRouteUpdate} from "vue-router";
import loginUser from "@/stores/LoginUser.js";

const { userInfo } = storeToRefs(loginUser())


onMounted(() => {
  Breadcrumb().breadcrumb.one='';
  Breadcrumb().breadcrumb.two=''
  initData()
})
// 添加路由守卫监听
onBeforeRouteUpdate((to, from, next) => {
  if (to.path === '/index') {
    initData()
  }
  next()
})
async function initData() {

}
</script>

<template>
首页
</template>

<style scoped>

</style>