<template>
    <v-layout row>
        <v-text-field
                label="New message"
                placeholder="Write something"
                v-model="text"
        />
        <v-btn @click="save">
            Save
        </v-btn>
    </v-layout>
</template>

<script>
    import messagesApi from 'api/messages'

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
        props: ['messages', 'messageAttr'],
        data() {
            return {
                text: '',
                id: ''
            }
        },
        watch: {
            messageAttr(newVal, oldVal) {
                this.text = newVal.text
                this.id = newVal.id
            }
        },
        methods: {
            save() {
             //   sendMessage({id: this.id, text: this.text}) // выполняем ws функцию

                const message = {
                    id: this.id,
                    text: this.text
                }

                if (this.id) {
                    messagesApi.update(message).then(result =>
                        result.json().then(data => {
                            const index = this.messages.findIndex(item => item.id === data.id)
                            this.messages.splice(index, 1, data)
                        })
                    )
                } else {
                    messagesApi.add(message).then(result =>
                        result.json().then(data => {
                            const index = this.messages.findIndex(item => item.id === data.id)

                            if (index > -1) {
                                this.messages.splice(index, 1, data)
                            } else {
                                this.messages.push(data)
                            }
                        })
                    )
                }



                this.text = ''
                this.id = ''


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
