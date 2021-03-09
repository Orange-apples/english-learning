package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.form.StudyForm;
import com.cxylm.springboot.dto.form.WordsTestSaveForm;
import com.cxylm.springboot.dto.result.BooksUnitStateDto;
import com.cxylm.springboot.dto.result.MyBooksDto;
import com.cxylm.springboot.enums.StudyType;
import com.cxylm.springboot.enums.TestType;
import com.cxylm.springboot.model.StudyBookRate;

import java.util.List;

/**
 * 学习课程表
 */
public interface StudyBookRateService extends IService<StudyBookRate> {
    /**
     * 校验课程开通情况
     *
     * @param bookId
     * @param userId
     * @return
     */
    boolean checkOpenState(Integer bookId, Integer userId);

    /**
     * 开通课程
     *
     * @param bookId
     * @param userId
     */
    void OpenCourse(Integer bookId, Integer userId, Long expireTime);

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
     * 添加学习记录
     *
     * @param form
     * @param userId
     */
    void addStudyBookRate(StudyForm form, Integer userId);

    /**
     * 根据bookId、userId、unitId获取StudyBookRate
     *
     * @param bookId
     * @param userId
     * @param unitId
     * @return
     */
    StudyBookRate getStudyBookRateByBookIdAndUserIdAndUnitId(Integer bookId, Integer userId, Integer unitId);

    /**
     * 根据学习类型更新课程单元状态
     *
     * @param studyType
     * @param studyBookRate
     */
    void updateStudyState(StudyType studyType, StudyBookRate studyBookRate);

    /**
     * 判断是否可以进行听读训练、单元测试和学后测试
     *
     * @param bookId
     * @param unitId
     * @param userId
     */
    void check(Integer bookId, Integer unitId, Integer userId);

    /**
     * 判断是否可以进行听读训练、单元测试和学后测试
     *
     * @param studyBookRate
     */
    void check(StudyBookRate studyBookRate);

    /**
     * 判断是否可以进行5单元测试，6一测到底，8学后测试
     *
     * @param testType
     * @param bookId
     * @param unitId
     * @param userId
     */
    void checkTest(TestType testType, Integer bookId, Integer unitId, Integer userId);

    /**
     * 5单元测试，7学前测试，8学后测试
     * 添加学习记录
     *
     * @param form
     * @param userId
     */
    void addStudyBookRateByTest(WordsTestSaveForm form, Integer userId);
}
