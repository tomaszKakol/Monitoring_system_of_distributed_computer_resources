module.exports = {
    // options...
    devServer: {
        proxy: {
            '^/conn': {
                //target: 'http://localhost:8080',
                target: 'http://127.0.0.1:8080',
                ws: true,
                changeOrigin: true
            }
        }
    }
}
