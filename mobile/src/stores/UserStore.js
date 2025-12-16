import {ref, reactive, getCurrentInstance, onMounted} from "vue";
import {defineStore} from "pinia";

export default defineStore('userStore', () => {
        const userModel = ref({
            uid: -1,
            userInfo:{
                headPic: "http://cd.ray-live.cn/imgs/headpic/pic_54.jpg"
            }
        })
        return {
            userModel
        }
    }, {
        persist: {
            storage: sessionStorage,
            paths: ['userModel']
        }
    }
)