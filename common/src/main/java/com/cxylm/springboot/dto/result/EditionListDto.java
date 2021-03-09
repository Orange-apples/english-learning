package com.cxylm.springboot.dto.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditionListDto{
    /**
     * 版本id
     */
    private Integer editionId;
    /**
     * 版本名
     */
    private String name;
}
