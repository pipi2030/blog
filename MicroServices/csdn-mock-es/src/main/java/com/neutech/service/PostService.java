package com.neutech.service;

import com.neutech.entity.Post;

import java.io.IOException;
import java.util.List;

public interface PostService {
    List<Post> selectPostList(String input, int page, int userId) throws IOException;
}
