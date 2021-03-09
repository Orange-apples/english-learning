package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 用户系统设置
 *
 * @author Haote
 * @since 2020-03-15 16:45:06
 */
@Data
@TableName("app_user_config")
public class AppUserConfig extends Model<AppUserConfig> {
    
    @TableId
    private Integer userId;
    /**
    * 自动播放音频
    */
    private Boolean autoAudio;
    /**
    * 是否自动复习
    */
    private Boolean autoReview;
    /**
    * 单词基础每组单词数量
    */
    private Integer wordPerGroup;
    /**
    * 达到指定单词数量后进行逆向回忆
    */
    private Integer mtReverseMemory;
    /**
    * 单元结束后是否进行整体回忆
    */
    private Boolean mtOverallMemory;
    /**
    * 最大复习单词数量
    */
    private Integer maxReviewWord;
    /**
    * 正确单词提示秒数
    */
    private Integer hintTime;
    /**
    * 发音设置，0,美音，1英音
    */
    private Integer pronunciationSettings;

}