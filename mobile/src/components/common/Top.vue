<template>
  <div class="container-fluid">
    <div class="row top " >
      <nav class="navbar navbar-expand-lg bg-primary col" data-bs-theme="dark">
        <div class="container-fluid">
          <router-link class="navbar-brand top-logo" to="/login">
            <img src="@/assets/imgs/logo_top.png" class="rounded float-start" alt="...">
          </router-link>
          <div class="collapse navbar-collapse top-info" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
              <li class="nav-item">
                <router-link class="nav-link" aria-current="page" to="/">首页</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link" aria-current="page" to="/room/free">组队开黑</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link" aria-current="page" to="/godPlayers">大神陪玩</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link" aria-current="page" to="/login">派单大厅</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link" aria-current="page" to="/login">娱乐大厅</router-link>
              </li>
            </ul>
            <form class="d-flex option-item" role="search">
              <input class="form-control me-2 bg-white" type="search" placeholder="房间号/用户昵称" aria-label="Search">
              <button class="btn btn-outline-light" type="button">
                <i class="bi bi-search-heart"></i>
              </button>
            </form>
            <template v-if="isLogin">
              <div class="option-item">
                <img :src="userModel.userInfo.headPic" class="head_pic" alt="..." data-bs-toggle="modal" data-bs-target="#headModal">
                <span class="nickname">{{userModel.userInfo.nickname}}</span>
              </div>
              <div class="option-item">
                <div class="btn-group" data-bs-theme="light">
                  <button type="button" class="btn btn-primary dropdown-toggle "
                          data-bs-toggle="dropdown" aria-expanded="false"
                          data-bs-offset="10,20">
                    <i class="bi bi-gear bi-white"></i>
                  </button>
                  <ul class="dropdown-menu dropdown-menu-md-end">
                    <li>
                      <router-link class="dropdown-item" to="/login">个人信息</router-link>
                    </li>
                    <li>
                      <router-link class="dropdown-item" to="/login">修改密码</router-link>
                    </li>
                    <li>
<!--                      <router-link class="dropdown-item" to="/login">退出登录</router-link>-->
                      <a class="dropdown-item" @click="logout">退出登录</a>
                    </li>
                  </ul>
                </div>
              </div>
            </template>
            <template v-else>
              <router-link class="btn btn-outline-light" to="/login">登录</router-link>
              <router-link class="btn btn-outline-light" to="/reg">注册</router-link>
            </template>

          </div>
        </div>
      </nav>
    </div>

  </div>

  <div class="modal fade" id="headModal" tabindex="-1" aria-labelledby="headModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-body">
          <img :src="userModel.userInfo.headPic" class="head_pic" alt="..." data-bs-dismiss="modal">
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from "vue";
import router from '@/router/index.js'
import userStore from '@/stores/userStore.js';
import api from '@/utils/request.js';
import {ElMessage} from "element-plus";
import TokenStore from "@/stores/TokenStore.js";

let user = userStore();
let isLogin = ref(false);
let userModel = reactive(user.userModel);

let autoLogin=()=>{
  api.post("/login/auto").then(result=>{
    if (result.code === 200){
      userModel = result.data;
      userStore().userModel = result.data;
      isLogin.value = true;
    }
  })
}

let logout = ()=>{
  //清空本地Token
  TokenStore().token = "";
  //清空本地用户信息
  userStore().userModel = {
    uid: -1,
    userInfo:{
      headPic: "http://cd.ray-live.cn/imgs/headpic/pic_54.jpg"
    }
  }
  router.push("/login")
}

onMounted(()=>{
  console.log(userModel)
  //判断是否登录
  if (user.userModel.uid!==-1){
    isLogin.value = true;
  }else {
    //未登录
    autoLogin()
  }
})
</script>

<style scoped>

</style>