import Vue from 'vue'
import VueRouter from 'vue-router'
import MessagesList from 'pages/MessageList.vue'
import Auth from 'pages/Auth.vue'
import Profile from 'pages/Profile.vue'

Vue.use(VueRouter)

const routes = [
    {path: '/', component: MessagesList},
    {path: '/auth', component: Auth},
    {path: '/user/:id?', component: Profile}, // :id динамическая переменная //? данный параметор необязателен
    {path: '*', component: MessagesList}, // redirect
]

export default new VueRouter({
    mode: 'history', // # delete
    routes
})
