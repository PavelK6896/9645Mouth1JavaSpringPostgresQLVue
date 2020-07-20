import Vue from 'vue'

import vuetify from 'plugins/vuetify'
import '@babel/polyfill'
import 'api/resource'
import router from 'router/router'
import store from 'store/store'
import App from 'pages/App.vue'
import {connect} from 'util/ws'
import * as Sentry from '@sentry/browser';
import { Vue as VueIntegration } from '@sentry/integrations';

try {
    Sentry.init({
        dsn: 'https://30',
        integrations: [new VueIntegration({Vue, attachProps: true})],
    });
    Sentry.setUser({id: profile && profile.id, username: profile && profile.username}); //&& - проверка что существует
}catch (e) {}

if (profile) { // если авторизован
    connect()
}

new Vue({
    vuetify,
    store,
    router,
    // el: '#app',
    render: a => a(App)
}).$mount('#app')

