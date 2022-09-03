
export default {
    state: {
      //存储所有数据
      a_id: "",
      b_id: "",
      status: "matching",  //matching 正在匹配   playing 对战
      socket: null,
      opponent_username: "",
      opponent_photo: "",
      gamemap: null,  
      loser: "none", //none all  A  B
      g: null,
      order: "", //single表示单步走   single表示双步走
      steps: 0,
    },
    getters: {
      //通过计算获取数据(state)的内容 只能读取 不能修改
    },
    mutations: {
      //对数据修改的操作要在这里执行 不能执行异步操作   调用用 commit
      updateAIdFive(state,aId){
        state.a_id = aId;
      },
      updateBIdFive(state,bId){
        state.b_id = bId;
      },
      updateSocketFive(state,socket) {
          state.socket = socket;
      },
      updateOpponentFive(state,opponent) {
          state.opponent_username = opponent.username;
          state.opponent_photo = opponent.photo;
      },
      updateStatusFive(state,status) {
          state.status = status;
      },
      updateLoserFive(state,loser){
        state.loser = loser;
      },
      updateG(state,g){
          state.g = g;
      },
      updateOrder(state, order){
        state.order = order;
      },
      updateStepsFive(state, steps){
        state.steps = steps;
      },
  
    },
    actions: {
      //操作数据 不能直接修改 异步  从云端获取数据写在这一步   调用用  dispatch
    },
    modules: {
      
    }
  }
  