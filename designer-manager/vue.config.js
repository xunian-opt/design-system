const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    port: 8081, // 前端运行端口
    proxy: {
      '/springboot2z04j': {
        target: 'http://localhost:8080', // 后端地址
        changeOrigin: true,
        pathRewrite: {
          // 如果后端接口直接就是 /springboot2z04j 开头，不需要重写
          // '^/api': '' 
        }
      }
    }
  }
})