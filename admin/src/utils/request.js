import axios from 'axios';
import qs from 'qs';
import router from '@/router/index';

const service = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API || '', // 自动根据环境加载
    timeout: 10000,
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    }
});

// 请求拦截器
service.interceptors.request.use(
    config => {
        // 如果是 post 请求，并且没有设置 Content-Type 或者不是 multipart/form-data
        if (config.method === 'post') {
            // 检查是否已经设置 Content-Type 为 application/json
            if (!config.headers['Content-Type'] || !config.headers['Content-Type'].includes('multipart/form-data')) {
                // 只有当 Content-Type 不是 application/json 时才进行 qs.stringify
                if (!config.headers['Content-Type'] || !config.headers['Content-Type'].includes('application/json')) {
                    config.data = qs.stringify(config.data);
                }
            }
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 响应拦截器
service.interceptors.response.use(
    response => {
        if (response.status === 200) {
            return Promise.resolve(response.data);
        } else {
            return Promise.reject(response);
        }
    },
    error => {
        alert(`异常请求：${JSON.stringify(error.message)}`);
        return Promise.reject(error);
    }
);

export default {
    get(url, data) {
        return service.get(url, { params: data });
    },
    post(url, data) {
        return service.post(url, data);
    },
    post2(url, data) {
        return service.post(url, qs.stringify(data, { indices: false }));
    },
    postJson(url, data) {
        return service.post(url, data, {
            headers: { 'Content-Type': 'application/json' }
        });
    },
    postFile(url, data) {
        return service.post(url, data, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },
    put(url, data) {
        return service.put(url, data);
    },
    putJson(url, data) {
        return service.put(url, data, {
            headers: { 'Content-Type': 'application/json' }
        });
    },
    delete(url, data) {
        return service.delete(url, { params: data });
    },


    // 🔥 新增：下载文件方法
    download(url, data, fileName = '') {
        return service({
            method: 'get',
            url: url,
            params: data,
            responseType: 'blob'  // 指定响应类型
        }).then(response => {
            // 获取文件名
            let downloadFilename = fileName;
            if (!downloadFilename && response.headers['content-disposition']) {
                const contentDisposition = response.headers['content-disposition'];
                const filenameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
                if (filenameMatch && filenameMatch[1]) {
                    downloadFilename = decodeURIComponent(filenameMatch[1].replace(/['"]/g, ''));
                }
            }

            return {
                data: response.data,  // blob数据
                filename: downloadFilename || 'download'
            };
        });
    }
};
