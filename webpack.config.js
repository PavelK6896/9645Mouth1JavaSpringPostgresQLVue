const path = require('path'); // получаем утилиту для путей
const VueLoaderPlugin = require('vue-loader/lib/plugin'); // для компанентов

module.exports = {
    mode: 'development', // разработка
    devtool: 'source-map', // без заморочек
    entry: path.join(__dirname, 'src', 'main', 'resources', 'static', 'js', 'main.js'), // начало сборки проекта
    devServer: {
        contentBase: './dist', // деректория скомпилированных файлов
        compress: true,
        port: 8000, // порт работы
        allowedHosts: [
            'localhost:8080' // порт приема запросов
        ],
        stats: 'errors-only', // видеть логи только по статусу
        clientLogLevel: 'error', //
    },
    module: {
        rules: [
            { // 1 первое правило все js через bable
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            },
            { // 2 второе все ву черз ву лоадер
                test: /\.vue$/,
                loader: 'vue-loader'
            },
            { // для css
              test: /\.css$/,
              use: [
                'vue-style-loader',
                'css-loader'
              ]
            },

            // {
            //     test: /\.s(c|a)ss$/,
            //     use: [
            //         'vue-style-loader',
            //         'css-loader',
            //         {
            //             loader: 'sass-loader',
            //             // Requires sass-loader@^7.0.0
            //             options: {
            //                 implementation: require('sass'),
            //                 fiber: require('fibers'),
            //                 indentedSyntax: true // optional
            //             },
            //             // Requires sass-loader@^8.0.0
            //             options: {
            //                 implementation: require('sass'),
            //                 sassOptions: {
            //                     fiber: require('fibers'),
            //                     indentedSyntax: true // optional
            //                 },
            //             },
            //         },
            //     ],
            // }


        ]
    },
    plugins: [
        new VueLoaderPlugin()
    ],
    resolve: { // модули
        modules: [
            path.join(__dirname, 'src', 'main', 'resources', 'static', 'js'),
            path.join(__dirname, 'node_modules'),
        ],
    }
}
