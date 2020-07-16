<template>
    <v-container>
        <v-layout justify-space-around>
            <v-flex :xs6="!$vuetify.breakpoint.xsOnly">
                <div class="title mb-3">User profile</div>
                <v-layout row justify-space-between>
                    <v-flex class="px-1">
                        <v-img :src="profile.userpic"></v-img>
                    </v-flex>
                    <v-flex class="px-1">
                        <v-layout column>
                            <v-flex>{{profile.name}}</v-flex>
                            <v-flex>{{profile.locale}}</v-flex>
                            <v-flex>{{profile.gender}}</v-flex>
                            <v-flex>{{profile.lastVisit}}</v-flex>
                            <v-flex>{{profile.subscriptions && profile.subscriptions.length}} subscriptions</v-flex>
                            <v-flex>{{profile.subscribers && profile.subscribers.length}} subscribers</v-flex>
                        </v-layout>
                    </v-flex>
                </v-layout>
                <v-btn
                        v-if="!isMyProfile"
                        @click="changeSubscription"
                >
                    {{isISubscribed ? 'Unsubscribe' : 'Subscribe'}}
                </v-btn>
            </v-flex>
        </v-layout>
    </v-container>
</template>
<script>
    //import {mapState} from 'vuex'
    import profileApi from 'api/profile' // 2 полючаем

    export default {
        name: 'Profile',
        //computed: mapState(['profile']) // берет из стора
        data() {
            return {
                profile: {} // задаем профель
            }
        },
        computed: {
            isMyProfile() { // проверям что это собственный профиль
                return !this.$route.params.id ||
                    this.$route.params.id === this.$store.state.profile.id
            },
            isISubscribed() {
                return this.profile.subscribers && // есть ли подписки
                    this.profile.subscribers.find(subscription => { // если уже подписан
                        return subscription.subscriber === this.$store.state.profile.id
                    })
            }
        },
        watch: {
            '$route'() { // ели меняем роут
                this.updateProfile()
            }
        },
        methods: {
            async changeSubscription() { // 3 заполняет профель через апи
                const data = await profileApi.changeSubscription(this.profile.id)
                this.profile = await data.json()
            },
            async updateProfile() {
                const id = this.$route.params.id || this.$store.state.profile.id // берем ид из урла или профиля

                const data = await profileApi.get(id)
                this.profile = await data.json() // заполняем профиль

                this.$forceUpdate() // обновляем
            }
        },
        beforeMount() {
            this.updateProfile() // обновляем профиль
        }
    }
</script>

<style scoped>
    img {
        max-width: 100%;
        height: auto;
    }
</style>
