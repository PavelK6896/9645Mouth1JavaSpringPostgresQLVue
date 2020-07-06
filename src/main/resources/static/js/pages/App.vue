<template>
    <v-app>

        <v-app-bar app>
            <v-toolbar-title>Mouth</v-toolbar-title>

            <v-spacer></v-spacer>

            <span v-if="profile">{{profile.name}}</span>


            <v-btn v-if="profile" icon href="/logout">

                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-app-bar>

        <v-main>

            <v-container v-if="!profile">
                Необходимо авторизоваться через
                <a href="/login">Google</a>
            </v-container>

            <v-container v-if="profile">
                <messages-list />
            </v-container>
        </v-main>

    </v-app>
</template>

<script>
    import { mapState, mapMutations } from 'vuex' // состояние гетер мутация действие
    import MessagesList from 'components/messages/MessageList.vue'
    import {addHandler} from 'util/ws'



    export default {
        components: { // регистрация компанента
            MessagesList
        },
        computed: mapState(['profile']), // получаем из стора
        methods: mapMutations(['addMessageMutation', 'updateMessageMutation', 'removeMessageMutation']), // обычьные мутации
        // data() { // функция чтобы на каждый экземпляр был свои даные
        //     return {
        //         messages: frontendData.messages,
        //         profile: frontendData.profile
        //     }
        // },
        created() { // хук после инициализаци обьектов
            addHandler(data =>

                {
                    if (data.objectType === 'MESSAGE') {
                        // const index = this.messages.findIndex(item => item.id === data.body.id)

                        switch (data.eventType) {
                            case 'CREATE':
                                this.addMessageMutation(data.body)

                                break
                            case 'UPDATE':
                                this.updateMessageMutation(data.body)
                                break
                            case 'REMOVE':
                                this.removeMessageMutation(data.body)
                                break
                            default:
                                console.error(`Looks like the event type if unknown "${data.eventType}"`)
                        }
                    } else {
                        console.error(`Looks like the object type if unknown "${data.objectType}"`)
                    }
                }

            // {
            //     let index = getIndex(this.messages, data.id) // ищем сообщение
            //     if (index > -1) {
            //         this.messages.splice(index, 1, data) // заменяем старое сообщение
            //     } else { // когда поевляеться новое сообщение
            //         this.messages.push(data)
            //     }
            // }

            )
        }
    }
</script>

<style>

</style>
