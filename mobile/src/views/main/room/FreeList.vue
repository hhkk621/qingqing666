<template>
  <Top/>
  <div class="container-fluid main-content-wrapper">
    <div class="hot-room">

      <div class="row">
        <div class="col-md-2 col-lg-1 card a game-card active" id="game-card--1"><img
            src="http://cd.ray-live.cn/imgs/game/jx.png" @click="viewRoomList(-1)">
          <div class="card-body"><h6 class="card-title">人气推荐</h6></div>
        </div>
        <div v-for="type in room_type_list"
             class="col-md-2 col-lg-1 card a game-card"
             :id="'game-card-'+type.categoryId"
              @click="viewRoomList(type.categoryId)">
          <img :src="type.categoryLogo">
          <div class="card-body">
            <h6 class="card-title">{{type.categoryName}}</h6>
          </div>
        </div>
      </div>
      <div class="row skill-list">
        <div v-for="room in room_list"
             class="col-lg-4 col-md-4 card skill-card">
          <router-link class="video-btn popup-youtube"
                       :to="{ path: '/room/chat' , query: { id: room.id} }">
            <img :src="room.cover">
            <div class="card-body">
              <h5 class="card-title">{{room.name}}</h5>
            </div>
          </router-link>
        </div>

      </div>
    </div>
  </div>
  <Footer/>
</template>

<script setup>
import {ref, onMounted, nextTick} from "vue";
import router from '@/router/index.js'
import api from '@/utils/request.js';
import Top from "@/components/common/Top.vue";
import Footer from "@/components/common/Footer.vue";

let room_type_list = ref([])

let queryTypeList=()=>{
  api.get("/room/type/list").then(result=>{
    if (result.code === 200){
      room_type_list.value = result.data;
      nextTick(()=>{
        //查询推荐房间列表
        viewRoomList(-1)
      })
    }
  })
}

let room_list = ref([])
let viewRoomList=(id)=>{
    //移除 active class的值
    document.querySelectorAll(".game-card").forEach(item =>{
      item.classList.remove("active")
    })
    document.querySelector("#game-card-"+id).classList.add("active")
    api.get("/room/query/"+id).then(result=>{
      room_list.value = result.data;
    })
}
onMounted(()=>{
  queryTypeList();
})

</script>

<style scoped>

</style>