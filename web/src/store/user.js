import $ from 'jquery'
const ModuleUser={
    state: {
        id:"",
        username:"",
        photo:"", 
        token:"",
        is_login:false,
        pulling_info:true,  //是否正在拉取信息
    },
    getters: {
    },
    mutations: {
        updateUser(state,user){
            state.id=user.id;
            state.username=user.username;
            state.photo=user.photo;
            state.is_login=user.is_login;
        },
        updateToken(state,token){
            state.token = token;
        },
        logout(state){
            state.id="";
            state.username="";
            state.photo="";
            state.token="";
            state.is_login=false;
        },
        updatePullingInfo(state,pullingInfo) {
            state.pulling_info = pullingInfo
        }
    },
    actions: {
        login(context,data){//data传入api的输入  这个data是定义函数(login)时的参数   context用来调用mutations的函数
            $.ajax({
                url:"http://localhost:3000/user/account/token/",
                type:"post",
                data:{
                    username: data.username,
                    password: data.password,
                },
                success(resp){
                    if(resp.error_message === "success") {
                        localStorage.setItem("jwt_token",resp.token);//将token存到浏览器的localStorage中
                        context.commit("updateToken",resp.token);
                        data.success(resp);
                    }else {
                        data.error(resp);
                    }
                },
                error(resp){
                    data.error(resp);
                }
              })
            
        },

        getinfo(context,data){
            $.ajax({
                url:"http://localhost:3000/user/account/info/",
                type:"get",
                headers: {
                  Authorization: "Bearer "+ context.state.token,
                },//接口要jwt验证的话就要加上这个
                success(resp){  
                    if(resp.error_message === "success") {
                        context.commit("updateUser",{
                            ...resp,
                            is_login:true,
                        });
                        data.success(resp);
                    }
                    else {  
                        data.error(resp);
                    }
                },
                error(resp){
                  data.error(resp);
                }
              })
        },
        
        logout(context){
            localStorage.removeItem("jwt_token");//从localStorage中删掉
            context.commit("logout");
        }
        
    },
    modules: {
      
    }
}

export default ModuleUser