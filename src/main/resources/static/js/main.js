import Vue from 'vue'

import vuetify from 'plugins/vuetify'

import 'api/resource'

import App from 'pages/App.vue'
import { connect } from './util/ws'

if (frontendData.profile){ // если авторизован
    connect()
}

new Vue({
    vuetify,
    // el: '#app',
    render: a => a(App)
}).$mount('#app')

