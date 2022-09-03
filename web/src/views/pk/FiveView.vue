<template>
  <div v-if="$store.state.five.loser === 'none'" class="playground">
    <div ref="parent" class="gamemap">
      <canvas ref="canvas" tabindex="0"></canvas>
    </div>
  </div>
  <FiveResult v-if="$store.state.five.loser != 'none'" />
</template>

<script>
import { Five } from "@/assets/js/Five.js";
import  FiveResult  from "@/components/FiveResult.vue";
import { ref, onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";

export default {
  components: {
      FiveResult
  },
  setup() {
    const store = useStore();
    let parent = ref(null);
    let canvas = ref(null);
    let socket = null;
    const socketUrl = `wss://app2409.acapp.acwing.com.cn/websocket/five/${store.state.user.token}/`;

    onMounted(() => {
      socket = new WebSocket(socketUrl); //建立连接

      socket.onopen = () => {
        //建立链接时前端执行的函数
        console.log("connected!");
        socket.send(JSON.stringify({ event: "start-matching" }));
        store.commit("updateSocketFive", socket);
      };

      socket.onmessage = (msg) => {
        //当前端收到信息时执行的函数
        const data = JSON.parse(msg.data);
        console.log(data);
        if (data.event === "start-game") {
          //匹配成功
          store.commit("updateOpponent", {
            username: data.opponent_username,
            photo: data.opponent_photo,
          });
          store.commit("updateG", data.g);
          store.commit("updateOrder", data.order);
          store.commit("updateAIdFive", data.a_id);
          store.commit("updateBIdFive", data.b_id);
          new Five(canvas.value, parent.value, store, socket);
        }
        if (data.event === "update_game") {
          store.commit("updateG", data.g);
          store.commit("updateStepsFive", data.steps);
        }
        if(data.event === "result"){
            store.commit("updateLoserFive", data.loser);
            console.log(store.state.five.loser);
        }
      };

      socket.onclose = () => {
        //链接关闭时执行的函数
        console.log("disconnected!");
      };
    });

    onUnmounted(() => {
      //组件卸载时执行
      socket.close(); //断开连接
      store.commit("updateStatusFive", "matching");
    });

    return {
      parent,
      canvas,
    };
  },
};
</script>

<style  scoped>
div .gamemap {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-content: center;
}
.playground {
  width: 70vw;
  height: 80vh;
  margin: 40px auto;
  background-color: lightblue;
}
</style>