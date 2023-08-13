package com.neutech.controller;

import com.neutech.entity.Post;
import com.neutech.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostController {
    @Autowired
    private PostService postService;
    @GetMapping ("/post/addLikes")
    public void addLikes(Integer postId){
        postService.addLikesByPostId(postId);
    }
    @GetMapping("/post/addCollections")
    public void addCollections(Integer postId){
        postService.addCollectionsByPostId(postId);
    }
    @PostMapping("/getPost")
    public Post getPostByPostId(Integer postId){
        return postService.getPostByPostId(postId);
    }
    @GetMapping("/getCountOfComments")
    public int getCountOfCommentsByPostId(Integer postId){
        return postService.getCountOfCommentsByPostId(postId);
    }
    @GetMapping("/getCreation")
    public List<Post> getCreation(Integer userId){
        return postService.selectPostListByUserId(userId);
    }
    @PostMapping("/publishArticle")
    public int publishArticle(@RequestBody Post post){
        return postService.insertPost(post);
    }
}
