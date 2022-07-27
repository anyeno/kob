import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView'
import RankListIndexView from '../views/ranklist/RankListIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import NotFoundView from '../views/error/NotFoundView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import Login from '../views/user/account/Login'
import Register from '../views/user/account/Register'
//import { redirect } from 'statuses'

//匹配是从上往下匹配的
const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/"   //重定向 即用户输入xxx.com/  将输入变成 xxx.com/pk/ 重新发送请求
  },
  {
    path:"/pk/",
    name:"pk_index",
    component:  PkIndexView,
  },
  {
    path:"/record/",
    name: "record_index",
    component:  RecordIndexView,
  },
  {
    path:"/ranklist/",
    name: "ranklist_index",
    component:  RankListIndexView,
  },
  {
    path:"/user/bot/",
    name: "user_bot_index",
    component:  UserBotIndexView,
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

export default router
