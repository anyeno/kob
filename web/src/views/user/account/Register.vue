<template>
  <ConTentField>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="register">
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input
              v-model="username"
              type="text"
              class="form-control"
              id="username"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input
              v-model="password"
              type="password"
              class="form-control"
              id="password"
            />
          </div>
          <div class="mb-3">
            <label for="password_confirm" class="form-label">确认密码</label>
            <input
              v-model="password_confirm"
              type="password"
              class="form-control"
              id="password_confirm"
            />
          </div>
          <div class="error-msg">{{ error_msg }}</div>
          <button type="submit" class="btn btn-primary">注册</button>
        </form>
      </div>
    </div>
  </ConTentField>
</template>

<script>
import ConTentField from "@/components/ContentField.vue";
import { ref } from "vue";
import { useStore } from "vuex";
import router from "@/router/index"; //@定义到了src目录
import $ from "jquery";
export default {
  name: "ReGister",
  components: {
    ConTentField,
  },
  setup() {
    const store = useStore();
    let username = ref("");
    let password = ref("");
    let error_msg = ref("");
    let password_confirm = ref("");
    const register = () => {
      error_msg.value = "";
      $.ajax({
        url: "https://app165.acapp.acwing.com.cn/myspace/user/",
        type: "POST",
        data: {
          username: username.value,
          password: password.value,
          password_confirm: password_confirm.value,
        },
        success(resp) {
          if (resp.result === "success") {
            store.dispatch("login", {
              //如果在外面调用actions里的名字的话要用dispatch的api "login是调用的函数名 {}里的是传入的参数"
              username: username.value,
              password: password.value,
              success() {
                router.push({ name: "userlist" }); //跳转到userlist 跟navbar里的一样
              },
              error() {
                error_msg.value = "系统异常，稍后重试";
              },
            }) ;
          } else{
              error_msg.value=resp.result;
          }
        },
      });
    };

    return {
      username,
      password,
      error_msg,
      password_confirm,
      register,
    };
  },
};
</script>

<style scoped>
.error-msg {
  color: red;
}
button {
  width: 100%;
}
</style>
