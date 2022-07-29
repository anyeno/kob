<template>
    <ContentField>
        我的蛇蛇
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'
import $ from 'jquery'
import {useStore} from 'vuex'

export default {
    components: {
        ContentField,
    },
    setup(){
        const store = useStore();
        // $.ajax({
        //     url:"http://127.0.0.1:3000/user/bot/add/",
        //     type:"post",
        //     data:{
        //         title:"Bot的标题",
        //         description:"描述",
        //         content:"代码",
        //     },
        //     headers:{
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp)
        //     },
        //     error(resp){
        //         console.log(resp)
        //     }
        // })

        // $.ajax({
        //     url:"http://127.0.0.1:3000/user/bot/remove/",
        //     type:"post",
        //     data:{
        //         bot_id: 15,
        //     },
        //     headers:{
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp)
        //     },
        //     error(resp){
        //         console.log(resp)
        //     }
        // })

        // $.ajax({
        //     url:"http://127.0.0.1:3000/user/bot/update/",
        //     type:"post",
        //     data:{
        //         bot_id: 3,
        //         title:"改Bot的标题",
        //         description:"改描述",
        //         content:"改代码",
        //     },
        //     headers:{
        //         Authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp) {
        //         console.log(resp)
        //     },
        //     error(resp){
        //         console.log(resp)
        //     }
        // })

         $.ajax({
            url:"http://127.0.0.1:3000/user/bot/getlist/",
            type:"get",
            headers:{
                Authorization: "Bearer " + store.state.user.token,
            },
            success(resp) {
                console.log(resp)
            },
            error(resp){
                console.log(resp)
            }
        })
    }
}
</script>

<style scoped>

</style>