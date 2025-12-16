import {ref, reactive, getCurrentInstance, onMounted} from "vue";
import {defineStore} from "pinia";

export default defineStore('userHomeGameStore', () => {
        const gameList = ref([])
        return {
            gameList
        }
    }, {
        persist: {
            storage: sessionStorage,
            paths: ['gameList']
        }
    }
)