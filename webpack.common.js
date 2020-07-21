const path = require('path'); // получаем утилиту для путей
const VueLoaderPlugin = require('vue-loader/lib/plugin'); // для компанентов

module.exports = {

    entry: path.join(__dirname, 'src', 'main', 'resources',  'js', 'main.js'), // начало сборки проекта

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
            path.join(__dirname, 'src', 'main', 'resources',  'js'),
            path.join(__dirname, 'node_modules'),
        ],
    }
}
