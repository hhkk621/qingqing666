import {ref} from 'vue'
import {defineStore} from "pinia";

export default defineStore('breadStore',()=>{
    const breadcrumb = ref({
        one: '',
        two: '',
    })
    // 新增面包屑层级回退方法
    const popBreadcrumb = () => {
        if (breadcrumb.value.two) {
            breadcrumb.value.two = null
        } else if (breadcrumb.value.one) {
            breadcrumb.value.one = null
        }
    }
    return {
        breadcrumb,
        popBreadcrumb
    }
},{
    persist:{
        storage: sessionStorage,
        paths:['breadcrumb']
    }
})