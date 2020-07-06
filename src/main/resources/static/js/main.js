import Vue from 'vue'

import vuetify from 'plugins/vuetify'
import '@babel/polyfill'
import 'api/resource'
import store from 'store/store'
import App from 'pages/App.vue'
import { connect } from './util/ws'

if (frontendData.profile){ // если авторизован
    connect()
}

new Vue({
    vuetify,
    store,
    // el: '#app',
    render: a => a(App)
}).$mount('#app')

