<template>
  <div>
    <video
      class="video"
      id="video_1"
      controls
      src="@/assets/video/春雨里洗过的太阳.mp4"
      preload="none"
    ></video>
    <video src=""></video>
    <button @click="play_or_pause">播放/暂停</button>
  </div>
</template>

<script>
import $ from "jquery";
import { onMounted, onUnmounted } from "@vue/runtime-core";
import {useStore} from 'vuex'
export default {
  setup() {
    //let video = $("#video_1");
    //console.log("video  " + video);
    const store = useStore();
    let socket = null;
    const socketUrl = `wss://app2409.acapp.acwing.com.cn/websocket/movie/${store.state.user.token}/`;
    const play = () => {
      $("#video_1")[0].play();
      socket.send(JSON.stringify({ event: "play" }))
    };
    const pause = () => {
      $("#video_1")[0].pause();
      socket.send(JSON.stringify({ event: "pause" }))
    };
    const uncontrolled_play = () => {
      $("#video_1")[0].play();
    };
    const uncontrolled_pause = () => {
      $("#video_1")[0].pause();
    };
    const play_or_pause = () => {
      if ($("#video_1")[0].paused) {
        play();
      } else {
        pause();
      }
    };

    onMounted(() => {
      socket = new WebSocket(socketUrl); //建立连接

      socket.onopen = () => {
        //建立链接时前端执行的函数
        console.log("connected!");
        socket.send(JSON.stringify({ event: "start-matching" }));
      };

      socket.onmessage = (msg) => {
        //当前端收到信息时执行的函数
        const data = JSON.parse(msg.data);
        console.log(data);
        if(data.event === "play"){
            uncontrolled_play();
        }
        if(data.event === "pause"){
            uncontrolled_pause();
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
    });
    return {
      play_or_pause,
    };
  },
};
</script>

<style scoped>
.video {
  width: 70vw;
  height: auto;
}
</style>