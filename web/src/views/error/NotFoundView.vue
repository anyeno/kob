<template>
    <button @click="change" class="btn btn-primary btn-sm">随机刷新</button>

    <ContentField>
        <img class="img-fluid"  :src=url_dog alt="无法显示"  >
    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import $ from 'jquery'
import {ref} from 'vue'
import {useStore} from 'vuex'

export default {
    components: {
        ContentField,
    },
    setup(){
        let url_dog = ref("")
        //let dog_width = ref(0)
        //let dog_height = ref(0)
        const store = useStore();
        $.ajax({
                //url:"https://api.thedogapi.com/v1/images/search",
                
                url: store.state.domain + "api/images/get/",
                tyle:"get",
                headers:{
                    Authorization:"Bearer "+ store.state.user.token
                },
                success(resp){
                    const len = resp.length;
                    let i = Math.floor(Math.random()*len);
                    url_dog.value = resp[i]["url"];
                },
                error(resp){
                    console.log(resp)
                },
        })
        const change = ()=>{
            $.ajax({
                //url:"https://api.thedogapi.com/v1/images/search",
                
                url: store.state.domain + "api/images/get/",
                tyle:"get",
                success(resp){
                    const len = resp.length;
                    let i = Math.floor(Math.random()*len);
                    url_dog.value = resp[i]["url"];
                },
                error(resp){
                    console.log(resp)
                },
            })
        }
        return {
            url_dog,change
        }
    }
}
</script>

<style  scoped>
.btn{
    margin-left: 10px;
    margin-top: 10px;
}
</style>