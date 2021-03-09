package com.cxylm.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.result.WordsDto;
import com.cxylm.springboot.enums.WordBookType;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.StudyWordRecordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 单词本
 */
@RestController
@RequestMapping("/api/wordBook")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WordBookController extends ApiController {

    private final StudyWordRecordsService studyWordRecordsService;

    /**
     * 单词本
     * @return
     */
    @GetMapping
    @Transactional
    public Object getWordBook(@RequestParam final WordBookType type) {
        Page<WordsDto> words = studyWordRecordsService.getWordBook(ApiPageFactory.getPage(), getUserId(), type);
        Map<String, Page<WordsDto>> map = new HashMap<>(2);
        map.put("words", words);
        return AppResponse.ok(map);
    }
}
