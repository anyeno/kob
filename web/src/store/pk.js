
export default {
  state: {
    //存储所有数据
    status: "matching",  //matching 正在匹配   playing 对战
    socket: null,
    opponent_username: "",
    opponent_photo: "",

  },
  getters: {
    //通过计算获取数据(state)的内容 只能读取 不能修改
  },
  mutations: {
    //对数据修改的操作要在这里执行 不能执行异步操作   调用用 commit
    updateSocket(state,socket) {
        state.socket = socket;
    },
    updateOpponent(state,opponent) {
        state.opponent_username = opponent.username;
        state.opponent_photo = opponent.photo;
    },
    updateStatus(state,status) {
        state.status = status;
    }
  },
  actions: {
    //操作数据 不能直接修改 异步  从云端获取数据写在这一步   调用用  dispatch
  },
  modules: {
    
  }
}
