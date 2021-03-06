import Vue from 'vue'
import Vuex from 'vuex'
import messagesApi from 'api/messages'
import commentApi from 'api/comment'

Vue.use(Vuex)

export default new Vuex.Store({
    state: { // начальный стор
        messages,
        profile,
        ...frontendData
    },
    getters: { // прослушка
        sortedMessages: state => (state.messages || []).sort((a, b) => -(a.id - b.id))
    },
    mutations: { // запрос на изменение
        addMessageMutation(state, message) {
            state.messages = [
                ...state.messages,
                message
            ]
        },
        updateMessageMutation(state, message) {
            const updateIndex = state.messages.findIndex(item => item.id === message.id)

            state.messages = [ // вставка в середину
                ...state.messages.slice(0, updateIndex),
                message,
                ...state.messages.slice(updateIndex + 1)
            ]
        },
        removeMessageMutation(state, message) {
            const deletionIndex = state.messages.findIndex(item => item.id === message.id)

            if (deletionIndex > -1) {
                state.messages = [
                    ...state.messages.slice(0, deletionIndex),
                    ...state.messages.slice(deletionIndex + 1)
                ]
            }
        },
        addCommentMutation(state, comment) {
            const updateIndex = state.messages.findIndex(item => item.id === comment.message.id)
            const message = state.messages[updateIndex]

            if (message.comments === null) {
                state.messages = [
                    ...state.messages.slice(0, updateIndex),
                    {
                        ...message,
                        comments: [
                            comment
                        ]
                    },
                    ...state.messages.slice(updateIndex + 1)
                ]

            } else {
                if (!message.comments.find(it => it.id === comment.id)) {
                    state.messages = [
                        ...state.messages.slice(0, updateIndex),
                        {
                            ...message,
                            comments: [
                                ...message.comments,
                                comment
                            ]
                        },
                        ...state.messages.slice(updateIndex + 1)
                    ]
                }
            }
        },
        addMessagePageMutation(state, messages) {
            const targetMessages = state.messages
                .concat(messages)
                .reduce((res, val) => { // убирает дубликат
                    res[val.id] = val
                    return res
                }, {})

            state.messages = Object.values(targetMessages) // получаем значение
        },
        updateTotalPagesMutation(state, totalPages) {
            state.totalPages = totalPages
        },
        updateCurrentPageMutation(state, currentPage) {
            state.currentPage = currentPage
        }

    },
    actions: {
        async addMessageAction({commit, state}, message) {

            const result = await messagesApi.add(message)// получаем с сервера
            const data = await result.json()
            console.log("data store.js " , data )

            const index = state.messages.findIndex(item => item.id === data.id) // ищем в jsone по id

            if (index > -1) {
                commit('updateMessageMutation', data) // вызываем мутацию
            } else {
                commit('addMessageMutation', data)
            }
        },
        async updateMessageAction({commit}, message) {
            console.log("updateMessageAction store.js " , message )

            const result = await messagesApi.update(message)
            const data = await result.json()
            commit('updateMessageMutation', data)
        },
        async removeMessageAction({commit}, message) {
            console.log("removeMessageAction store.js " , message )

            const result = await messagesApi.remove(message.id) // запрос на сервер

            if (result.ok) { // промис вернул ок
                commit('removeMessageMutation', message)
            }
        },
        async addCommentAction({commit, state}, comment) {
            console.log("addCommentAction store.js " , comment )

            const response = await commentApi.add(comment) // отпровляем на сервер по апи
            const data = await response.json()
            commit('addCommentMutation', data)
        },
        async loadPageAction({commit, state}) {
            console.log("loadPageAction store.js " , state.currentPage )

            const response = await messagesApi.page(state.currentPage + 1)
            const data = await response.json()

            commit('addMessagePageMutation', data.messages)
            commit('updateTotalPagesMutation', data.totalPages)
            commit('updateCurrentPageMutation', Math.min(data.currentPage, data.totalPages - 1)) // запрашиваем минимум

        }

    }
})
