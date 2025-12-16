import {ref} from 'vue'
import {defineStore} from "pinia";

export default defineStore('userStore',()=>{
    const userModel = ref({
        uid: -1,//未登录,
        role:{
            menuList:[]
        }
    })
    return {
        userModel
    }
},{
    persist:{
        storage: sessionStorage,
        paths:['userModel']
    }
})