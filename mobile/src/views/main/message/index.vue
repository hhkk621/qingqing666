<template>
  <Top/>
  <div class="container-fluid main-content-wrapper">
    <div class="content-page-box-area">
      <div class="all-messages-body">
        <div class="all-messages-header d-flex justify-content-between align-items-center">
          <h3>消息</h3>
        </div>
        <div class="messages-profile-box">
          <a href="/ai">
            <img :src="toUser.headPic" style="width: 100px;" class="rounded-circle" alt="image">
          </a>
          <h3><a href="#">{{ toUser.nickname }}</a></h3>
        </div>
        <div class="messages-chat-container">
          <div class="chat-content" >
            <div id="chat-content"></div>
            <div v-if="isLoading" class="img-loading">
              <el-progress type="dashboard" :percentage="percentage2" :color="colors"/>
            </div>
          </div>
          <div class="chat-list-footer">
            <div class="d-flex align-items-center row">
              <div class="col-md-10">
                <input type="text" class="form-control" placeholder="输入消息..." v-model="messageStr"
                       @keyup.enter="sendMessage">
              </div>
              <div class="col-md-2">
                <button type="button" class="btn btn-success" @click="sendMessage">
                  <i class="bi bi-send"></i>&nbsp;发送
                </button>
              </div>
            </div>
            <div class="row">
              <div class="col-md-12">
                <input type="file" accept="image/*" @change="onFileSelected">
              </div>

            </div>
            <div class="row">
              <div class="col-md-12">
                <img :src="previewImage" style="width: 100%"/>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
  <Footer/>
</template>

<script setup>
import {onMounted, ref} from "vue";
import socket from "@/utils/socket.js";
import UserStore from "@/stores/UserStore.js";
import Top from "@/components/common/Top.vue";
import Footer from "@/components/common/Footer.vue";
import {marked} from "marked";


import ChatUserSotre from "@/stores/ChatUserSotre.js";

let toUser = ChatUserSotre().user;
let loginUser = UserStore().userModel;
//获取当前用户，和对方的用户信息
let toUid = toUser.uid;
let myUid = loginUser.uid;

let messageStr = ref("");

let webSocket;
let initWebSocket = () => {
  //ws://192.168.0.151:8080/ai/textChat/{myuid}/{touid}
  let url = "/ai/textChat/" + myUid + "/" + toUid;
  //调用封装好的方法，连接socket
  socket.initWebSocket(url);
  webSocket = socket.getWebSocket();
  //打印收到的消息
  webSocket.onmessage = e => printMessage(e.data);
}
let sendMessage = () => {
  //type 1文字消息 2图片消息
  let type = 1;
  if (consultFile) {
    sendImage();
    type = 2;
  }
  let param = {
    "question": messageStr.value,
    "type": type
  }


  webSocket.send(JSON.stringify(param));
  //清空聊天框
  messageStr.value = "";
}
let chatNum = 0;
let printMessage = (msg) => {
  let msgObj = JSON.parse(msg);
  //协议号
  let agreement = msgObj.agreement
  let pic;
  let chat_mine = "";
  let html = "";
  let status = 2;
  switch (agreement) {
    case 3001:
      //3001 是用户自己发出去的消息
      let question = msgObj.answer;
      //用户头像
      pic = loginUser.userInfo.headPic
      //默认不是我的
      chat_mine = "chat-right";
      printChatHtml(chat_mine, pic, question)
      break;
    case 3002:
      //是ai发送来的，一定是在对面
      let answer = msgObj.answer;
      //用户头像
      pic = toUser.headPic;
      status = msgObj.status;
      switch (status) {
        case 0:
          allAnswer = "";
          //开始回答问题，创建一个空的聊天框，没有聊天内容
          html = '<div class="chat ' + chat_mine + '">\n' +
              '                <div class="chat-avatar">\n' +
              '                  <a class="d-inline-block">\n' +
              '                    <img src="' + pic + '" width="50" height="50"\n' +
              '                         class="rounded-circle" alt="image">\n' +
              '                  </a>\n' +
              '                </div>\n' +
              '                <div class="chat-body">\n' +
              //记录 当前是页面里的第几个聊天框
              '                  <div class="chat-message" id="chat-meesage-' + chatNum + '"></div>\n' +
              '                </div>\n' +
              '              </div>\n' +
              '            </div>';
          document.getElementById("chat-content").insertAdjacentHTML("beforeend", html);
          break;
        case 1:
          //回答问题的过程，是ai返回的内容，要追加到聊天框里
          document.getElementById("chat-meesage-" + chatNum)
              .insertAdjacentHTML("beforeend", answer);
          //每次的答案内容，都存储的同一个变量
          allAnswer += answer;
          break;
        case 2:
          //回答结束
          html = marked(allAnswer);
          document.getElementById("chat-meesage-" + chatNum).innerHTML = marked(allAnswer);
          allAnswer = "";
          chatNum++;
          break;
      }

      break;
    case 2001:
      //聊天记录
      let chatList = msgObj.list;
      chatList.forEach(chat => {
        let msgUid = chat.uid;
        let msgContent = chat.message;
        let chat_mine = "";
        let chat_Pic = toUser.headPic;
        if (msgUid === myUid) {
          chat_mine = "chat-right"
          chat_Pic = loginUser.userInfo.headPic;
        }
        //应该在聊天记录的实体类中，增加一个是否为图片的字段，是否为音频的字段等标识信息
        //做了简化处理，只是判断聊天内容的结尾是否以.png结束，可以优化
        if (msgContent.lastIndexOf(".png") !== -1) {
          printChatHtml(chat_mine, chat_Pic, "<img src='" + msgContent + "' style='width: 200px;height: 200px;'>")
        } else {
          printChatHtml(chat_mine, chat_Pic, marked(msgContent))
        }

      })
      break;
    case 3004:
      console.log(msgObj)
      //自己发送出去的图片
      let questionUrl = msgObj.answer;
      //用户头像
      pic = loginUser.userInfo.headPic
      //默认不是我的
      chat_mine = "chat-right";
      printChatHtml(chat_mine, pic, "<img src='" + questionUrl + "' style='width: 200px'>")
      break;
    case 3003:
      console.log(msgObj)
      //AI生成的图片
      let answerUrl = msgObj.answer;
      //用户头像
      pic = toUser.headPic;
      status = msgObj.status;
      //默认不是我的
      chat_mine = "";
      //增加动画，获取图片之后，结束动画等
      if (status === 0) {
        //显示进度条
        isLoading.value = true;
        percentage2.value = 0;
        setInterval(() => {
          percentage2.value = (percentage2.value % 100) + 1
        }, 1000)
      } else if (status === 1) {
        isLoading.value = false;
        printChatHtml(chat_mine, pic, "<img src='" + answerUrl + "' style='width: 200px'>")
      }

      break;
  }

}

let allAnswer = "";

let test1=()=>{
  //显示进度条
  isLoading.value = true;
  percentage2.value = 0;
  setInterval(() => {
    percentage2.value = (percentage2.value % 100) + 1
  }, 1000)
}
let printChatHtml = (chat_mine, pic, question) => {
  let html = '<div class="chat ' + chat_mine + '">\n' +
      '                <div class="chat-avatar">\n' +
      '                  <a class="d-inline-block">\n' +
      '                    <img src="' + pic + '" width="50" height="50"\n' +
      '                         class="rounded-circle" alt="image">\n' +
      '                  </a>\n' +
      '                </div>\n' +
      '                <div class="chat-body">\n' +
      '                  <div class="chat-message">\n' +
      question
  '                  </div>\n' +
  '                </div>\n' +
  '              </div>\n' +
  '            </div>'
  document.getElementById("chat-content").insertAdjacentHTML("beforeend", html);
}
//预览图的路径
let previewImage = ref("")
//当前正在选择的图片对象
let consultFile = null;
//上传的方法
let onFileSelected = (event) => {
  //获取文件
  const file = event.target.files[0];
  //如果传了文件
  if (file) {
    //新建一个读取图片对象
    const reader = new FileReader();
    //当图片被加载的时候，执行的方法
    reader.onload = (e) => {
      previewImage.value = e.target.result;
    }
    //读取图片
    reader.readAsDataURL(file)
    consultFile = file;
  }
}

//发送图片到服务器
function sendImage() {
  //通过WebSocket发送，发送二进制数据，到服务器
  const reader = new FileReader();
  reader.onload = (e) => {
    //获取图片的二进制数据
    let imgbytes = new Uint8Array(e.target.result);
    //构建图片的头信息--告诉服务器，来的二进制数据，类型是图片
    //发送二进制数据到服务端的时候，固定前6位，表示文件类型
    const header = new TextEncoder().encode("IMAGE:")
    //构建完整的数据
    const conbined = new Uint8Array(header.length + imgbytes.length);
    //从第0位开始，是header的信息
    conbined.set(header, 0);
    //header结尾的位置开始，是图片的数据
    conbined.set(imgbytes, header.length);
    //数据准备完成，判断session是否存在
    if (webSocket.readyState === WebSocket.OPEN) {
      //连接状态
      //发送二进制数据
      webSocket.send(conbined.buffer);
      console.log("图片上传完成")
    } else {
      console.error("WebSocket未连接，无法发送图片")
    }

  }

  //读取图片
  reader.readAsArrayBuffer(consultFile);
}

//是否正在生成图片
let isLoading = ref(false);
const percentage2 = ref(0)
const colors = [
  {color: '#f56c6c', percentage: 20},
  {color: '#e6a23c', percentage: 40},
  {color: '#5cb87a', percentage: 60},
  {color: '#1989fa', percentage: 80},
  {color: '#6f7ad3', percentage: 100},
]
onMounted(() => {
  initWebSocket()
})
</script>


<style scoped>
.demo-progress .el-progress--line {
  margin-bottom: 15px;
  max-width: 600px;
}

.demo-progress .el-progress--circle {
  margin-right: 15px;
}
.img-loading{
  float: left;
  margin-left: 72px;
}
</style>