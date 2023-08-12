package com.neutech.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neutech.entity.Post;
import com.neutech.mapper.PostMapper;
import com.neutech.service.ESService;
import com.neutech.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class PostServiceImpl extends ServiceImpl<PostMapper,Post> implements PostService {
    @Autowired
    private PostMapper postMapper;
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
            redisTemplate.expire(key,expireTime,TimeUnit.HOURS);
            return querry;
        }
    }
    @Override
    public List<Post> selectPostForRedis() {
        return this.query().last("limit 10000").list();
    }

//    @Override
//    public void insertES() throws IOException {
////      把数据分页，分批次的插入数据，”每页“一万条数据
//        for(int i = 1;i< postMapper.selectCount(null) + 1;i++){
//            Page<Post> posts = this.selectPostList("", i, 10000);
//            es.createBulkDocument(posts.getRecords());
//        }
//    }

    @Override
    public int addLikesByPostId(Integer postId) {
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id",postId);
        Post post = postMapper.selectOne(wrapper);
        post.setLikes(post.getLikes() + 1);
        return postMapper.updateById(post);
    }

    @Override
    public int addCollectionsByPostId(Integer postId) {
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id",postId);
        Post post = postMapper.selectOne(wrapper);
        post.setCollections(post.getCollections() + 1);
        return postMapper.updateById(post);
    }

    @Override
    @Cacheable(value = "post",key = "#postId")
    public Post getPostByPostId(Integer postId) {
        return postMapper.getPostByPostId(postId);
    }

    @Override
    public Integer getCountOfCommentsByPostId(Integer postId) {
        return postMapper.getCountOfCommentsByPostId(postId);
    }

    @Override
    @Cacheable(value = "myposts",key = "#userId")
    public List<Post> selectPostListByUserId(Integer userId) {
        return postMapper.selectPostListByUserId(userId);
    }

    @Override
    public int insertPost(Post post) {
        return postMapper.insert(post);
    }
}
