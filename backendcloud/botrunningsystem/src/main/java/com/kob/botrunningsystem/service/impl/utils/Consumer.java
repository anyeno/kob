package com.kob.botrunningsystem.service.impl.utils;

import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class Consumer extends Thread{
    private Bot bot;
    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";

    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();  //开启一个线程执行run()

        try {
            this.join(timeout); //主线程最多等待timeout秒 或者等待上面那个线程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.interrupt(); // 中断当前线程
    }

    private String addUid(String code, String uid) {//在code中的Bot类名后添加uid
        int k = code.indexOf(" implements java.util.function.Supplier<Integer>");
        return code.substring(0,k) + uid + code.substring(k);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();   //每次返回一个不一样的随机字符串
        String uid = uuid.toString().substring(0,8);

        Supplier<Integer> botInterface = Reflect.compile(  //工具接口帮我们动态编译代码
                "com.kob.botrunningsystem.utils.Bot" + uid,
                addUid(bot.getBotCode(),uid)
        ).create().get();

        File file = new File("input.txt");
        try (PrintWriter fout = new PrintWriter(file)){
            fout.println(bot.getInput());
            fout.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Integer direction = botInterface.get();

        System.out.println("move-direction: " + bot.getUserId() + "  " + direction); //调用编译后执行的结果

        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction",direction.toString());

        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }
}
