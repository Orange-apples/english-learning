package com.cxylm.springboot.dto.result;

import com.cxylm.springboot.enums.UserNoticeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNoticeDto {
    private UserNoticeType type;
    private String content;
    private long time;
}
