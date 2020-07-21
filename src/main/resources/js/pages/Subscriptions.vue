<template>
    <v-container>
        <v-layout justify-space-around>
            <v-list>
                <v-subheader>subscriber</v-subheader>
<!--                переберам всех подписщиков -->
                <v-list-item-group
                    v-for="(item, index) in subscriptions"
                >
                    <user-link
                            :user="item.subscriber"
                            size="24"
                    ></user-link>
                    <v-btn
                        @click="changeSubscriptionStatus(item.subscriber.id)"
                    >
<!--                        если подписка активная -->
                        {{item.active ? "Dismiss" : "Approve"}}
                    </v-btn>
                </v-list-item-group>
            </v-list>
        </v-layout>
    </v-container>
</template>

<script>
    import profileApi from 'api/profile'
    import UserLink from 'components/UserLink.vue'

    export default {
        name: 'Subscriptions',
        components: {UserLink},
        data() {
            return {
                subscriptions: [] // переменная для подписчиков
            }
        },
        methods: {
            async changeSubscriptionStatus(subscriberId) { // метод активации подписки

                await profileApi.changeSubscriptionStatus(subscriberId) // есил сервер вернет код 200
                console.log("changeSubscriptionStatus Subscriptions.vue" )
                const subscriptionIndex = this.subscriptions.findIndex(item =>
                    item.subscriber.id === subscriberId
                ) // ищем индек текущего усера в подписках

                const subscription = this.subscriptions[subscriptionIndex] // получаем

                this.subscriptions = [ // меняем наш стате
                    ...this.subscriptions.slice(0, subscriptionIndex),
                    {
                        ...subscription,
                        active: !subscription.active
                    },
                    ...this.subscriptions.slice(subscriptionIndex + 1)
                ]
            }
        },
        async beforeMount() { // перед открытием странице
            // апи запрашиваем и заполняем
            console.log("beforeMount Subscriptions.vue" , this.$store.state.profile.id)
            const resp = await profileApi.subscriberList(this.$store.state.profile.id)
            this.subscriptions = await resp.json()
        }
    }
</script>

<style scoped>

</style>
