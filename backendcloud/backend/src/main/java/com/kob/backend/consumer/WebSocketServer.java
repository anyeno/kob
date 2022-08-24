package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.Utils.Game;
import com.kob.backend.consumer.Utils.JwtAuthentication;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    //存储所有(全局)链接  >链接就是一个WebSocketServer实例
    //将userId 映射到一个链接
    //静态变量对所有实例都可见   共同拥有

    //每个链接都是一个线程   所以这个static要是线程安全的？
    final public static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();
    //线程安全 的匹配池


    private User user;//存储当前链接的用户是谁

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session = null;//?  websocket里的一个包？  每个连接用session来维护


    //websocket 不是spring的一个标准组件 不是一个单例模式？
    // > 单例模式-> 一个类同一时间只能有一个实例  websocket多线程所以不是一个单例模式
    //先定义成一个独一份的变量
    public static UserMapper userMapper;
    public static RecordMapper recordMapper;
    public static RestTemplate restTemplate;
    private static BotMapper botMapper;

    public Game game = null;

    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";


    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;//静态变量通过类名访问
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate = restTemplate;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接时调用这个函数
        this.session = session;
        System.out.println("connected!");

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

    public static void startGame(Integer aId, Integer aBotId,Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);

        Game game = new Game(
                13,
                14,
                20,
                a.getId(),
                botA,
                b.getId(),
                botB
        );
        game.createMap();

        if(users.get(a.getId()) != null)
            users.get(a.getId()).game = game;
        if(users.get(b.getId()) != null)
            users.get(b.getId()).game = game;
        game.start();

        JSONObject respGame = new JSONObject();
        respGame.put("a_id",game.getPlayerA().getId());
        respGame.put("a_sx",game.getPlayerA().getSx());
        respGame.put("a_sy",game.getPlayerA().getSy());
        respGame.put("b_id",game.getPlayerB().getId());
        respGame.put("b_sx",game.getPlayerB().getSx());
        respGame.put("b_sy",game.getPlayerB().getSy());
        respGame.put("gamemap",game.getG());

        JSONObject respA = new JSONObject();
        respA.put("event","start-matching");  //匹配成功
        respA.put("opponent_username",b.getUsername());
        respA.put("opponent_photo",b.getPhoto());
        respA.put("game",respGame);
        if(users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        JSONObject respB = new JSONObject();
        respB.put("event","start-matching");
        respB.put("opponent_username",a.getUsername());
        respB.put("opponent_photo",a.getPhoto());
        respB.put("game",respGame);
        if(users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    private void startMatching(Integer botId) {
        System.out.println("start matching");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        data.add("bot_id",botId.toString());
        restTemplate.postForObject(addPlayerUrl,data,String.class);//springboot间进行通信
    }

    private void stopMatching() {
        System.out.println("stop matching");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);
    }

    private void move(int direction) {
        if(game.getPlayerA().getId().equals(user.getId())) {
            if(game.getPlayerA().getBotId().equals(-1))  //真人操作
                game.setNextStepA(direction);
        } else if(game.getPlayerB().getId().equals(user.getId())) {
            if(game.getPlayerB().getBotId().equals(-1))
                game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息时调用这个函数
        //把onMessage当做一个路由使用

        System.out.println("receive a message");
        JSONObject data = JSONObject.parseObject(message);//?
        String event = data.getString("event");
        if("start-matching".equals(event)) {
            startMatching(data.getInteger("bot_id"));
        } else if("stop-matching".equals(event)) {
            stopMatching();
        } else if("move".equals(event)) {
            move(data.getInteger("direction"));
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

