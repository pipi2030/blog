package com.neutech.controller;

import com.neutech.entity.Comment;
import com.neutech.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/comments")
    public List<Comment> getComments(Integer postId){
        return commentService.selectCommentByPostId(postId);
    }
    @PostMapping("/addComments")
    public int addComments(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
}
