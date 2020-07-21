import SockJS from 'sockjs-client' // обходит ограничения безопасности
import {Stomp} from '@stomp/stompjs'

let stompClient = null
const handlers = [] // для приема

export function connect() {
    console.log("connect ws.js")
    const socket = new SockJS('/gs-guide-websocket')
    stompClient = Stomp.over(socket)
    stompClient.debug = () => {} // типо отключает логирование

    stompClient.connect({}, frame => {
        console.log('connect ws.js' + frame)
        stompClient.subscribe('/topic/activity', message => {
            handlers.forEach(handler => handler(JSON.parse(message.body))) // собираем ответ от сервера
            console.log("subscribe ws.js")
        })
    })
}

export function addHandler(handler) {
    console.log("addHandler ws.js", handler )
    handlers.push(handler)
}

