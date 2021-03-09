package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxylm.springboot.dto.result.BooksUnitStateDto;
import com.cxylm.springboot.dto.result.MyBooksDto;
import com.cxylm.springboot.model.StudyBookRate;

import java.util.List;

/**
 * 学习课程表
 * @author HaoTi
 */
public interface StudyBookRateMapper extends BaseMapper<StudyBookRate> {

    /**
     * 获取个人课程
     *
     * @param userId
     * @return
     */
    List<MyBooksDto> getMyBooks(Integer userId);

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
}
