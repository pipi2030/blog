package com.neutech.service;

import com.neutech.entity.Post;

import java.io.IOException;
import java.util.List;

public interface ESService {
//    向es批量插入数据
    void createBulkDocument(List<Post> list) throws IOException;
//    向es查询数据
    List<Post> query(String input) throws IOException;
//    删除es索引
    void deleteIndex() throws IOException;
}
