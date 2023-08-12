package com.neutech.service;

import com.neutech.entity.Comment;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface CommentService {
    List<Comment> selectCommentByPostId(Integer postId);
    int addComment(Comment comment);
}
