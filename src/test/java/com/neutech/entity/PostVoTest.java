package com.neutech.entity;

import com.neutech.vo.PostVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostVoTest {
    @Test
    public void test(){
        PostVo postVo = new PostVo();
        postVo.setTitle("123");
        System.err.println(postVo);
    }
}
