package com.neutech.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neutech.entity.Post;

import java.util.List;

public interface PostMapper extends BaseMapper<Post>{
//    List<Post> selectPostList(int currentPage);
    Post getPostByPostId(Integer postId);
    Integer getCountOfCommentsByPostId(Integer postId);
    List<Post> selectPostListByUserId(Integer userId);
}
