<template>
    <div class="container">
        <div class="card" v-for="post in posts" :key="post.id">
            <div class="card-body">{{post.content}}</div>
        </div>
    </div>
</template>

<script>
import $ from 'jquery'
import {useStore} from 'vuex'
import {ref} from 'vue'
    export default {
        setup(){
            const store = useStore();
            let posts = ref([])
            $.ajax({
                url:"http://127.0.0.1:3000/post/getlist/",
                type:"get",
                headers:{
                    Authorization:"Bearer "+store.state.user.token,
                },
                success(resp){
                    posts.value = resp
                    //console.log(posts)
                }
            })
            console.log(posts)
            return{
                posts
            }
        },
    }
</script>

<style scoped>
div.card{
    margin:5px;
}
</style>