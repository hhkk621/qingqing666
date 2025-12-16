<template>
  <div class="login-main">
    <div class="login-head">
      <div class="row video-main">
        <div class="col-lg-12 col-md-12">
          <video class="head-video" src="http://cd.ray-live.cn/video/WuKong1.mp4" poster="" autoplay="" loop="" muted=""></video>
        </div>
      </div>
    </div>
  </div>
  <div class="common-layout">
    <el-container >
      <el-main class="container">
        <el-row>
          <el-col :span="8" :offset="16" class="login-form-main">
            <div class="login-form">
              <el-row>
                <el-col :span="8" :offset="8" style="text-align: center">
                  <h3 class="title">{{ title }}</h3>
                </el-col>
              </el-row>
              <el-form ref="loginRef" :model="loginForm" :rules="loginRules" >
                <el-form-item prop="username" class="form-group">
                  <el-input
                      v-model="loginForm.username"
                      type="text"
                      placeholder="请输入用户名"
                      size="large"
                      auto-complete="off"
                      :prefix-icon="User"
                      clearable/>
                </el-form-item>
                <el-form-item prop="password" class="form-group">
                  <el-input
                      v-model="loginForm.password"
                      @keyup.enter="doLogin"
                      type="password"
                      size="large"
                      auto-complete="off"
                      placeholder="请输入密码"
                      :prefix-icon="Lock"
                      show-password/>
                </el-form-item>
                <el-row>
                  <el-col :span="24" >
                    <el-button type="primary" class="login-btn" @click="doLogin" size="large">
                      <span v-if="!loading">登 录</span>
                      <span v-else>登 录 中...</span>
                    </el-button>

                  </el-col>
                </el-row>
              </el-form>
            </div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>

</template>

<script setup>
import {
  User,
  Lock,
} from '@element-plus/icons-vue'
import {ref, getCurrentInstance , onMounted} from "vue";
import api from '@/utils/request.js';
import router from "@/router/index.js";
import loginUser from '@/stores/LoginUser.js'
import unamePwd from '@/stores/UnamePwd.js'
import { ElMessage } from 'element-plus'
const title = import.meta.env.VITE_APP_TITLE;
const { proxy } = getCurrentInstance();
const loading = ref(false);
const loginForm = ref({
  username: unamePwd().userModel.username,
  password: unamePwd().userModel.password,
});
const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
};

let doLogin=()=>{
  proxy.$refs.loginRef.validate(valid => {
    //值校验通过了
    if (valid) {
      api.postJson("/login/json",loginForm.value).then(result=>{
        loginUser().userModel = result.data;
        //记住用户名和密码
        unamePwd().userModel = loginForm.value
        router.push({ path: '/index', query: { t: Date.now() } }).then(() => {
          // 可添加加载状态管理
        })
      })

    }
  });

}

onMounted(() => {
});
</script>

<style scoped>
.login-head {
  height: 490px;
  display: flex;
  justify-content: center;
  position: absolute;
  width: 100%;
  top: 0px;
  left: 0px;
  z-index: -1;
}

.login-head video {
  position: absolute;
  top: 0;
  left: 0;
  min-width: 100%; /* 保证视频宽度至少为 100% */
  min-height: 100%; /* 保证视频高度至少为 100% */
  width: auto;
  height: auto;
  z-index: -100;
  object-fit: cover;
}

.login-main {
  width: 100%;
  max-width: 1920px;
  min-height: 100vh;
  min-width: 1320px;
  max-height: 100%;
  position: absolute;
  z-index: -2;
  scrollbar-width: none;
  overflow: hidden;
  margin-left: -8px;
  margin-top: -8px;
}

.common-layout .container {
  /*position: absolute;*/
  float: left;
  top: 10px;
  margin-top: 0px;
  z-index: 2;
  height: 690px;
}
.login-form-main{

  margin-top: 160px;
}
.login-logo{
  margin-left: 10px;
}
.login-form{
  /*  right: 50px;
    top: 50px;
    position: absolute;
    width: 660px;*/
  padding: 30px;
  background: rgba(255,255,255,0.5);
}
.form-group{
  margin-bottom: 20px;
  margin-top: 10px;
}
.login-btn{
  width: 100%;
}
</style>