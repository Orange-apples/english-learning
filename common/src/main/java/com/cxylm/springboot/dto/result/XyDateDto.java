package com.cxylm.springboot.dto.result;

import lombok.Getter;
import lombok.Setter;

/**
 * 柱状图xy数据
 */
@Getter
@Setter
public class XyDateDto {
    /**
     * 日期
     */
    private String xData;
    /**
     * 学习数
     */
    private Integer yData;
}
