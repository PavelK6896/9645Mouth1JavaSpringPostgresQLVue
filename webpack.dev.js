const {merge} = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
    mode: 'development', // разработка
    devtool: 'source-map', // без заморочек
    devServer: {
        contentBase: './dist', // деректория скомпилированных файлов
        compress: true,
        port: 8000, // порт работы
        allowedHosts: [
            'localhost:8080' // порт приема запросов
        ],
        stats: 'errors-only', // видеть логи только по статусу
        clientLogLevel: 'error', //
    }
});

