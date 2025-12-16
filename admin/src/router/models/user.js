
const userRouter=[
    {
        path : '/user',
        name : 'user',
        children : [
            {
                path : 'list',
                name : 'userList',
                component: () => import('@/views/system/user/index.vue')
            },{
                path : 'info',
                name : 'userInfo',
                component: () => import('@/views/system/user/info.vue')
            }
        ]
    }
]
export default userRouter;