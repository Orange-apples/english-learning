package com.cxylm.springboot.util;

import cn.hutool.core.date.DateUtil;
import com.cxylm.springboot.enums.MemoryLevel;
import java.util.Date;

/**
 * 学习等级换算工具类
 */
public class StudyLevelUtil {

    /**
     * 学习等级换算
     *
     * @param now              当前时间
     * @param studyWordRecords 上次记忆时间
     * @return
     */
    public static MemoryLevel getLevel(Date now, Date studyWordRecords) {
        long day = DateUtil.betweenDay(studyWordRecords, now, false);
        if (day >= MemoryLevel.FIFTEEN_DAY.getValue()) {
            return MemoryLevel.FIFTEEN_DAY;
        } else if (day >= MemoryLevel.WEEK.getValue()) {
            return MemoryLevel.WEEK;
        } else if (day >= MemoryLevel.FOUR_DAY.getValue()) {
            return MemoryLevel.FOUR_DAY;
        } else if (day >= MemoryLevel.TWO_DAY.getValue()) {
            return MemoryLevel.TWO_DAY;
        } else if (day >= MemoryLevel.ONE_DAY.getValue()) {
            return MemoryLevel.ONE_DAY;
        } else {
            return MemoryLevel.HOUR;
        }
    }
}
