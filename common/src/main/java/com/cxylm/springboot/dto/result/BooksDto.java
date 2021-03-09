package com.cxylm.springboot.dto.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooksDto {
    /**
     * 课程id
     */
    private Integer id;
    /**
     * 课本名称
     */
    private String name;
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

    /**
     * 是否免费
     */
    private boolean free;

    /**
     * 表示是否为试用,1为试用(未开通也标记为试用),2为已开通
     */
    private Integer isTry;
}
