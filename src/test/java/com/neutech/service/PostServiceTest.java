package com.neutech.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neutech.entity.Post;
import com.neutech.mapper.PostMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostService postService;
    @Test
    public void test() throws IOException {
//        postService.insertES();
    }
}
