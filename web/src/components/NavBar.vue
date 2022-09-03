<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <router-link class="navbar-brand" :to="{ name: 'home' }">KOB</router-link>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link
              :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'pk_index' }"
              >对战</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'record_index' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'record_index' }"
              >对局列表</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'ranklist_index' }"
              >排行榜</router-link
            >
          </li>
          <!-- <li class="nav-item">
            <router-link
              :class="route_name == 'rpost' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'post' }"
              >发帖</router-link
            >
          </li> -->
          <li class="nav-item">
            <router-link
              :class="route_name == '404' ? 'nav-link active' : 'nav-link'"
              :to="{ name: '404' }"
              >随机壁纸</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="route_name == 'five' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'five' }"
              >五子棋</router-link
            >
          </li>
        </ul>

        <ul class="navbar-nav" v-if="!$store.state.user.is_login">
          <li class="nav-item">
            <router-link
              :class="route_name == 'login' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'login' }"
              >登录</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="route_name == 'register' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'register' }"
              >注册</router-link
            >
          </li>
        </ul>

        <ul class="navbar-nav" v-else>
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              id="navbarDropdown"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              {{ $store.state.user.username }}
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <!-- <li><a class="dropdown-item" href="/user/bot/">我的蛇蛇</a></li> -->
              <router-link
                class="dropdown-item"
                :to="{ name: 'user_bot_index' }"
                >我的Bot
              </router-link>
              <li><hr class="dropdown-divider" /></li>
              <li>
                <a @click="logout" class="dropdown-item" href="#">退出</a>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { useRoute } from "vue-router";
import { computed } from "vue";
import { useStore } from "vuex";

export default {
  setup() {
    const store = useStore();
    const route = useRoute();
    let route_name = computed(() => route.name);

    const logout = () => {
      store.dispatch("logout");
    };
    return {
      route_name,
      logout,
    };
  },
};
</script>

<style lang="scss" scoped>
</style>