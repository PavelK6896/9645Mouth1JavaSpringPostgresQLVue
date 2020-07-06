<template>
    <v-layout align-space-around justify-start column>
        <message-form  :messageAttr="message" />
        <message-row v-for="message in sortedMessages"
                     :key="message.id"
                     :message="message"
                     :editMessage="editMessage"
                     />
    </v-layout>
</template>

<script>
    import { mapGetters } from 'vuex'
    import MessageRow from 'components/messages/MessageRow.vue'
    import MessageForm from 'components/messages/MessageForm.vue'
    import messagesApi from 'api/messages'

    export default {
        //props: ['messages'],
        components: {
            MessageRow,
            MessageForm
        },
        data() {
            return {
                message: null
            }
        },
        computed: mapGetters(['sortedMessages']), // гетер из стора
        // computed: { // выполняеться когда меняесться поля
        //     // sortedMessages() {
        //     //     return this.messages.sort((a, b) => -(a.id - b.id)) // сортируем по id
        //     // }
        // },
        methods: {
            editMessage(message) {
                this.message = message
            },
            // deleteMessage(message) {
            //     //this.$resource('/message{/id}') // create new obj vue
            //     messagesApi.remove(message.id).then(result => {
            //         if (result.ok) {
            //             this.messages.splice(this.messages.indexOf(message), 1)
            //         }
            //     })
            // }
        }
    }
</script>

<style>

</style>
