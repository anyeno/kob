package com.kob.backend.video;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.Utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/websocket/movie/{token}")  // 注意不要以'/'结尾
public class VideoWebSocketServer {
    final public static ConcurrentHashMap<Integer, VideoWebSocketServer> users = new ConcurrentHashMap<Integer, VideoWebSocketServer>();
    private User user;
    private Session session = null;

    private Group group = null;


    //线程安全 的匹配池
    final private static CopyOnWriteArrayList<User> matchpool = new CopyOnWriteArrayList<>();

    public static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        VideoWebSocketServer.userMapper = userMapper;
    }

    private void startMatching() {
        System.out.println("start matching");
        matchpool.add(this.user);

        while (matchpool.size() >= 2) {
            Iterator<User> it = matchpool.iterator();
            User a = it.next(), b = it.next();
            matchpool.remove(a);
            matchpool.remove(b);

            Group group1 = new Group(a.getId(), b.getId());
            users.get(a.getId()).group = group1;
            users.get(b.getId()).group = group1;

            JSONObject resp = new JSONObject();
            resp.put("event", "start");
            users.get(a.getId()).sendMessage(resp.toJSONString());
            users.get(b.getId()).sendMessage(resp.toJSONString());
        }
    }

    public void play(){
        JSONObject resp = new JSONObject();
        resp.put("event", "play");
        Integer aId = this.group.aId;
        Integer bId = this.group.bId;
        users.get(aId).sendMessage(resp.toJSONString());
        users.get(bId).sendMessage(resp.toJSONString());
    }

    public void pause(){
        JSONObject resp = new JSONObject();
        resp.put("event", "pause");
        Integer aId = this.group.aId;
        Integer bId = this.group.bId;
        users.get(aId).sendMessage(resp.toJSONString());
        users.get(bId).sendMessage(resp.toJSONString());
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
        if("play".equals(event)){
            play();
        }
        if("pause".equals(event)){
            pause();
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
