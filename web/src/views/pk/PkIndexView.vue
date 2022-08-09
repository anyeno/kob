<template>
    <PlayGround>

    </PlayGround>
</template>

<script>
import PlayGround from '@/components/PlayGround.vue'
import {onMounted,onUnmounted} from 'vue'
import {useStore} from 'vuex'

export default {
    components: {
        PlayGround,
    },
    setup(){
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.id}`  //用``就可以使用store了

        let socket = null;
        onMounted(() => {//组件挂载时执行
            socket = new WebSocket(socketUrl);//建立连接

            socket.onopen = () => {//建立链接时前端执行的函数
                console.log("connected!");
            }

            socket.onmessage = msg => {//当前端收到信息时执行的函数
                const data = JSON.parse(msg.data);
                console.log(data);
            }

            socket.onclose = () => {//链接关闭时执行的函数
                console.log("disconnected!");
            }
        });
        
        onUnmounted(() => {//组件卸载时执行
            socket.close();//断开连接

        })
    }
}
</script>

<style lang="scss" scoped>

</style>