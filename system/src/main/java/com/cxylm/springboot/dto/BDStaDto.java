package com.cxylm.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BDStaDto {
    /**
     * 课程计数
     */
    private int courseCount;

    /**
     * 课程销售额
     */
    private int courseSellPrice;
    private String name;
}
