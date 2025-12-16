<script setup>
import {formatDay, formatTime} from '@/utils/date.js'
import {ref, onMounted, getCurrentInstance} from "vue";
import { ElMessage } from 'element-plus'
import loginUser from '@/stores/LoginUser.js'
import UnamePwd from "@/stores/UnamePwd.js";
import router from "@/router/index.js";
import api from '@/utils/request.js';
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const { proxy } = getCurrentInstance();
let userModel = ref(loginUser().userModel)
let userForm = ref({
  nickname:loginUser().userModel.nickname,
  email:loginUser().userModel.email,
  phone: loginUser().userModel.phone,
});
let passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
onMounted(()=>{
  //userForm.value = userModel.value;
});

const onSubmit = () => {
  console.log(userForm.value)
  proxy.$refs.formRef.validate(valid => {
    if (valid) {

    }
  })

}

const onSubmitPassword = () => {
  proxy.$refs.passwordFormRef.validate(valid => {
    if (valid) {

    }
  })
}

const handleAvatarSuccess=(result,file)=>{
  if (result.code === 200){
    ElMessage.success('上传成功')
    loginUser().userModel.headImg = result.data;
  }else if (result.code === 1002){
    router.push("/login")
  } else {
    ElMessage.error('上传失败')
  }
  console.log(result)
  console.log(file)

}
const activeName = ref('first')
const onFileChange = (file) => {
  console.log(file)
  if (!file) return

  if (!/^image\/(jpeg|png|gif)$/.test(file.type)) {
    ElMessage.error('仅支持JPG/PNG/GIF格式')
    return
  }

  // 处理上传逻辑...
}
const userInfoRules= {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
 }
const passwordRules = {
  oldPassword: [
    { required: true, message: '旧密码不能为空', trigger: 'blur' },
    { min: 3, max: 12, message: '长度在 3 到 12 个字符', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '新密码不能为空', trigger: 'blur' },
    { min: 3, max: 12, message: '长度在 3 到 12 个字符', trigger: 'blur' },
    {
      validator: (_, value) =>
          value !== passwordForm.value.oldPassword,
      message: '新密码不能与旧密码相同',
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { min: 3, max: 12, message: '长度在 3 到 12 个字符', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: ['blur', 'change']
    }
  ]
}
//二维码
const qrVisible = ref(false)
const qrCodeUrl = ref("test")

</script>

<template>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>个人信息</span>
          </div>
        </template>
        <div class="text-center">
          <el-upload
              class="avatar-uploader"
              :action="baseUrl+'/login/doUploadPic'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="onFileChange"
          >
            <img v-if="userModel.headImg" :src="userModel.headImg" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>

        </div>
        <ul class="list-group list-group-striped">
          <li class="list-group-item">
            <el-icon><User /></el-icon>
            <span>用户名称</span>
            <div class="pull-right">{{ userModel.username }}</div>
          </li>
          <li class="list-group-item">
            <el-icon><UserFilled /></el-icon>
            <span>角色名称</span>
            <div class="pull-right">{{ userModel.role.name }}</div>
          </li>
          <li class="list-group-item">
            <el-icon><Timer /></el-icon>
            <span>创建日期</span>
            <div class="pull-right">{{ formatDay(userModel.regTime) }}</div>
          </li>
          <li class="list-group-item">
            <el-icon><ChromeFilled /></el-icon>
            <span>最后登录时间</span>
            <div class="pull-right">{{ formatDay(userModel.loginTime) }}</div>
          </li>
        </ul>
      </el-card>

    </el-col>
    <el-col :span="16">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>基本资料</span>
          </div>
        </template>
        <el-row>
          <el-col :span="24">
            <el-tabs  v-model="activeName" class="demo-tabs">
              <el-tab-pane label="基本资料" name="first">
                <el-form  ref="formRef" :model="userForm" label-width="auto" style="max-width: 600px" :rules="userInfoRules">
                  <el-form-item label="昵称" prop="nickname">
                    <el-input v-model="userForm.nickname" />
                  </el-form-item>
                  <el-form-item label="邮箱" prop="email">
                    <el-input v-model="userForm.email" />
                  </el-form-item>
                  <el-form-item label="手机号" prop="phone">
                    <el-input v-model="userForm.phone" />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="onSubmit">保存</el-button>
                    <el-button type="danger" @click="router.push('/index')">关闭</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="修改密码" name="second">
                <el-form  ref="passwordFormRef" :model="passwordForm" label-width="auto" style="max-width: 600px" :rules="passwordRules">
                  <el-form-item label="旧密码" prop="oldPassword">
                    <el-input
                        v-model="passwordForm.oldPassword"
                        type="password"
                        placeholder="请输入旧密码"
                        show-password
                    />
                  </el-form-item>
                  <el-form-item label="新密码" prop="newPassword">
                    <el-input
                        v-model="passwordForm.newPassword"
                        type="password"
                        placeholder="请输入新密码"
                        show-password
                    />
                  </el-form-item>
                  <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input
                        v-model="passwordForm.confirmPassword"
                        type="password"
                        placeholder="请确认新密码"
                        show-password
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="onSubmitPassword">保存</el-button>
                    <el-button type="danger" @click="router.push('/index')">关闭</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
            </el-tabs>

          </el-col>
        </el-row>
      </el-card>
    </el-col>
  </el-row>
  </template>

<style scoped>
.avatar-uploader {
  position: relative;
  display: inline-block;
  cursor: pointer;
}

.hover-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader:hover .hover-mask {
  opacity: 1;
}

.upload-icon {
  color: white;
  font-weight: bold;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.list-group {
  padding-left: 0;
  list-style: none;
}

ul {
  display: block;
  list-style-type: disc;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  padding-inline-start: 40px;
  unicode-bidi: isolate;
}

.list-group-striped>.list-group-item {
  border-left: 0;
  border-right: 0;
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
}
.list-group-item {
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0;
  font-size: 13px;
}
li {
  display: list-item;
  text-align: -webkit-match-parent;
  unicode-bidi: isolate;
}
.pull-right {
  float: right !important;
}
</style>