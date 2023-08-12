package com.neutech.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.neutech.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class postMapperTest {
    @Autowired
    private PostMapper postMapper;
    @Test
    public void Test(){
        Post post = postMapper.selectById(2);
        System.out.println(post);
    }
    @Test
    public void DelTest(){
        postMapper.deleteById(1);
    }
    @Test
    public void AddTest(){
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id",2);
        Post post = postMapper.selectOne(wrapper);
        post.setLikes(post.getLikes() + 1);
        postMapper.updateById(post);
    }
    @Test
    public void getPostByPostId(){
        Post post = postMapper.getPostByPostId(2);
        System.out.println(post);

    }
    @Test
    public void getPostByUserId(){
        List<Post> posts = postMapper.selectPostListByUserId(2);
        for (Post post : posts) {
            System.err.println(post);
        }
    }
    @Test
    public void insertPostTest(){
        Post post = new Post();
        post.setPostId(5);
        post.setContent("666");
        post.setCollections(0);
        postMapper.insert(post);
    }
}
