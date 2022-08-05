import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView'
import RankListIndexView from '../views/ranklist/RankListIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import NotFoundView from '../views/error/NotFoundView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import Login from '../views/user/account/Login'
import Register from '../views/user/account/Register'
import PostView from '@/views/post/PostView'
import store from '../store/index'
//import { redirect } from 'statuses'

//匹配是从上往下匹配的
const routes = [
  {
    path:"/post/",
    name:"post",
    component:  PostView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path: "/",
    name: "home",
    redirect: "/pk/" ,  //重定向 即用户输入xxx.com/  将输入变成 xxx.com/pk/ 重新发送请求
    meta:{//把其他的一些信息放到meta中，也可以起别的名字
      requestAuth: true,
    }
  },
  {
    path:"/pk/",
    name:"pk_index",
    component:  PkIndexView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path:"/record/",
    name: "record_index",
    component:  RecordIndexView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path:"/ranklist/",
    name: "ranklist_index",
    component:  RankListIndexView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path:"/user/bot/",
    name: "user_bot_index",
    component:  UserBotIndexView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path:"/404/",
    name: "404",
    component:  NotFoundView,
  },
  {
    path:"/:catchall(.*)",//匹配任意字符
    redirect: "/404/"  //重定向到404页面
  },
  {
    path:"/user/account/login/",
    name: "login",
    component:  Login,
  },{
    path:"/user/account/register/",
    name: "register",
    component:  Register,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

//实现前端页面的授权
router.beforeEach((to,from,next) => {    //to跳转的页面   from从哪个页面跳转到   next后续操作
  if(to.meta.requestAuth && !store.state.user.is_login) {
    next({name:"login"})  //跳转到login页面
  }else{
    next()   //执行默认跳转
  }
})

export default router
