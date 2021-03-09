package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cxylm.springboot.enums.BookLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 课本信息
 * @author HaoTi
 */
@Getter
@Setter
@TableName("book_info")
public class BookInfo extends Model<BookInfo> {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 课本名称
     */
    private String name;

    /**
     * 阶段（1、小学英语，2、初中英语，3、高中英语，4、大学英语，5、托福/雅思）
     */
    private BookLevel level;

    /**
     * 版本
     */
    private String edition;

    /**
     * 出版社
     */
    private String press;

    /**
     * 课程详情
     */
    private String detail;

    /**
     * 课程图片
     */
    private String pic;

    private Integer price;

    private Boolean free;
}
