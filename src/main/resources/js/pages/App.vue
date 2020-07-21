<template>
    <v-app>
        <v-app-bar app>
            <v-toolbar-title>Mouth</v-toolbar-title>
            <v-btn v-if="profile" text :disabled="$route.path === '/'" @click="showMessages">
                Messages
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn text v-if="profile" :disabled="$route.path === '/profile'" @click="showProfile">
                {{profile.name}}</v-btn>
            <v-btn v-if="profile" icon href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-app-bar>
        <v-main>
            <router-view></router-view>
        </v-main>
    </v-app>
</template>

<script>
    import {mapMutations, mapState} from 'vuex' // состояние гетер мутация действие
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
            showMessages() {
                this.$router.push('/')
            },
            showProfile() {
                this.$router.push('/profile')
            }


        },

        created() { // хук после инициализаци обьектов

            addHandler(data => {

                    console.log("addHandler app.vue", data)

                    if (data.objectType === 'MESSAGE') {

                        switch (data.eventType) {
                            case 'CREATE':
                                console.log("MESSAGE 1111111111 app.vue")
                                this.addMessageMutation(data.body)
                                console.log("MESSAGE 222222222 app.vue")
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
                                console.log("COMMENT CREATE app.vue")

                                break
                            default:
                                console.error(`Looks like the event type if unknown "${data.eventType}"`)
                        }
                    } else {
                        console.error(`Looks like the object type if unknown "${data.objectType}"`)
                    }
                }
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
