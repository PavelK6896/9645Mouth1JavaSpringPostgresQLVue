<template>
    <v-app>

        <v-app-bar app>
            <v-toolbar-title>Mouth</v-toolbar-title>
            <v-btn v-if="profile" text :disabled="$route.path === '/'" @click="showMessages">
                Messages
            </v-btn>

            <v-spacer></v-spacer>

            <v-btn text v-if="profile" :disabled="$route.path === '/profile'" @click="showProfile">{{profile.name}}</v-btn>


            <v-btn v-if="profile" icon href="/logout">

                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-app-bar>

        <v-main>
            <router-view></router-view>
<!--            <v-container v-if="!profile">-->
<!--                Необходимо авторизоваться через-->
<!--                <a href="/login">Google</a>-->
<!--            </v-container>-->

<!--            <v-container v-if="profile">-->
<!--                <messages-list />-->
<!--            </v-container>-->
        </v-main>


    </v-app>
</template>

<script>
    import { mapState, mapMutations } from 'vuex' // состояние гетер мутация действие
    // import MessagesList from 'components/messages/MessageList.vue'
    import {addHandler} from 'util/ws'



    export default {
        // components: { // регистрация компанента
        //     MessagesList
        // },
        computed: mapState(['profile']), // получаем из стора
        methods: {
            ...mapMutations([
                'addMessageMutation',
                'updateMessageMutation',
                'removeMessageMutation',
                'addCommentMutation'

            ]),
            showMessages(){
                this.$router.push('/')
            },
            showProfile(){
                this.$router.push('/profile')
            }





        },   // обычьные мутации






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
                    } else if (data.objectType === 'COMMENT') {

                        switch (data.eventType) {
                            case 'CREATE':
                                this.addCommentMutation(data.body)
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
        },
        beforeMount() { // хук после копиляции html
            if (!this.profile) { // перенапровление если не авторизован // чтоб не видеть ошибки
                this.$router.replace('/auth') //replace запись не создает
            }
        }

    }
</script>

<style>

</style>
