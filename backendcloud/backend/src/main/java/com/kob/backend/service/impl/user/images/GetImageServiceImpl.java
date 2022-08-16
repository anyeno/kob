package com.kob.backend.service.impl.user.images;

import com.kob.backend.mapper.ImagesMapper;
import com.kob.backend.pojo.Images;
import com.kob.backend.service.user.images.GetImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GetImageServiceImpl implements GetImageService {
    @Autowired
    private ImagesMapper imagesMapper;

    @Override
    public List<Images> get_image(Map<String, String> data) {
        List<Images> res = imagesMapper.selectList(null);
        return res;
    }
}
