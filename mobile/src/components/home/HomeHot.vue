<template>
  <div class="hot-room">
    <div class="row">
      <h1>人气推荐</h1>
    </div>
    <div class="row">
      <div v-for="game in game_list"
           class="col-md-2 col-lg-1 card a game-card"
           :id="'game-card-'+game.id" @click="viewUser(game.id)">
        <img :src="game.icon">
        <div class="card-body">
          <h6 class="card-title">{{game.name}}</h6>
        </div>
      </div>
    </div>
    <div class="row skill-list">
      <div v-for="user in game_user_list"
           class="col-lg-2 col-md-4 card skill-card"
            @click="jumpAiMessage(user)">
        <img :src="user.headPic">
        <div class="card-body">
          <h5 class="card-title">{{user.nickname}}</h5>
        </div>
        <div class="card-body row">
          <div class="col">
            <span class="bg-success text-white p-1">{{user.gameName}}</span>
          </div>
          <div class="col">
            <span class="text-danger a">{{user.price}}/小时</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, nextTick} from "vue";
import router from '@/router/index'
import api from '@/utils/request.js'
import UserHomeGameStore from "@/stores/UserHomeGameStore.js";

let game_list = ref([])
let game_user_list=ref([])
import ChatUserSotre from "@/stores/ChatUserSotre.js";
let jumpAiMessage =(user)=>{
    ChatUserSotre().user = user;
    router.push("/ai/index")
}
let viewUser = (gameId) => {
  //移除game-card的class属性 active
  document.querySelectorAll(".game-card").forEach(item => {
    item.classList.remove("active")
  })
  //添加当前game-card的class属性 active
  document.querySelector("#game-card-" + gameId).classList.add("active")
  game_user_list.value = game_map.value.get(gameId);
}

let query=()=>{
  //先去SessionStorage中查询数据
  if (UserHomeGameStore().gameList.length > 0){
    //本地有数据，已经缓存了
    game_list.value = UserHomeGameStore().gameList;
    createMap(UserHomeGameStore().gameList)
    nextTick(()=>{
      viewUser(-1)
    })
  }else {
    api.get("/home/game").then(result=>{
      //本页循环需要使用的数据
      game_list.value = result.data;
      //本地SessionStorage缓存数据
      UserHomeGameStore().gameList = result.data;
      createMap(result.data)
      nextTick(()=>{
        viewUser(-1)
      })
    })
  }

}
let game_map = ref({})
function createMap(list){
  let map = new Map();
  list.forEach(game=>{
    map.set(game.id,game.gameSkillList);
  })
  game_map.value = map;
}

onMounted(() => {
  query();
});
</script>

<style scoped>

</style>