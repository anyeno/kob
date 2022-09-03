<template>
  <div class="container">
    <div class="row">
      <div class="mb-3">
        <label for="content" class="form-label" style="margin-top:20px"
          >编辑帖子</label
        >
        <textarea
          class="form-control"
          id="content"
          rows="3"
          v-model = content
        ></textarea>
      </div>
      <button class="btn btn-primary" @click="add_post">发送</button>
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
        let content = ref("")
        const add_post = ()=>{
            $.ajax({
                url: store.state.domain + "api/post/add/",
                type:"post",
                headers:{
                    Authorization:"Bearer "+store.state.user.token
                },
                data:{
                    "content":content.value
                },
                success(resp){
                    console.log(resp)
                    content.value = ""
                }
            })
            
        }

        return {
            content,add_post,
        }
    }

};
</script>

<style  scoped>
</style>