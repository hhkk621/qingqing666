<template>
  <div class="reg-main">
    <div class="reg-head">
      <div class="row video-main">
        <div class="col-lg-12 col-md-12">
          <video width="100%"
                 class="head-video"
                 src="http://cd.ray-live.cn/video/goddess-moon.mp4"
                 poster="http://cd.ray-live.cn/video/goddess-moon.jpg"
                 autoplay
                 loop
                 muted></video>
        </div>
      </div>
    </div>
    <div class="container">
      <div class="row">
        <div class="col-lg-12 col-md-12">
          <div class="register-form">
            <h2>注册</h2>
            <form id="reg-form" class="needs-validation" novalidate ref="form">
              <div class="form-group">
                <label>用户名</label>
                <input type="text" class="form-control" placeholder="请输入用户名"
                       v-model="user.username" required minlength="4">
                <div class="invalid-feedback" >用户名长度不足</div>
                <div class="valid-feedback" >用户名不能为空</div>
              </div>
              <div class="form-group">
                <label>密码</label>
                <input type="password" class="form-control" placeholder="请输入密码"
                       v-model="user.password" required>
                <div class="invalid-feedback">密码不能为空</div>
              </div>
              <div class="form-group">
                <label>手机号</label>
                <input type="number" class="form-control" placeholder="请输入手机号"
                       v-model="user.phone" required minlength="11">
                <div class="invalid-feedback">请输入11位手机号</div>
              </div>
              <div class="form-group">
                <label>昵称</label>
                <input type="text" class="form-control" placeholder="请输入昵称"
                       v-model="user.userInfo.nickname" required>
                <div class="invalid-feedback">昵称不能为空</div>
              </div>
              <button type="button" class="default-btn" @click="doRegister">注册</button>
              <div class="or-text"><span></span></div>
              <button type="button" class="google-btn" @click="jumpLogin">返回登录</button>
            </form>
          </div>
        </div>
      </div>
    </div>

    <div class="row reg-logo">
      <div class="col-lg-12 col-md-12">
        <img src="@/assets/imgs/logo_reg.png" alt="image">
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, getCurrentInstance, onMounted} from "vue";
import router from '@/router/index'
import api from '@/utils/request.js'
import userStore from "@/stores/UserStore.js";
import {ElMessage} from "element-plus";


let user = ref({
  "username": "",
  "password": "",
  "userInfo": {
    "nickname": "",
    "email": ""
  }

})
let form = ref()
let jumpLogin = () => {
  router.push("/login")
}
//登录
let doRegister = () => {
  //$('#already').modal('show')
  if (form.value.checkValidity()){
    //校验通过
    api.postJson("/login/doReg",user.value).then(result=>{
      if (result.code === 200){
        userStore().userModel = result.data;
        ElMessage.success("注册成功");
        router.push("/")
      }else {
        ElMessage.error(result.msg)
      }

    })
  }
  form.value.classList.add('was-validated');
}
onMounted(() => {

})

</script>

<style scoped>
.reg-head {
  height: 480px;
  display: flex;
  justify-content: center;
  position: absolute;
  width: 99.3%;
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
.reg-main{
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
.register-form {
  background: rgba(255, 255, 255, 0.5);
}

.container {
  right: 50px;
  top: 50px;
  position: absolute;
  width: 660px;
}

.reg-logo {
  position: absolute;
  left: 100px;
  top: 0px
}

</style>