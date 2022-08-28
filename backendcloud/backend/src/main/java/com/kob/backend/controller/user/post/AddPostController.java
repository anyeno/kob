package com.kob.backend.controller.user.post;

import com.kob.backend.service.user.post.Add;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddPostController  {
    @Autowired
    private Add add;

    @PostMapping("/api/post/add/")
    public Map<String,String> add(@RequestParam Map<String, String> data) {
        return add.add(data);
    }
}
