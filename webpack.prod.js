const path = require('path'); // получаем утилиту для путей
const {merge} = require('webpack-merge');
const common = require('./webpack.common.js');
const {CleanWebpackPlugin} = require('clean-webpack-plugin');

module.exports = merge(common, {
    mode: 'production',
    plugins: [
        new CleanWebpackPlugin(), // чистит пакву вывода
    ],
    output: {
        filename: 'main.js',
        path: path.resolve(__dirname, 'src', 'main', 'resources', 'static', 'js'), // путь к папки
    },
});