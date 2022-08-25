<template>
    <div class="show_which">
        <h4>您为 
            <span v-if="$store.state.pk.color === '蓝色'" class="text-blue"> {{color}}</span>
            <span v-if="$store.state.pk.color === '红色'" class="text-red"> {{color}}</span>
        方</h4>
    </div>
   <div class="playground">
       <GameMap></GameMap>
   </div>
</template>

<script>
import { useStore } from 'vuex'
import {ref} from 'vue'

import GameMap from './GameMap.vue'
    export default {
        components: {
            GameMap,
        },
        setup(){
            const store = useStore();
            let color = ref("");
            color = store.state.pk.color
            return {
                color
            }
        }
    }
</script>

<style  scoped>
div .playground {
    width: 60vw;
    height: 70vh;
    margin:40px auto;
    background-color: lightblue ;
}
.show_which{
    width: 12vw;
    margin:10vw 10vh;
    background-color: white;
    position: absolute;
}
.text-red{
    color: red;
}
.text-blue{
    color: blue;
}
</style>