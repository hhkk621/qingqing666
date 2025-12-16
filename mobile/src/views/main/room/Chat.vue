<template>
  <Top/>
  <div class="container-fluid main-content-wrapper">

    <div class="row">
      <div class="col-lg-6 col-md-12">
        <div class="prism-player" id="player-content"></div>
      </div>
      <div class="col-lg-6 col-md-12">
        <div class="all-messages-body">
          <div class="messages-chat-container">
            <div class="chat-content" id="chat-content"></div>
            <div class="chat-list-footer">
              <div class="d-flex align-items-center">
                <div class="btn-box d-flex align-items-center me-3">
                </div>
                <div class="col-lg-9">
                  <input type="text" class="form-control" placeholder="输入消息..." v-model="input_msg">
                </div>
                <div class="col-lg-2 offset-md-1">
                  <button type="button" class="btn btn-success" @click="sendMessage">
                    <i class="bi bi-send"></i>&nbsp;发送
                  </button>
                </div>
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
import Top from "@/components/common/Top.vue";
import Footer from "@/components/common/Footer.vue";
import Aliplayer from 'aliyun-aliplayer';
import 'aliyun-aliplayer/build/skins/default/aliplayer-min.css';
//登录用户信息
import UserStore from "@/stores/UserStore.js";
//房间id
import {useRoute} from "vue-router";
//当前登录用户的uid
let uid = UserStore().userModel.uid;
//浏览器参数中，获取id的值
let roomId = useRoute().query.id;
//发送的消息内容
let input_msg = ref("")
//连接聊天服务器
let webSocket;
let initWebSocket = () => {
  let url = "/room/multiuser/" + roomId + "/" + uid;
  socket.initWebSocket(url);
  webSocket = socket.getWebSocket();
  //接收服务端传入的消息
  webSocket.onmessage = e => printMessage(e.data);
}
let printMessage = (msg) => {
  /**
   * {
   *     "agreement": 1001,
   *     "message": "我是路人并",
   *     "nickname": "命命",
   *     "pic": "http://cd.ray-live.cn/imgs/headpic/pic_550.jpg",
   *     "uid": 3017
   * }
   */
  let msgObj = JSON.parse(msg);
  //协议号
  let agreement = msgObj.agreement
  switch (agreement) {
    case 1001:
      //1001号协议是聊天
      //发送信息的人的uid
      let msg_uid = msgObj.uid;
      //用户头像
      let pic = msgObj.pic;
      //消息的内容
      let message = msgObj.message;
      //默认不是我的
      let chat_mine = "";
      if (msg_uid == uid){
        chat_mine = "chat-right";
      }
      let html = '<div class="chat '+chat_mine+'">\n' +
          '                <div class="chat-avatar">\n' +
          '                  <a class="d-inline-block">\n' +
          '                    <img src="'+pic+'" width="50" height="50"\n' +
          '                         class="rounded-circle" alt="image">\n' +
          '                  </a>\n' +
          '                </div>\n' +
          '                <div class="chat-body">\n' +
          '                  <div class="chat-message">\n' +
          '                    <p>'+message+'</p>\n' +
          '                  </div>\n' +
          '                </div>\n' +
          '              </div>\n' +
          '            </div>'
      console.log(html)
      document.getElementById("chat-content").insertAdjacentHTML("beforeend",html);
      break;
  }
}

let sendMessage = () => {
  let param = {
    "msg": input_msg.value
  }

  webSocket.send(JSON.stringify(param));
  //清空聊天框
  input_msg.value = "";
}


let initVideo = () => {
  //使用阿里云，直播播放器
  const player = new Aliplayer({
        license: {
          domain: "ai.dxvideo.cn", // 申请 License 时填写的域名
          key: "7P8qS3EB6NRiFZu2M7665aac16fe54f2483f9032c03d94d29" // 申请成功后，在控制台可以看到 License Key
        },
        "id": "player-content",
        "source": "http://cd.ray-live.cn/video/overwatch/mei.mp4",//流地址
        "width": "100%",
        "height": "600px",
        "autoplay": true,
        "isLive": false,//true 直播 false录播
        "rePlay": false,
        "playsinline": true,
        "preload": true,
        "controlBarVisibility": "hover",
        "useH5Prism": true
      }, function (player) {
        console.log("The player is created");
      }
  );
}

onMounted(() => {
  initVideo();
  initWebSocket();
})
//document.getElementById("").insertAdjacentHTML("beforeend", html) ;
</script>


<style scoped>

</style>