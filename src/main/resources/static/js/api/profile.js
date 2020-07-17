import Vue from 'vue'

const profile = Vue.resource('/profile{/id}')

export default {
    get: id => profile.get({id}),
    // 1 получаем сиреализованный ручьми профиль
    changeSubscription: channelId => Vue.http.post(`/profile/change-subscription/${channelId}`),
    // get запрос на
    subscriberList: channelId => Vue.http.get(`/profile/get-subscribers/${channelId}`),
    // пост запрос
    changeSubscriptionStatus: subscriberId => Vue.http.post(`/profile/change-status/${subscriberId}`)
}
