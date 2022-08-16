package com.kob.backend.service.user.images;

import com.kob.backend.pojo.Images;

import java.util.List;
import java.util.Map;

public interface GetImageService {
    List<Images> get_image(Map<String,String> data) ;
}
