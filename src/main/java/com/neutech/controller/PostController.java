package com.neutech.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neutech.entity.Post;
import com.neutech.service.PostService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class PostController {
    @Autowired
    private PostService postService;
    @Value("${config.page.pageSize}")
    private int pageSize;
    @GetMapping("/post")
    public List<Post> getPosts(@RequestParam(required = false,defaultValue = "") String input,
                               @RequestParam(required = false,defaultValue = "1")int page,
                               @RequestParam(required = false,defaultValue = "1")Integer userId) throws IOException {
        return postService.selectPostList(input,page,userId);
}
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
