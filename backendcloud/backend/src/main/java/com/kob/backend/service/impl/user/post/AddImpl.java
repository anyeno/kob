package com.kob.backend.service.impl.user.post;

import com.kob.backend.mapper.PostMaper;
import com.kob.backend.pojo.Post;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.post.Add;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddImpl implements Add {
    @Autowired
    private PostMaper postMaper;
    @Override
    public Map<String, String> add(Map<String, String> data) {
        //这一坨就是取出user
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();


        Date now_time = new Date();
        String content = data.get("content");

        Post post = new Post(null,user.getId(),content,now_time);
        postMaper.insert(post);

        Map<String,String> res = new HashMap<>();
        res.put("result","success");

        return res;
    }
}
