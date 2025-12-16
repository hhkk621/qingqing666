
const errorRouter=[
    {
        path : '/401',
        name : '401',
        component: () => import('@/views/error/401.vue')
    },{
        path : '/404',
        name : '404',
        component: () => import('@/views/error/404.vue')
    }
]
export default errorRouter;