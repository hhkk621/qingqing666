const roomRouter =[
    {
        path: '/room',
        name: 'room',
        children: [
            {
                path: 'free',
                name: 'free_room',
                component: import('@/views/main/room/FreeList.vue')
            },
            {
                path: 'chat',
                name: 'room_chat',
                component: import('@/views/main/room/Chat.vue')
            }
        ]
    },
]
export default roomRouter;