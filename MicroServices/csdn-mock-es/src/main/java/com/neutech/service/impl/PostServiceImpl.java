package com.neutech.service.impl;

import com.neutech.entity.Post;
import com.neutech.service.ESService;
import com.neutech.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private ESService esService;
    @Resource
    private RedisTemplate redisTemplate;
    @Override
//    @Cacheable(value = "posts",key = "#input")
    public List<Post> selectPostList(String input,int page,int userId) throws IOException {
        String key = input + "_" + page + "_" + userId;
//        St1.查询是否有相关数据，如果有，直接返回给前端
        Map<Integer,Post> map = (Map<Integer,Post>)redisTemplate.opsForHash().entries(key);
        if(map.size() != 0){
            System.out.println("缓存里存在数据");
            return new ArrayList<>(map.values());
        }
//        St2.如果没有，先向es查询数据，然后把数据插入redis，然后把数据传给前端
        else{
            System.out.println("缓存里不存在数据");
            List<Post> querry = esService.query(input);
            Map<Integer,Post> mapPost = new HashMap<>();
            for(Post post:querry){
                mapPost.put(post.getPostId(),post);
            }
            redisTemplate.opsForHash().putAll(key,mapPost);
//          为缓存设置随机的失效时间
            Random random = new Random();
            long expireTime = random.nextInt(12);
            redisTemplate.expire(key,expireTime, TimeUnit.HOURS);
            return querry;
        }
    }
}
