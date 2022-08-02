<template>
    <ContentField>
        <img class="img-fluid"  :src=url_dog alt="无法显示"  :width=dog_width*3/2 :height=dog_height*3/2>
        <button @click="change">改变图片</button>
    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import $ from 'jquery'
import {ref} from 'vue'

export default {
    components: {
        ContentField,
    },
    setup(){
        let url_dog = ref("")
        let dog_width = ref(0)
        let dog_height = ref(0)
        let hhh = ref("")
        $.ajax({
                url:"https://api.thedogapi.com/v1/images/search",
                tyle:"get",
                success(resp){
                    //console.log(resp)
                    url_dog.value = resp[0]["url"];
                    dog_width.value = resp[0]["width"];
                    dog_height.value = resp[0]["height"];
                    hhh.value = "hhh"
                },
                error(resp){
                    console.log(resp)
                },
        })
        const change = ()=>{
            $.ajax({
                url:"https://api.thedogapi.com/v1/images/search",
                tyle:"get",
                success(resp){
                    //console.log(resp)
                    url_dog.value = resp[0]["url"];
                    dog_width.value = resp[0]["width"];
                    dog_height.value = resp[0]["height"];
                    hhh.value = "hhh"
                },
                error(resp){
                    console.log(resp)
                },
            })
        }
        //console.log(url_dog)
        return {
            url_dog,dog_height,dog_width,change,hhh
        }
    }
}
</script>

<style lang="scss" scoped>

</style>