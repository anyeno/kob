<template>
    <PlayGround v-if="$store.state.pk.status==='playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
</template>

<script>
import PlayGround from '@/components/PlayGround.vue'
import MatchGround from '@/components/MatchGround.vue'
import {onMounted,onUnmounted} from 'vue'
import {useStore} from 'vuex'

export default {
    components: {
        PlayGround,
        MatchGround,
    },
    setup(){
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`  //用``就可以使用store了

        let socket = null;
        onMounted(() => {//组件挂载时执行
            store.commit("updateOpponent",{
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })  //调用store里的函数

            socket = new WebSocket(socketUrl);//建立连接

            socket.onopen = () => {//建立链接时前端执行的函数
                console.log("connected!");
                store.commit("updateSocket",socket);
            }

            socket.onmessage = msg => {//当前端收到信息时执行的函数
                const data = JSON.parse(msg.data);
                if(data.event === "start-matching") {//匹配成功
                    store.commit("updateOpponent",{
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout(() => {
                        store.commit("updateStatus","playing");
                    },2000);   //两秒后执行
                    store.commit("updateGamemap",data.gamemap)
                }                
            }

            socket.onclose = () => {//链接关闭时执行的函数
                console.log("disconnected!");
            }
        });
        
        onUnmounted(() => {//组件卸载时执行
            socket.close();//断开连接
            store.commit("updateStatus","matching");
        })
    }
}
</script>

<style lang="scss" scoped>

</style>