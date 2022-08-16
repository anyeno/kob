package com.kob.backend.service.user.post;

import com.kob.backend.pojo.Post;

import java.util.List;
import java.util.Map;

public interface GetList {
    List<Post> getlist(Map<String,String> data);
}
