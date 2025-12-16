const aiRouter =[
    {
        path: '/ai',
        name: 'airoom',
        children: [
            {
                path: 'index',
                name: 'ai_index',
                component: import('@/views/main/message/index.vue')
            }
        ]
    },
]
export default aiRouter;