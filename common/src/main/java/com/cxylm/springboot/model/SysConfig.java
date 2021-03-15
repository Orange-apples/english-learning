package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @auther Orange-apples
 * @date 2021/3/15 21:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_config")
public class SysConfig extends Model<StudyTestRecords> {
    private Integer id;
    private String keyName;
    private String keyValue;
    private String des;
}
