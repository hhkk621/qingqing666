import { createRouter, createWebHistory } from 'vue-router'
import  loginUser from '@/stores/UserStore';
import systemRouter from './models/system'
import roomRouter from './models/room'
import aiRouter from './models/ai'

export const constantRoutes=[
    ...systemRouter,
    ...roomRouter,
    ...aiRouter,
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoutes
})


//白名单
const whiteList = ['/login','/404','/'];
router.beforeEach((to,
                   from,
                   next)=>{
    //寻找 用户 想访问的页面,如果找不到 跳转到 404
    if (to.matched.length === 0){
        next('/404');
    }else {
        let login = loginUser().userModel;
        //登录了正常访问
        if (login.uid !== -1 || whiteList.includes(to.path)){
            next()
        }else {
            //如果用户不登录,跳转到登录页面
            //next("/login")
           next();
        }
    }
    //下一站,执行了next方法,会跳转到 to的页面
    //next();
})


export default router