

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
    }
]
export default systemRouter;