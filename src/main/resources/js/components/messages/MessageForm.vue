<template>
    <v-layout row>
        <v-text-field
                label="New message"
                placeholder="Write something"
                v-model="text"
                @keyup.enter="save"
        />
        <v-btn @click="save">
            Save
        </v-btn>
    </v-layout>
</template>

<script>
    // import messagesApi from 'api/messages'
    import {mapActions} from 'vuex' // все акшены из стора
  //  import * as Sentry from '@sentry/browser';

    // function getIndex(list, id) {
    //     for (var i = 0; i < list.length; i++ ) {
    //         if (list[i].id === id) {
    //             return i
    //         }
    //     }
    //
    //     return -1
    // }

    export default {
        props: ['messageAttr'],
        data() {
            return {
                text: '',
                id: null
            }
        },
        watch: {
            messageAttr(newVal, oldVal) {
                this.text = newVal.text
                this.id = newVal.id
            }
        },
        methods: {

            ...mapActions(['addMessageAction', 'updateMessageAction']), // добовляем в методы компанента
            save() {
                //   sendMessage({id: this.id, text: this.text}) // выполняем ws функцию
             //   Sentry.captureMessage('Start save ', );

                const message = {
                    id: this.id,
                    text: this.text
                }

                if (this.id) {
                    this.updateMessageAction(message)
                } else {
                    this.addMessageAction(message)
                }


                this.text = ''
                this.id = null

               // throw new Error('bang!')
                // const message = { text: this.text }
                //
                // if (this.id) {
                //     this.$resource('/message{/id}').update({id: this.id}, message).then(result =>
                //         result.json().then(data => {
                //             const index = getIndex(this.messages, data.id)
                //             this.messages.splice(index, 1, data)
                //             this.text = ''
                //             this.id = ''
                //         })
                //     )
                // } else {
                //     this.$resource('/message{/id}').save({}, message).then(result =>
                //         result.json().then(data => {
                //             this.messages.push(data)
                //             this.text = ''
                //         })
                //     )
                // }
            }
        }
    }
</script>

<style>

</style>
