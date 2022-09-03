package com.kob.backend.Five;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.Utils.JwtAuthentication;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/websocket/five/{token}")  // 注意不要以'/'结尾
public class FiveWebSocketServer {
    final public static ConcurrentHashMap<Integer, FiveWebSocketServer> users = new ConcurrentHashMap<Integer, FiveWebSocketServer>();
    private User user;
    private Session session = null;

    //private int[][] g = new int[15][15];
    private FiveGame game = null;

    //线程安全 的匹配池
    final private static CopyOnWriteArrayList<User> matchpool = new CopyOnWriteArrayList<>();

    public static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        FiveWebSocketServer.userMapper = userMapper;
    }

    private void startMatching() {
        System.out.println("start matching");
        matchpool.add(this.user);

        while(matchpool.size()>=2) {
            Iterator<User> it = matchpool.iterator();
            User a = it.next(),b = it.next();
            matchpool.remove(a);
            matchpool.remove(b);

            FiveGame game = new FiveGame(a.getId(), b.getId());
            users.get(a.getId()).game = game;
            users.get(b.getId()).game = game;

            JSONObject respA = new JSONObject();
            respA.put("event", "start-game");
            respA.put("opponent_username",b.getUsername());
            respA.put("opponent_photo",b.getPhoto());
            respA.put("g", game.g);
            respA.put("order", "single");
            respA.put("a_id", a.getId());
            respA.put("b_id", b.getId());
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "start-game");
            respB.put("opponent_username",b.getUsername());
            respB.put("opponent_photo",b.getPhoto());
            respB.put("g", game.g);
            respB.put("order", "double");
            respB.put("a_id", a.getId().toString());
            respB.put("b_id", b.getId().toString());
            users.get(b.getId()).sendMessage(respB.toJSONString());

            System.out.println("start game");

        }
    }

    private void stopMatching() {
        System.out.println("stop matching");
        matchpool.remove(this.user);
    }

    boolean is_valid(int x,int y){
        return 0<=x && x<=14 && 0<=y && y<=14;
    }

    private String check_loser(){
        int [] dx = {0,1,1,1}, dy = {1,1,0,-1};
        String loser = "none";
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int x = i, y = j;
                if(game.g[i][j] == 1){
                    int count  = 1;
                    for (int k = 0; k < 4; k++) {
                            x+=dx[k]; y+=dy[k];
                            while(is_valid(x,y) && game.g[x][y] == 1){
                                count++;
                                x+=dx[k]; y+=dy[k];
                            }
                            if(count >= 5){
                                loser = "B";
                                break;
                            }else{
                                count = 1;
                                x = i;
                                y = j;
                            }
                    }
                }else if(game.g[i][j] == 2){
                    int count  = 1;
                    for (int k = 0; k < 4; k++) {
                        x+=dx[k]; y+=dy[k];
                        while(is_valid(x,y) && game.g[x][y] == 2){
                            count++;
                            x+=dx[k]; y+=dy[k];
                        }
                        if(count >= 5){
                            loser = "A";
                            break;
                        }else{
                            count = 1;
                            x = i;
                            y = j;
                        }
                    }
                }
            }
        }

        return loser;
    }

    private void chess(JSONObject data){
        Integer row = Integer.parseInt(data.getString("row"));
        Integer col = Integer.parseInt(data.getString("col"));
        Integer gNumber = Integer.parseInt(data.getString("g_number"));
        game.g[row][col] = gNumber;
        game.steps ++;
        JSONObject resp = new JSONObject();
        resp.put("event", "update_game");
        resp.put("g", game.g);
        resp.put("steps", game.steps);
        users.get(game.aId).sendMessage(resp.toJSONString());
        users.get(game.bId).sendMessage(resp.toJSONString());
        System.out.println("update game");

        String loser = check_loser();
        if(!"none".equals(loser)){
            JSONObject respResult = new JSONObject();
            respResult.put("event", "result");
            respResult.put("loser", loser);
            users.get(game.aId).sendMessage(respResult.toJSONString());
            users.get(game.bId).sendMessage(respResult.toJSONString());
            System.out.println("hava a result");
        }

    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接时调用这个函数
        this.session = session;
        System.out.println("connected!");
        //System.out.println(token);
        Integer userId = JwtAuthentication.getUserID(token);//获取uerid
        this.user = userMapper.selectById(userId);//从数据库里查询链接的client端的用户

        if(this.user!=null) {
            users.put(userId,this);//把当前链接加入到全局的链接中
        } else {
            this.session.close();
        }

        //System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接时调用这个函数
        System.out.println("disconnected!");
        if(this.users != null) {
            users.remove(this.user.getId()); //从全局链接中删除
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息时调用这个函数
        //把onMessage当做一个路由使用

        System.out.println("receive a message");
        JSONObject data = JSONObject.parseObject(message);//?
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        }
//        if("stop-matching".equals(event)){
//            stopMatching();
//        }
        if("chess".equals(event)) {
            chess(data);
        }

    }

    public void sendMessage(String message) {
        //向当前链接的client发送信息
        synchronized (this.session) { //异步通信 加锁 ？
            try{
                this.session.getBasicRemote().sendText(message);  //向前端发送信息
            } catch (IOException e) {
                e.printStackTrace();//将异常输出
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
