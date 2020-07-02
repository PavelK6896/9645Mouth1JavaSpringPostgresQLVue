import Vue from 'vue'
import VueResource from 'vue-resource'
import App from 'pages/App.vue'

Vue.use(VueResource) // добавления ву ресурса для рест апиа

new Vue({
    el: '#app', // в элемент app поместить компанет App
    render: a => a(App)
})
