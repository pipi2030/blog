package com.neutech.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
@TableName("s_post")
@Data
@Accessors(chain = true)
public class Post implements Serializable {
    @TableId(type = IdType.AUTO)
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
    @TableField(exist = false)
    private User user;
}
