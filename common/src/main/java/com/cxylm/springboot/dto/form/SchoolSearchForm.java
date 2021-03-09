package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class SchoolSearchForm {

    /**
     * 校区名称
     */
    @Length(max = 32, message = "不能超过32个字")
    private String name;
}
