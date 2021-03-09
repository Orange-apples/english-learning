package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxylm.springboot.model.CourseCardBind;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface CourseCardBindMapper extends BaseMapper<CourseCardBind> {
    @Select("SELECT EXISTS(SELECT 1 FROM course_card_bind WHERE card_id = #{cardId} AND book_id = #{bookId})")
    boolean isBind(int bookId, long cardId);

    //@Update("UPDATE course_card_bind SET bind_user_id = #{userId}, bind_user_time = UNIX_TIMESTAMP()*1000 WHERE card_id = #{cardId} AND book_id = #{bookId}")
    @Update("UPDATE course_card_bind SET bind_user_id = #{userId}, bind_user_time = NOW() WHERE card_id = #{cardId} AND book_id = #{bookId}")
    int bindUser(int userId, int bookId, long cardId);
}
