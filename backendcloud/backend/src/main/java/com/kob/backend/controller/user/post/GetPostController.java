package com.kob.backend.controller.user.post;

import com.kob.backend.pojo.Post;
import com.kob.backend.service.user.post.GetList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GetPostController {
    @Autowired
    private GetList getList;

    @GetMapping("/post/getlist/")
    public List<Post> getlist(@RequestParam Map<String,String> data) {
        return getList.getlist(data);
    }
}
