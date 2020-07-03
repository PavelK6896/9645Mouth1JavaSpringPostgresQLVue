import Vue from 'vue'
import VueResource from 'vue-resource'
import App from 'pages/App.vue'
import { connect } from './util/ws'


import vuetify from 'plugins/vuetify'


////
if (frontendData.profile){ // если авторизован
    connect()
}

Vue.use(VueResource) // добавления ву ресурса для рест апиа


new Vue({
    vuetify,
    // el: '#app',
    render: a => a(App)
}).$mount('#app')

