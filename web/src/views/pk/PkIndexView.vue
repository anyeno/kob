<template>
    <PlayGround v-if="$store.state.pk.status==='playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
    <ResultBoard v-if="$store.state.pk.loser != 'none' " />
</template>

<script>
import PlayGround from '@/components/PlayGround.vue'
import MatchGround from '@/components/MatchGround.vue'
import ResultBoard from '@/components/ResultBoard.vue'

import {onMounted,onUnmounted} from 'vue'
import {useStore} from 'vuex'

export default {
    components: {
        PlayGround,
        MatchGround,
        ResultBoard,
    },
    setup(){
        const store = useStore();
        //const socketUrl = `wss://app2409.acapp.acwing.com.cn/websocket/${store.state.user.token}/`  //用``就可以使用store了
        const socketUrl = `wss://app2409.acapp.acwing.com.cn/websocket/${store.state.user.token}/`
        let socket = null;

        store.commit("updateIsRecord",false);
        store.commit("updateLoser","none");
        
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
                    store.commit("updateColor",data.color);
                    setTimeout(() => {
                        store.commit("updateStatus","playing");
                    },200);   //两秒后执行
                    store.commit("updateGame",data.game)
                } else if(data.event === "move"){
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0,snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                } else if(data.event === "result") {
                    console.log(data)
                    const game = store.state.pk.gameObject;
                    const [snake0,snake1] = game.snakes;

                    if(data.loser === "all" || data.loser === "A") {
                        snake0.status = "die";
                    } 
                    if(data.loser === "all" || data.loser === "B") {
                        snake1.status = "die";
                    }
                    store.commit("updateLoser",data.loser);
                }            
            }

            socket.onclose = () => {//链接关闭时执行的函数
                console.log("disconnected!");
            };
        });
        
        onUnmounted(() => {//组件卸载时执行
            socket.close();//断开连接
            store.commit("updateStatus","matching");
        })
    }
}
</script>

<style scoped>

</style>