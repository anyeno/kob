package com.kob.backend.consumer;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    //每个链接都是一个线程   所以这个static要是线程安全的？
    //存储所有(全局)链接  >链接就是一个WebSocketServer实例
    //将userId 映射到一个链接
    //静态变量对所有实例都可见   共同拥有
    private static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    private User user;//存储当前链接的用户是谁
    private Session session = null;//?  websocket里的一个包？  每个连接用session来维护

    //websocket 不是spring的一个标准组件 不是一个单例模式？
    // > 单例模式-> 一个类同一时间只能有一个实例  websocket多线程所以不是一个单例模式
    //先定义成一个独一份的变量
    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;//静态变量通过类名访问
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接时调用这个函数
        this.session = session;
        System.out.println("connected!");

        Integer userId = Integer.parseInt(token);//获取uerid
        this.user = userMapper.selectById(userId);//从数据库里查询链接的client端的用户
        users.put(userId,this);//把当前链接加入到全局的链接中
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
        System.out.println("receive a message");
    }

    public void sendMessage(String message) {
        //向当前链接的client发送信息
        synchronized (this.session) { //异步通信 加锁 ？
            try{
                this.session.getBasicRemote().sendText(message);  //向前端发送信息
            } catch (IOException e) {
                e.printStackTrace();//讲异常输出
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}

