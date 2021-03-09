package com.cxylm.springboot.dto.bookrate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * @author wbh
 * @date 2020/8/19 14:32
 * @description
 */
@Getter
@Setter
public class BookRateDto {
    @NotNull(message = "bookId不能为空")
    private Integer bookId;

    @NotNull(message = "unitId")
    private Integer unitId;
}
