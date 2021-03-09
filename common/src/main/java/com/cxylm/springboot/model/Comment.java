package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("comment")
public class Comment extends Model<Comment> {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 店铺回复
     */
    private String reply;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 店铺id
     */
    private Integer storeId;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 图片
     */
    private String pics;

    private Date createTime;
}
