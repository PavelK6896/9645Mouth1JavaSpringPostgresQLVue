import Vue from 'vue'

const messages = Vue.resource('/message{/id}') // get post put delete

export default {
    add: (message) => {
       return fetch(
            '/message',
            {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(message)
            }
        )
    },

    update: (message) => {
        return fetch(
            '/message/' + message.id,
            {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(message)
            }
        )
    },

    remove: id => messages.remove({id}),
    page: page => Vue.http.get('/message', {params: {page}}) // номер странице
}
