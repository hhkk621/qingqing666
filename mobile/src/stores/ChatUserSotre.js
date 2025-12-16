import {ref} from 'vue'
import {defineStore} from "pinia";

export default defineStore('chatUserStore',()=>{
    const user = ref({
        uid: -1,
        gid: 1,
        nickname: "",
        headPic: "",
        price: 0
    })
    return {
        user
    }
},{
    persist:{
        storage: sessionStorage,
        paths:['user']
    }
})