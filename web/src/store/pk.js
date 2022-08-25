
export default {
  state: {
    //存储所有数据
    status: "matching",  //matching 正在匹配   playing 对战
    socket: null,
    opponent_username: "",
    opponent_photo: "",
    gamemap: null,
    a_id: 0,
    a_sx: 0,
    a_sy: 0,
    b_id: 0,
    b_sx: 0,
    b_sy: 0,
    gameObject: null,
    loser: "none", //none all  A  B
    color: "蓝色",
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
    },
    updateGame(state,game) {
      state.gamemap = game.gamemap;
      state.a_id = game.a_id;
      state.a_sx = game.a_sx;
      state.a_sy = game.a_sy;
      state.b_id = game.b_id;
      state.b_sx = game.b_id;
      state.b_sy = game.b_sy;
    },
    updateGameObject(state,gameObject){
      state.gameObject = gameObject;
    },
    updateLoser(state,loser){
      state.loser = loser;
    },
    updateColor(state,color) {
      state.color = color;
    }
  },
  actions: {
    //操作数据 不能直接修改 异步  从云端获取数据写在这一步   调用用  dispatch
  },
  modules: {
    
  }
}
