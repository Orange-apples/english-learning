package com.cxylm.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.result.XyDateDto;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.model.AppUser;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.AppUserService;
import com.cxylm.springboot.service.StudyBookRateService;
import com.cxylm.springboot.service.StudyTestRecordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习报告
 */
@RestController
@RequestMapping("/api/studyReport")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudyReportController extends ApiController {

    private final AppUserService appUserService;
    private final StudyBookRateService studyBookRateService;
    private final StudyTestRecordsService studyTestRecordsService;

    /**
     * 柱状图
     *
     * @return
     */
    @GetMapping("/histogram")
    @Transactional
    public Object histogram() {
        List<XyDateDto> xyDateDtos = studyTestRecordsService.histogram(getUserId());
        Map<String, List<XyDateDto>> map = new HashMap<>(2);
        map.put("histogramData", xyDateDtos);
        return AppResponse.ok(map);
    }

    /**
     * 饼状图
     *
     * @return
     */
    @GetMapping("/pieChart")
    @Transactional
    public Object pieChart() {
        AppUser appUser = appUserService.getById(getUserId());
        Integer learningTime = appUser.getLearningTime();
        Integer spellTime = appUser.getSpellTime();
        Integer testTime = appUser.getTestTime();
        int allTime = learningTime + spellTime + testTime;
        int learning = learningTime *100 / allTime;
        Map<String, Integer> map = new HashMap<>(4);
        map.put("learning", learning);
        map.put("spell", 100 - learning);
        return AppResponse.ok(map);
    }


    /**
     * 测试列表
     *
     * @return
     */
    @GetMapping("/list/{type}")
    @Transactional
    public Object studyReport(@PathVariable Integer type) {
        Page<?> studyReport = null;
        Integer userId = getUserId();
        //自主测试学习记录
        if(type.equals(0)){
            studyReport = studyTestRecordsService.getTestResult(ApiPageFactory.getPage(), userId);
        }else if(type.equals(1)){
            //课本单元学习记录
            studyReport = studyBookRateService.bookStudyReport(ApiPageFactory.getPage(),userId);
        }
        Map<String, Page<?>> map = new HashMap<>(2);
        map.put("testResult", studyReport);
        return AppResponse.ok(map);
    }
}
