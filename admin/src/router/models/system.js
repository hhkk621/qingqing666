

const systemRouter=[
    {
        path : '/role',
        name : 'role',
        children : [
            {
                path : 'list',
                name : 'roleList',
                component: () => import('@/views/system/role/index.vue')
            }
        ]
    },
    {
        path : '/menu',
        name : 'menu',
        children : [
            {
                path : 'list',
                name : 'menuList',
                component: () => import('@/views/system/menu/index.vue')
            }
        ]
    },
    {
        path : '/log',
        name : 'log',
        children : [
            {
                path : 'list',
                name : 'LogList',
                component: () => import('@/views/system/log/Log.vue')
            }
        ]
    },

]
export default systemRouter;