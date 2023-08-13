package com.neutech.service.impl;

import com.neutech.entity.Comment;
import com.neutech.mapper.CommentMapper;
import com.neutech.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Override
    @Cacheable(value = "comments",key = "#postId")
    public List<Comment> selectCommentByPostId(Integer postId) {
        return commentMapper.selectCommentByPostId(postId);
    }

    @Override
    public int addComment( Comment comment) {
        return commentMapper.insert(comment);
    }
}
