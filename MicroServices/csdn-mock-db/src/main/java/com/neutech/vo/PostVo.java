package com.neutech.vo;

import lombok.Data;

import java.util.Date;

@Data
public class PostVo {
    private Integer postId;
    private String title;
    private String tags;
    private String content;
    private Date createTime;
    private Date updateTime;
    private Integer publisherId;
    private Integer readingCount;
    private Integer likes;
    private Integer collections;
    private String userName;
}
