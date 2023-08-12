package com.neutech.service.impl;

import com.neutech.entity.Post;
import com.neutech.service.PostService;
import com.neutech.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private PostService postService;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public void insertRedis() {
        //向redis中插入一万条数据
        Random random = new Random();
        List<Post> posts = postService.selectPostForRedis();
        for(Post post:posts){
            long expireTime =  200 + random.nextInt(300);
            redisTemplate.opsForValue().set(post.getPostId().toString(),post,expireTime);
        }
    }
}
