package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/pk/")
public class BotInofoController {
    @RequestMapping("getbotinfo/")
    public HashMap getBotInfo(){
        HashMap<String,String> bot1 = new HashMap();
        bot1.put("name","tiger");
        bot1.put("rating","2000");
        return bot1;
    }
}
