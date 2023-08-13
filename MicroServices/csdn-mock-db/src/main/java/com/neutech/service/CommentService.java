package com.neutech.service;

import com.neutech.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> selectCommentByPostId(Integer postId);
    int addComment(Comment comment);
}
