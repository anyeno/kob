<template>
  <div class="card" style="margin: 20px ">
    <div class="card-body">
      <div class="container">
        <div class="row">
          <div class="col-4">
            <UserInfo />
            <AddPost />
          </div>
          <div class="col-8">
            <PostList />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AddPost from "@/components/post/AddPost";
import PostList from "@/components/post/PostList";
import UserInfo from "@/components/post/UserInfo";
export default {
  components: {
    AddPost,
    PostList,
    UserInfo,
  },
};
</script>

<style  scoped>
</style>