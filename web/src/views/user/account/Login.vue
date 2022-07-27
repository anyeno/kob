<template>
  <ConTentField>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="login">
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
          <div class="error-msg">{{ error_msg }}</div>
          <button type="submit" class="btn btn-primary">登录</button>
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

export default {
  name: "LogIn",
  components: {
    ConTentField,
  },
  setup() {
    const store = useStore();
    let username = ref("");
    let password = ref("");
    let error_msg = ref("");

    const login = () => {
      error_msg.value="";
      store.dispatch("login", { 
        //如果在外面调用actions里的名字的话要用dispatch的api "login是调用的函数名 {}里的是传入的参数"
        username: username.value,
        password: password.value,
        success() {
          store.dispatch("getinfo",{
            success() {
              router.push({ name: 'home' });
              console.log(store.state.user);
            },
            error(){
              error_msg.value = "用户名或密码错误";
            }
          })
          console.log(store.state.user.token);
        },
        error() {
          error_msg.value = "用户名或密码错误";
        },
      });
    };

    return {
      username,
      password,
      error_msg,
      login,
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
