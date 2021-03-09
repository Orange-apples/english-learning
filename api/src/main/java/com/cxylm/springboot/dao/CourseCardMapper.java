package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxylm.springboot.model.CourseCard;
import org.apache.ibatis.annotations.Update;

public interface CourseCardMapper extends BaseMapper<CourseCard> {
    @Update("UPDATE course_card SET use_count = use_count + 1 WHERE id = #{cardId}")
    boolean incUseCount(Long cardId);
}
