package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 用户评论
 */
@Getter
@Setter
public class CommentForm {

    /**
     * 评论内容
     */
    @NotNull
    @Length(max = 255, message = "评论内容最多255字")
    private String content;

    /**
     * 图片
     */
    private String pics;

    /**
     * 店铺ID
     */
    private Integer storeId;

//    /**
//     * 父级评论
//     */
//    private Integer pid;

    /**
     * 评分
     */
    @NotNull(message = "请选择评分")
    @Positive(message = "评分必须为正数")
    @Min(value = 1, message = "评分最低为一星")
    @Max(value = 5, message = "评分最高为五星")
    private Integer score;

    @NotNull(message = "评论的订单不存在")
    private String orderId;
}
