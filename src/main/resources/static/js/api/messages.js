import Vue from 'vue'

const messages = Vue.resource('/message{/id}') // get post put delete

export default {
    add: message => messages.save({}, message),
    update: message => messages.update({id: message.id}, message),
    remove: id => messages.remove({id}),
    page: page => Vue.http.get('/message', {params: { page }}) // номер странице
}
