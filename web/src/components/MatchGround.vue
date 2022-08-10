<template>
  <div class="matchground">
    <div class="container">
      <div class="row">
        <div class="col-6">
          <div class="user-photo">
            <img :src="$store.state.user.photo" alt="" class="img-fluid" />
          </div>
          <div class="user-username">
            {{ $store.state.user.username }}
          </div>
        </div>
        <div class="col-6">
          <div class="user-photo">
            <img
              :src="$store.state.pk.opponent_photo"
              alt=""
              class="img-fluid"
            />
          </div>
          <div class="user-username">
            {{ $store.state.pk.opponent_username }}
          </div>
        </div>
        <div class="row">
          <div class="col-12 match-btn">
            <button @click="click_match_btn" type="button" class="btn btn-warning">{{match_btn_info}}</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {ref} from 'vue'
import {useStore} from 'vuex'

export default {
    setup() {
        let match_btn_info = ref("开始匹配");
        const store = useStore();

        const click_match_btn = () => {
            if(match_btn_info.value === "开始匹配") {
                match_btn_info.value = "取消";
                store.state.pk.socket.send(JSON.stringify({//json封装成字符串然后通过socket连接发送到后端   
                    event:"start-matching",

                }));
            }
            else{
                match_btn_info.value = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({//json封装成字符串   
                    event:"stop-matching",
                    
                }));
            }
        }   

        return {
            match_btn_info,
            click_match_btn,
        }
    }
};
</script>

<style  scoped>
div .matchground {
  width: 60vw;
  height: 70vh;
  margin: 40px auto;
  background-color: rgba(50, 50, 50, 0.5);
}
div.user-photo {
  text-align: center;
  padding-top: 10vh;
}
div.user-photo > img {
  width: 200px;
  height: 180px;
  border-radius: 50%;
  width: 25vh;
}
div.user-username {
  text-align: center;
  font-size: 24px;
  font-weight: 500;
  color: white;
  padding-top: 2vh;
}
div.match-btn {
  text-align: center;
  padding-top:14vh;
}
</style>