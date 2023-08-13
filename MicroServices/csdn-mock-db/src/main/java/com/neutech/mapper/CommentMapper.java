package com.neutech.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neutech.entity.Comment;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {
    List<Comment>selectCommentByPostId(Integer postId);
//    int addComment(Comment comment);
}
