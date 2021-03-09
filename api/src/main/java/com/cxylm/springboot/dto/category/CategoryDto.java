package com.cxylm.springboot.dto.category;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: shiyanru
 * @Date: 2019/9/26 22:46
 */
@Getter
@Setter
public class CategoryDto {

    private Integer categoryId;

    /**
     * 类型名称
     */
    private String categoryName;

    private Integer subId;

    /**
     * 类型名称
     */
    private String subName;
}
