package com.neutech.controller;

import com.neutech.entity.Post;
import com.neutech.service.PostService;
import com.neutech.service.RedisService;
import com.neutech.service.TestService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class PostController {
    @Autowired
    private PostService postService;
    @DubboReference(interfaceClass = TestService.class,interfaceName = "${es.service.name}",version = "${es.service.version}")
    private TestService testService;
    @Value("${config.page.pageSize}")
    private int pageSize;
    @GetMapping("/post")
    public List<Post> getPosts(@RequestParam(required = false,defaultValue = "") String input,
                               @RequestParam(required = false,defaultValue = "1")int page,
                               @RequestParam(required = false,defaultValue = "1")Integer userId) throws IOException {
        return postService.selectPostList(input,page,userId);
    }
    @GetMapping("/test")
    public String test(){
        testService.printHello();
        return "123";
    }

}
