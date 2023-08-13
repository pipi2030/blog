const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        port: 8081,//它是用来修改你打开后的端口号的
        open: true,//值为 true的话，项目启动时自动打开到浏览器里边， false不会打开
        proxy:{
            '/api-db':{
                target:'http://192.168.44.199:9080',//跨域请求的公共地址
                ws:false, //也可以忽略不写，不写不会影响跨域
                changeOrigin:true, //是否开启跨域，值为 true 就是开启， false 不开启
                pathRewrite:{
                    '^/api-db':'/db'
                }
            },
            '/api-es':{
                target:'http://192.168.44.199:9080',//跨域请求的公共地址
                ws:false, //也可以忽略不写，不写不会影响跨域
                changeOrigin:true, //是否开启跨域，值为 true 就是开启， false 不开启
                pathRewrite:{
                    '^/api-es':'/es'
                }
            }
        }
    },
});
