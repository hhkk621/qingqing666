let webSocket;

export default {
    //初始化socket连接
    initWebSocket(url){
        let baseUrl = import.meta.env.VITE_APP_WebSocket_BASE_API;
        ///room/mws://127.0.0.1:8080ultiuser/{roomId}/{uid}

        if ('WebSocket' in  window){
            //判断当前浏览器是否支持WebSocket
            webSocket = new WebSocket(baseUrl + url);
            //设置二进制类型是arraybuffer
            webSocket.binaryType='arraybuffer';
        }
        //连接WebSocket，在客户端发生的事情
        webSocket.onopen = function (){
            console.log("连接成功！")
        }
        //当连接关闭的时候，执行的方法
        webSocket.onclose = function (){
            console.log("断开 连接")
        }
        window.onbeforeunload = function (){
            //当页面刷新的时候，主动关闭连接
            webSocket.close(3000,"关闭");
        }


    },
    getWebSocket(){
        return webSocket;
    }
}