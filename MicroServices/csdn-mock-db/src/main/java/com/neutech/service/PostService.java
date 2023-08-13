package com.neutech.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neutech.entity.Post;

import java.util.List;

public interface PostService extends IService<Post> {
    List<Post> selectPostForRedis();
    int addLikesByPostId(Integer postId);
    int addCollectionsByPostId(Integer postId);
    Post getPostByPostId(Integer postId);
    Integer getCountOfCommentsByPostId(Integer postId);
    List<Post> selectPostListByUserId(Integer userId);
    int insertPost(Post post);
}
