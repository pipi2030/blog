package com.neutech.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("s_user")
@Data
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String userName;
    private String pass;
    private String headImage;
}
