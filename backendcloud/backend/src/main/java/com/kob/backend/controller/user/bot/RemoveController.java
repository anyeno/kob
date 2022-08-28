package com.kob.backend.controller.user.bot;

import com.kob.backend.service.impl.user.bot.RemoveBotServiceImpl;
import com.kob.backend.service.user.bot.RemoveBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RemoveController {
    @Autowired
    private RemoveBotService removeBotService;

    @PostMapping("/api/user/bot/remove/")
    public Map<String,String> remove(@RequestParam Map<String,String> data){
        return removeBotService.remove(data);
    }
}
