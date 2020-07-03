<template>
    <div>
    <div v-if="!profile">Необходимо авторизоваться через
        <a href="/login">Google</a>
    </div>
    <div v-else>
        <div>{{profile.name}}&nbsp;<a href="/logout">Выйти</a></div>
        <messages-list :messages="messages" />
        </div>
    </div>
</template>

<script>
    import MessagesList from 'components/messages/MessageList.vue'
    import { addHandler } from 'util/ws'
    import { getIndex } from 'util/collections'


    export default {
        components: { // регистрация компанента
            MessagesList
        },
        data() { // функция чтобы на каждый экземпляр был свои даные
            return {
                messages: frontendData.messages,
                profile: frontendData.profile
            }
        },
        created() { // хук после инициализаци обьектов
            addHandler(data => {
                let index = getIndex(this.messages, data.id) // ищем сообщение
                if (index > -1) {
                    this.messages.splice(index, 1, data) // заменяем старое сообщение
                } else { // когда поевляеться новое сообщение
                    this.messages.push(data)
                }
            })
        }
    }
</script>

<style>

</style>
