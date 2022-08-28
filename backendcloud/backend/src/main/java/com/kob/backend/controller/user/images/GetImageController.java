package com.kob.backend.controller.user.images;

import com.kob.backend.pojo.Images;
import com.kob.backend.service.user.images.GetImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GetImageController {
    @Autowired
    private GetImageService getImageService;

    @GetMapping("/api/images/get/")
    public List<Images> get_images(Map<String,String> data){
        return getImageService.get_image(data);
    }
}
