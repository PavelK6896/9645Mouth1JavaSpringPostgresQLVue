<template>
    <span></span>
</template>

<script>
    import {mapActions} from 'vuex' // 1 импортируем

    export default {
        name: 'LazyLoader',
        methods: mapActions(['loadPageAction']), // 2 импортируем
        mounted() { // навешиваем лисанар
            window.onscroll = () => {
                const el = document.documentElement
                // window.innerHeight -ширина экрана
                // el.scrollTop насколько прокручен элемент
                // el.offsetHeight - общая высота документа
                const isBottomOfScreen = el.scrollTop + window.innerHeight === el.offsetHeight

                if (isBottomOfScreen) {
                    this.loadPageAction() // 3 вызываем
                }
            }
        },
        beforeDestroy() { // убираем лисанер
            window.onscroll = null
        }
    }
</script>

<style scoped>

</style>
