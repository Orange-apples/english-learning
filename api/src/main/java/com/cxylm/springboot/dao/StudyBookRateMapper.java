package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.bookrate.BookStudyReport;
import com.cxylm.springboot.dto.result.BooksUnitStateDto;
import com.cxylm.springboot.dto.result.MyBooksDto;
import com.cxylm.springboot.model.StudyBookRate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 学习课程表
 * @author HaoTi
 */
public interface StudyBookRateMapper extends BaseMapper<StudyBookRate> {

    /**
     * 学校报告列表记录
     * @param page
     * @param userId
     * @return
     */
    @Select("SELECT bi.`name` book_name,sbr.book_id,sbr.unit_id,IF(sbr.state > 1,'是','否') passed FROM study_book_rate sbr LEFT JOIN book_info bi ON bi.id = sbr.book_id WHERE sbr.user_id = #{userId} AND unit_id <> 0 ")
    List<BookStudyReport> bookStudyReport(Page<BookStudyReport> page, @Param("userId") Integer userId);

    /**
     * 获取用户试用的课程id
     * @param userId
     * @return
     */
    int getTryCourseCount(@Param("userId") Integer userId);

    /**
     * 获取个人课程
     *
     * @param userId
     * @return
     */
    List<MyBooksDto> getMyBooks(Integer userId);

    /**
     * 根据用户和状态获取课程信息
     * @param userId 用户id
     * @param status 1为不过期,2为过期
     * @return
     */
    List<MyBooksDto> getBooksByStatus(@Param("userId") Integer userId, @Param("status") Integer status);

    /**
     * 该课程各单元学习状况
     *
     * @param bookId
     * @param userId
     * @return
     */
    List<BooksUnitStateDto> getBookUnitState(Integer bookId, Integer userId);

    /**
     * 校验是否可以进行一测到底
     * @param bookId
     * @param userId
     */
    boolean checkUntilTest(Integer bookId, Integer userId);

    StudyBookRate getStudyBookRateByBookIdAndUserIdAndUnitId(Integer bookId, Integer userId, Integer unitId);

    List<StudyBookRate> scanExpiredCourse(Long now);

    int getFreeCourseCount(Integer userId);


    @Select("SELECT EXISTS(SELECT 1 FROM study_book_rate WHERE user_id = #{userId} AND book_id = #{bookId} AND expire_time < #{now})")
    boolean isCourseExpired(Integer bookId, Integer userId, long now);
}
