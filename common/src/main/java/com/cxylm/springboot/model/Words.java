package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * 单词信息
 * @author HaoTi
 */
@Getter
@Setter
@TableName("words")
public class Words extends Model<Words> {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 英语单词
     */
    private String word;

    /**
     * 汉语
     */
    private String mean;

    /**
     * 音标，美
     */
    private String symbolA;

    /**
     * 音标，英
     */
    private String symbolB;

    /**
     * 课本id
     */
    private Integer bookId;

    /**
     * 单元
     */
    private Integer unit;
}
