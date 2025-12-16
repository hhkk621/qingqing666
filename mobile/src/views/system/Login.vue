<template>
  <div class="login-main">
    <div class="login-head">
      <div class="row video-main">
        <div class="col-lg-12 col-md-12">
          <video class="head-video" src="http://cd.ray-live.cn/video/xiangxiang.mp4" poster="" autoplay="" loop="" muted=""></video>
        </div>
      </div>
    </div>
    <div class="container">
      <div class="row">
        <div class="col-lg-6 col-md-6 login-form-main">
          <div class="login-form">
            <h2>登录</h2>
            <form ref="formValid" class="needs-validation" novalidate>
              <div class="tab-content" id="nav-tabContent">
                <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab" tabindex="0">
                  <div class="form-group">
                    <input type="text" class="form-control" placeholder="请输入用户名" v-model="form.uname" >
                  </div>
                  <div class="form-group input-group">
                    <input type="password" class="form-control" placeholder="请输入密码" v-model="form.pwd"  @keyup.enter="doLogin">
                  </div>
                </div>
                <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab" tabindex="0">
                  <div class="form-group">
                    <input type="text" class="form-control" placeholder="请输入手机号" v-model="form.uname" required >
                  </div>
                  <div class="form-group input-group">
                    <input type="password" class="form-control" placeholder="请输入验证码" v-model="form.pwd"  required>
                    <input type="button" :value="countDown > 0 ? countDown+` s后重新发送`  : '发送验证码'" class="btn btn-info" :disabled="countDown > 0">
                  </div>
                </div>
              </div>

              <nav>
                <div class="nav nav-tabs" id="nav-tab" role="tablist">
                  <button class="nav-link active" id="nav-home-tab"
                          data-bs-toggle="tab" data-bs-target="#nav-home" type="button"
                          role="tab" aria-controls="nav-home" aria-selected="true" @click="form.type='uname'">用户名密码登录</button>
                  <button class="nav-link" id="nav-profile-tab"
                          data-bs-toggle="tab" data-bs-target="#nav-profile" type="button"
                          role="tab" aria-controls="nav-profile" aria-selected="false" @click="form.type='phone'">手机号验证码登录</button>
                </div>
              </nav>
              <button type="button" class="default-btn" @click="doLogin">登录</button>
              <div class="or-text"><span></span></div>
              <button type="button" class="google-btn" @click="jumpReg">注册</button>
            </form>
          </div>
        </div>
        <div class="col-lg-3 col-md-3 offset-3 login-logo">
          <img src="@/assets/imgs/logo_reg.png" alt="image">
        </div>
      </div>
    </div>
  </div>

</template>

<script setup>
import {ref, onMounted} from "vue";
import router from '@/router/index.js'

import abc from '@/utils/request.js'

import userStore from '@/stores/userStore.js';
import {ElMessage} from "element-plus";

let form = ref({
  uname: '',
  pwd: '',
});
let countDown=ref(0);


let doLogin = () => {
  let param = {
    "username": form.value.uname,
    "password": form.value.pwd
  }

  abc.post("/login/doUsernameLogin",param).then(result=>{
    if(result.code === 200){
      //把登录返回的用户信息，存储到pina中，sessionStorage中
      userStore().userModel = result.data;
      router.push("/")
    }else {
      ElMessage.error(result.msg)
    }

  })
}
let jumpReg=()=>{
  router.push("/reg")
}

onMounted(() => {

})
</script>

<style scoped>
.login-form input {
  margin: 20px 0px;
}

.vector-image img {
  margin-top: 145px;
}

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
  height: 100%;
  width: 99.9%;
  max-width: 1920px;
  min-height: 99vh;
  min-width: 1320px;
  max-height: 100%;
  position: absolute;
  z-index: -2;
  scrollbar-width: none;
  overflow: hidden;
}

.login-main .container {
  position: absolute;
  float: left;
  left: 25%;
  top: 10px;
  margin-top: 0px;
  z-index: 2;
  height: 690px;
}
.login-form-main{

  margin-top: 100px;
}
.login-logo{

}
.login-form{
  background: rgba(255,255,255,0.5);
}


/* 针对 WebKit 浏览器 */
::-webkit-scrollbar {
  display: none;
}

</style>