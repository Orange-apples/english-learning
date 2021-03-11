package com.cxylm.springboot.dto.wordRecord;

import lombok.Data;

import java.util.Date;

/**
 * @author tang
 * @date 2021-03-11 14:34
 */
@Data
public class StudentTestRecordDto {
    private Date date;
    private Integer count;
    private Integer type;
    private Integer score;
    private Integer time;

}
