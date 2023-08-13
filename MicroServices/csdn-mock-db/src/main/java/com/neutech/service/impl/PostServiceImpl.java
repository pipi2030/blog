package com.neutech.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neutech.entity.Post;
import com.neutech.mapper.PostMapper;
import com.neutech.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper,Post> implements PostService {
    @Autowired
    private PostMapper postMapper;
    @Override
    public List<Post> selectPostForRedis() {
        return this.query().last("limit 10000").list();
    }
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
