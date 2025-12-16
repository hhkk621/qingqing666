import {createRouter,createWebHistory} from "vue-router";
import userStore from "@/stores/LoginUser.js";

import errorRouter from "@/router/models/error.js";
import userRouter from "@/router/models/user.js";
import systemRouter from "@/router/models/system.js";
import vipRouter from "@/router/models/vip.js";


export const constantRoutes=[
    {
        path: '/login',
        component: () => import('@/views/global/login.vue')
    },
    // 带布局的主路由
    {
        path: '/',
        component: () => import('@/views/global/Home.vue'), // 包含菜单的布局组件
        children: [
            {
                path: '/index',
                name: 'Index',
                component: () => import('@/views/global/index.vue'),
                meta: { requiresAuth: true }
            },
            ...userRouter,
            ...systemRouter,
            ...vipRouter,
        ]
    },
    ...errorRouter,
]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: constantRoutes,
    base: '/admin/',
})

//放过的网址白名单  哪些地址可以不登录就能访问
const whiteList = ['/login'];

//在这里要写一个拦截器,beforeEach每次通过路由的请求,都要经过这个方法
//to 用户访问的路径
//from 跳转过来的路径
//next 下一站对象
router.beforeEach((to,from,next)=>{
    //判断当前用户是否登录,如果未登录,跳转到登录页面
    if (to.matched.length === 0){
        next('/404')
    } else if (whiteList.includes(to.path)){
        //白名单的用户
        next();
    } else {
        //console.log(userStore().userModel)
        //检查 是否已经登录
        if (userStore().userModel.uid !== -1){
            //已经登录了
            next();
        }else {
            //next("/login")
            next();
        }
    }

})

export default router