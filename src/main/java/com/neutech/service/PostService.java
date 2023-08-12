package com.neutech.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neutech.entity.Post;
import com.neutech.vo.PostVo;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PostService extends IService<Post> {
//    List<Post> selectPostList(String input,int currentPage,int pageSize) throws IOException;
    List<Post> selectPostList(String input,int page,int userId) throws IOException;
    List<Post> selectPostForRedis();
//    void insertES() throws IOException;
    int addLikesByPostId(Integer postId);
    int addCollectionsByPostId(Integer postId);
    Post getPostByPostId(Integer postId);
    Integer getCountOfCommentsByPostId(Integer postId);
    List<Post> selectPostListByUserId(Integer userId);
    int insertPost(Post post);
}
