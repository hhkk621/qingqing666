import {ref} from 'vue'
import {defineStore} from "pinia";

export default defineStore('tokenStore',()=>{
    const token = ref("")
    return {
        token
    }
},{
    persist:{
        storage: localStorage,
        paths:['token']
    }
})