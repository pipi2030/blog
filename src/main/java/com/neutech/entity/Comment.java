package com.neutech.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
@TableName("s_comment")
public class Comment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer commentId;
    private Integer postId;
    private Integer userId;
    private String commentContent;
    private Date createTime;
    private Integer replyToCommentId;
    private Integer likes;

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @TableField(exist = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment() {
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", postId=" + postId +
                ", userId=" + userId +
                ", commentContent='" + commentContent + '\'' +
                ", createTime=" + createTime +
                ", replyToCommentId=" + replyToCommentId +
                ", likes=" + likes +
                ", user=" + user +
                '}';
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getreplyToCommentId() {
        return replyToCommentId;
    }

    public void setreplyToCommentId(Integer replyToCommentId) {
        this.replyToCommentId = replyToCommentId;
    }

    public Comment(Integer commentId, Integer postId, Integer userId, String commentContent, Date createTime, Integer replyToCommentId) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.createTime = createTime;
        this.replyToCommentId = replyToCommentId;
    }
}
