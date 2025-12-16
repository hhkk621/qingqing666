import {ref} from 'vue'
import {defineStore} from "pinia";

export default defineStore('unameStore',()=>{
    const userModel = ref({
        username: '',
        password: '',
        remember: false
    })
    return {
        userModel
    }
},{
    persist:{
        storage: localStorage,
        paths:['userModel']
    }
})