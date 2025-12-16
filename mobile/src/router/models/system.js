import HomeView from '@/views/system/Home.vue'
import LoginView from '@/views/system/Login.vue'
import Register from '@/views/system/Register.vue'
import page404 from '@/views/system/404.vue'

const systemRouter =[
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/login',
        name: 'login',
        component: LoginView
    },
    {
        path: '/reg',
        name: 'register',
        component: Register
    },
    {
        path: '/404',
        name: '404',
        component: page404
    }
]
export default systemRouter;