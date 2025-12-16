
const vipRouter=[
    {
        path : '/vip',
        name : 'vip',
        children : [
            {
                path : 'customer',
                name : 'customer',
                component: () => import('@/views/vip/customer/index.vue')
            }
        ]
    }
]
export default vipRouter;