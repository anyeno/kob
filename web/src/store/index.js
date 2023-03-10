import { createStore } from 'vuex'
import ModuleUser from './user.js'
import ModulePK from './pk'
import ModuleRecord from './record'
import ModuleFive from './five'
//存储全局变量  各组件之间传递数据、共用数据就用它

export default createStore({
  state: {
    //存储所有数据
    domain: "https://app2409.acapp.acwing.com.cn/"
    //domain: "http://127.0.0.1:3000/"
  },
  getters: {
    //通过计算获取数据(state)的内容 只能读取 不能修改
  },
  mutations: {
    //对数据修改的操作要在这里执行 不能执行异步操作   调用用 commit
  },
  actions: {
    //操作数据 不能直接修改 异步  从云端获取数据写在这一步   调用用  dispatch
  },
  modules: {
    //分割state state可能数据太多了
    user: ModuleUser,
    pk: ModulePK,
    record: ModuleRecord,
    five: ModuleFive,
  }
})
