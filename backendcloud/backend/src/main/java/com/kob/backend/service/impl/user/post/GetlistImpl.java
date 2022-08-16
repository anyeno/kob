package com.kob.backend.service.impl.user.post;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.PostMaper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Post;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.post.GetList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GetlistImpl implements GetList {
    @Autowired
    private PostMaper postMaper;
    @Override
    public List<Post> getlist(Map<String, String> data) {
        //这一坨就是取出发出请求的user
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();//封装一个查询条件
        queryWrapper.eq("user_id",user.getId());

        return postMaper.selectList(queryWrapper);
    }
}
