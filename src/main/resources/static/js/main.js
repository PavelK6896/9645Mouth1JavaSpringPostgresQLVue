function getIndex(list, id) { //определяет индекс в колекции
    for (var i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}


var messageApi = Vue.resource('/message{/id}'); //для rest

Vue.component('message-form', { //форма для добавления  и изменения
    props: ['messages', 'messageAttr'], //
    data: function () { // вункция возвращает объект что бы переменые была уникальная внутри компанента
        return {//стате в компаненте ву локальный
            text: '',
            id: ''
        }
    },
    watch: { //следит за messageAttr или обновляет стате
        messageAttr: function (newVal, oldVal) {
            this.text = newVal.text; //устанавливает новое значание
            this.id = newVal.id;
        }
    },
    template:
        '<div>' + //фома в ву для ресурсе
        '<input type="text" placeholder="Write something" v-model="text" />' +//v-model связывает объект с локальным стетом
        '<input type="button" value="Save" @click="save" />' + //v-on:click или @click вызывает метод
        '</div>',
    methods: {
        save: function () { //метод для кнопки как сабмит
            const message = {text: this.text};//сообщение

            if (this.id) { //если обнавление
                messageApi.update({id: this.id}, message).then(result =>
                    result.json().then(data => {
                        const index = getIndex(this.messages, data.id); //находим индекс
                        this.messages.splice(index, 1, data); // меняем элемент по индексу
                        this.text = ''// очищаем форму ввода
                        this.id = ''// очищаем форму ввода
                    })
                )
            } else {
                messageApi.save({}, message).then(result => // метод save post запрос
                    result.json().then(data => { // верныл созданые объект с id
                        this.messages.push(data); //добавляем в наш список
                        this.text = '' // очищаем форму ввода
                    })
                )
            }
        }
    }
});

Vue.component('message-row', {//для строчьки
    props: ['message', 'editMethod', 'messages'],
    template: '<div>' +
        '<i>({{ message.id }})</i> {{ message.text }}' +
        '<span style="position: absolute; right: 0">' +
        '<input type="button" value="Edit" @click="edit" />' + //редактирование
        '<input type="button" value="X" @click="del" />' +//удаление
        '</span>' +
        '</div>',
    methods: {
        edit: function () {
            this.editMethod(this.message); // используем полученые месадж
        },
        del: function () { //this потомучто поле
            messageApi.remove({id: this.message.id}).then(result => { //проверяем существования
                if (result.ok) { //если есть тоесть удален на сервере
                    this.messages.splice(this.messages.indexOf(this.message), 1) //удаляем на фронте
                }
            })
        }
    }
});

Vue.component('messages-list', {//для списка
    props: ['messages'],
    data: function () {
        return {
            message: null
        }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
        '<message-form :messages="messages" :messageAttr="message" />' + //форма добавления
        '<message-row v-for="message in messages" :key="message.id" :message="message" ' + //цикол фор
        ':editMethod="editMethod" :messages="messages" />' +
        '</div>',
    methods: {
        editMethod: function (message) { //пробрасывет в форму редактирования
            this.message = message;
        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<div>' +
        // если профайл незаполнен то надо авторизоваться
        '<div v-if="!profile">Необходимо авторизоваться через <a href="/login">Google</a></div>' +
        '<div v-else>' +
        //имя пользователя и логаут
        '<div>{{profile.name}}&nbsp;<a href="/logout">Выйти</a></div>' +
        '<messages-list :messages="messages" />' +
        '</div>' +
        '</div>',

    data: {
        // messages: [],
        messages: frontendData.messages, // берем из переменой с бекенда из глобального контекста
        profile: frontendData.profile
    },
    created: function () {
        // // сходить получить сообщения по ресту
        // messageApi.get().then(result =>
        //     result.json().then(data =>
        //         data.forEach(message => this.messages.push(message))
        //     )
        // )
    },
});
