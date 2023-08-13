package com.neutech.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("s_comment")
@Data
public class Comment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer commentId;
    private Integer postId;
    private Integer userId;
    private String commentContent;
    private Date createTime;
    private Integer replyToCommentId;
    private Integer likes;
    @TableField(exist = false)
    private User user;
}
