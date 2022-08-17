package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateBotServiceImpl implements UpdateBotService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        //这一坨就是取出发出请求的user
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String,String> map = new HashMap<>();

        if(title == null || title.length() == 0) {
            map.put("error_message","标题不能为空");
            return map;
        }

        if(title.length()>100) {
            map.put("error_message","标题长度不能大于100");
            return map;
        }

        if(description == null && description.length() == 0) {
            description = "这个用户很懒，什么也没有写";
        }

        if(description != null && description.length() > 500) {
            map.put("error_message","描述不能大于500");
            return map;
        }

        if(content == null || content.length() == 0) {
            map.put("error_message","代码不能为空");
            return map;
        }

        if(content.length() > 10000) {
            map.put("error_message","代码长度不能大于10000");
            return map;
        }

        Bot bot = botMapper.selectById(bot_id);
        if(bot == null){
            map.put("error_message","bot不存在或已被删除");
            return map;
        }

        if(!bot.getUserId().equals(user.getId())){//bot的userid 与 请求的用户的id不是同一个
            map.put("error_message","没有权限修改别人的bot");
            return map;
        }

        Bot new_bot = new Bot(bot.getId(),user.getId(),title,description,content,bot.getCreateTime(),new Date());
        botMapper.updateById(new_bot);
        map.put("error_message","success");

        return map;
    }
}
