package com.neutech.mapper;

import com.neutech.entity.Comment;
import com.neutech.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommentMapperTest {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentService commentService;
    @Test
    public void test(){
        List<Comment> comments = commentMapper.selectCommentByPostId(3);
        for (Comment comment : comments) {
            System.err.println(comment);
        }
    }
    @Test
    public void InsertTest(){
        Comment comment = new Comment();
        comment.setCommentId(14);
        comment.setUserId(1);
        comment.setPostId(2);
        comment.setCommentContent("难死我了");
        System.out.println(comment);
        commentMapper.insert(comment);
    }
    @Test
    public void CommentServiceTest(){
        Comment comment = new Comment();
        comment.setCommentId(12);
        comment.setUserId(1);
        comment.setPostId(2);
        comment.setCommentContent("怎么还出不来啊");
        commentService.addComment(comment);
    }
}
