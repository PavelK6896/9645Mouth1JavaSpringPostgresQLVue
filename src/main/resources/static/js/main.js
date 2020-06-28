var messageApi = Vue.resource('/message{/id}'); //вьюресурс {}-часть пути не обезательное

Vue.component('message-row', {//отоброжает строку
    props: ['message'],//принемает часть масива
    //темплате для листа
    template: '<div>' +
        '<i>({{ message.id }})</i> {{ message.text }}' +
        '</div>'
});

Vue.component('messages-list', {//отоброжает лист
  props: ['messages'],//компонент принемает масив из корня

//переберает заполненый масив
  template:
    '<div style="position: relative; width: 300px;">' +
      //ключь по ид
        '<message-row v-for="message in messages" :key="message.id" :message="message" ' + // прокидываем части масива
            '/>' +
    '</div>',


    //заполняем мосив
    //--------------------------------------resource--
    //получаем данные с сервера и ложем в messages
    // на момент создания объекта не существует
    //если () => то this for window
  created: function() {//ХУК ву когда проинициализировали объект
    messageApi.get().then(result => // запрок к созданому ву ресурс //промис возращает //result ответ сервера
    //статус текст json - response
        result.json().then(data => //так как промис то then // получили json с сервера
            data.forEach(message => this.messages.push(message)) //ложем json  messages по сылке который
        )
    )
  },
    //*********************************************
});

var app = new Vue({
  el: '#app',
  template: '<messages-list :messages="messages" />',//передает значение в компонент//прокидывает пустой мосив
  data: {
    messages: []
  }
});
