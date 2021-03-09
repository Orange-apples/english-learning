package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.bookrate.BookStudyReport;
import com.cxylm.springboot.dto.form.StudyForm;
import com.cxylm.springboot.dto.form.WordsTestSaveForm;
import com.cxylm.springboot.dto.result.BooksUnitStateDto;
import com.cxylm.springboot.dto.result.MyBooksDto;
import com.cxylm.springboot.enums.StudyType;
import com.cxylm.springboot.enums.TestType;
import com.cxylm.springboot.model.StudyBookRate;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 学习课程表
 */
public interface StudyBookRateService extends IService<StudyBookRate> {

    /**
     * 学习报告列表信息
     * @param page
     * @param userId
     * @return
     */
    Page<BookStudyReport> bookStudyReport(Page<BookStudyReport> page, Integer userId);

    /**
     * 获取用户试用的课程数量
     * @param userId
     * @return
     */
    int getTryCourseCount(Integer userId);

    /**
     * 通过金币购买课程
     * @param bookId
     * @param userId
     * @param unitId
     * @return
     */
    @Transactional
    ResponseEntity buyUnit(Integer bookId, Integer userId, Integer unitId);

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
    @Transactional
    void OpenCourse(Integer bookId, Integer userId, @Nullable Long expireTime);

    /**
     * 保存一条试用课程信息
     * @param bookId
     * @param userId
     * @param expireTime
     */
    @Transactional
    void saveFreeCourse(Integer bookId, Integer userId, @Nullable Long expireTime);

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
    List<MyBooksDto> getBooksByStatus(Integer userId,Integer status);

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
    @Transactional
    StudyBookRate addStudyBookRate(StudyForm form, Integer userId);

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
     * @param overState     学习完成状态
     */
    void updateStudyState(StudyType studyType, StudyBookRate studyBookRate, boolean overState);

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
    @Transactional
    void addStudyBookRateByTest(WordsTestSaveForm form, Integer userId);

    List<StudyBookRate> scanExpiredCourse();

    @Transactional
    void openFreeCourse(Integer bookId, Integer userId);

    /**
     * 查看用户对应的课程是否为试用
     * @param bookId
     * @param userId
     * @return 0不为试用,1为试用
     */
    Integer getBookIsTry(Integer bookId, Integer userId);

    /**
     * 检查课程是否过期
     */
    boolean isCourseExpired(Integer bookId, Integer userId);
}
