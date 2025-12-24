const productRouter = [
    {
        path: '/product',
        name: 'product',
        children: [
            {
                path: 'list',
                name: 'productList',
                component: () => import('@/views/product/Product.vue')
            }
        ]
    },
    {
        path: '/brand',
        name: 'brand',
        children: [
            {
                path: 'list',
                name: 'brandList',
                component: () => import('@/views/product/Brand.vue')
            }
        ]
    },
    {
        path: '/ggtype',
        name: 'list',
        children: [
            {
                path: 'list',
                name: 'ggtypeList',
                component: () => import('@/views/product/GgType.vue')
            }
        ]
    },

]

export default productRouter