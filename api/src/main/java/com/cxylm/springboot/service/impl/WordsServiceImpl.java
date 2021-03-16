package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.WordsMapper;
import com.cxylm.springboot.dto.form.StudyForm;
import com.cxylm.springboot.dto.form.WordsTestForm;
import com.cxylm.springboot.dto.result.TestWordsDto;
import com.cxylm.springboot.dto.result.WordsDto;
import com.cxylm.springboot.enums.StudyRateState;
import com.cxylm.springboot.enums.TestType;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.model.StudyBookRate;
import com.cxylm.springboot.model.Words;
import com.cxylm.springboot.service.WordsService;
import com.cxylm.springboot.util.SymbolUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WordsServiceImpl extends ServiceImpl<WordsMapper, Words> implements WordsService {

    /**
     * 获取学习单词
     *
     * @param form
     * @param studyBookRate
     * @return
     */
    @Override
    public List<WordsDto> getStudyWords(StudyForm form, StudyBookRate studyBookRate) {
        boolean b = studyBookRate != null && (studyBookRate.getState() == StudyRateState.LEARNING || studyBookRate.getState() == StudyRateState.TEST_AND_LEARNING);
        return baseMapper.getStudyWords(form, b ? studyBookRate.getLastWordsId() : null);
    }

    /**
     * 获取1汉译英，2英译汉，3听选，4听写单词
     *
     * @param form
     * @param userId
     * @return
     */
    @Override
    public Map<String, List<TestWordsDto>> getTestWords(WordsTestForm form, Integer userId) {
        Map<String, List<TestWordsDto>> map = new HashMap<>(2);

        //获取测试单词
        List<TestWordsDto> testWords = baseMapper.getTestWords(form, userId);
        if (testWords == null) {
            return null;
        }

        Integer size = testWords.size();
        int typeSize = form.getAutoTestType().size();
        int groupSize = size / typeSize;

        for (int i = 0; i < typeSize - 1; i++) {
            Map map2 = getTestWords(testWords.subList(i * groupSize, (i + 1) * groupSize), form.getAutoTestType().get(i));
            map.put((String) map2.get("key"), (List<TestWordsDto>) map2.get("value"));
        }

        int lastGroups = (typeSize - 1) * groupSize;
        Map map3 = getTestWords(testWords.subList(lastGroups, size), form.getAutoTestType().get(typeSize - 1));
        List<TestWordsDto> list = (List<TestWordsDto>) map3.get("value");
        HashSet<TestWordsDto> set = new HashSet<>(list);
        list = new ArrayList<>(set);
        map.put((String) map3.get("key"), list);

        return map;
    }

    /**
     * 循环获取测试单词
     *
     * @param subList
     * @param testType
     * @return
     */
    private Map getTestWords(List<TestWordsDto> subList, TestType testType) {
        Map<String, Object> map = new HashMap<>(4);
        switch (testType) {
            case CH_TO_EN:
                //汉译英，问题是汉语，选项是英语
                map.put("key", "chToEn");
                map.put("value", getTestAndBlurWords(subList, true));
                break;
            case EN_TO_CH:
                map.put("key", "enToCh");
                map.put("value", getTestAndBlurWords(subList, false));
                break;
            case LISTEN_TO_CH:
                map.put("key", "listenToCh");
                map.put("value", getTestAndBlurWords(subList, false));
                break;
            case LISTEN_TO_WRITE:
                map.put("key", "listenToWrite");
                map.put("value", subList);
                break;
        }
        return map;
    }

    /**
     * 获取添加混淆词后的测试单词
     *
     * @param testWords
     * @param isChToEn  是否是汉译英
     */
    private List<TestWordsDto> getTestAndBlurWords(List<TestWordsDto> testWords, boolean isChToEn) {
        int size = testWords.size();
        int testIndex = 0;
        while (testIndex < size) {
            BlurWord blurWord = new BlurWord();
            //获取混淆词汇
            int endIndex = 5;
            if (size < testIndex + 5) {
                endIndex = size - testIndex;
            }
            List<WordsDto> wordsDtos = baseMapper.getBlurWords(testWords.get(0).getBookId(), endIndex * 4);
            for (int i = 0; i < endIndex; i++) {
                //测试单词
                TestWordsDto testWordsDto = testWords.get(testIndex + i);

                //开始填充混淆词
                Map<String, String> blurWords = new HashMap<>(8);

                //种子
                Random random = new Random();
                int seed = random.nextInt(4);
                for (int j = 0; j < 4; j++) {
                    //获得ABCD四个选项
                    if (j == seed) {
                        //选项中的正确答案
                        blurWords.put((char) (65 + j) + "", isChToEn ? testWordsDto.getWord() : testWordsDto.getQuestion());
                        j++;
                        if (j == 4)
                            continue;
                    }
                    blurWords.put((char) (65 + j) + "", addBlurWord(testWordsDto, wordsDtos, blurWord, isChToEn));
                }
                //设置选项
                testWordsDto.setChoices(blurWords);
                if (!isChToEn) {
                    //不是汉译英，填充Question
                    testWordsDto.setQuestion(testWordsDto.getWord());
                }
                testWordsDto.setAnswer((char) (65 + seed) + "");
            }

            //每循环一次+5
            testIndex += 5;
        }
        return testWords;
    }

    /**
     * ABCD选项
     * 获取混淆词
     *
     * @param testWordsDto 源数据
     * @param wordsDtos    混淆源
     * @param blurWord     混淆源下标
     * @param isChToEn     是否是汉译英
     * @return
     */
    private String addBlurWord(TestWordsDto testWordsDto, List<WordsDto> wordsDtos, BlurWord blurWord, boolean isChToEn) {
        if (blurWord.blurIndex >= wordsDtos.size()) {
           return null ;
        }
        WordsDto wordsDto = wordsDtos.get(blurWord.blurIndex);
        if (wordsDto.getWordId().equals(testWordsDto.getWordId())) {
            //随机到正确答案，下标+1
            blurWord.blurIndex += 1;
            wordsDto = wordsDtos.get(blurWord.blurIndex);
        }
        blurWord.blurIndex += 1;
        if (isChToEn) {
            return wordsDto.getWord();
        }
        return wordsDto.getMean();
    }

    static class BlurWord {
        //混淆词内部类，想要实现同时返回blurIndex和混淆词
        int blurIndex = 0;
        String word;
    }

    /**
     * 获取5单元测试，6一测到底，7学前测试，8学后测试
     *
     * @param form
     * @param userId
     * @return
     */
    @Override
    public Map<String, List<TestWordsDto>> getAllTestWords(Page<TestWordsDto> page, WordsTestForm form, Integer userId) {
        Map<String, List<TestWordsDto>> map = new HashMap<>(8);
        List<TestWordsDto> testWordsDtos;
        //获取数据源
        if (TestType.UNTIL_TEST.equals(form.getTestType())) {
            //一测到底
            testWordsDtos = baseMapper.getUntilTestWords(page, form.getBookId(), userId);
        } else {
            testWordsDtos = baseMapper.getUnitTestWords(form);
        }
        if (testWordsDtos == null || testWordsDtos.size() == 0) {
            return null;
        }
        int size = testWordsDtos.size();
        //按照3:3:2:2分化资源
        int i = size * 3 / 10;
        int j = size * 2 / 10;
        //汉译英，问题是汉语，选项是英语
        List<TestWordsDto> chToEn = getTestAndBlurWords(testWordsDtos.subList(0, i), true);
        map.put("chToEn", chToEn);
        List<TestWordsDto> enToCh = getTestAndBlurWords(testWordsDtos.subList(i, 2 * i), false);
        map.put("enToCh", enToCh);
        List<TestWordsDto> listenToCh = getTestAndBlurWords(testWordsDtos.subList(2 * i, 2 * i + j), false);
        map.put("listenToCh", listenToCh);
        map.put("listenToWrite", testWordsDtos.subList(2 * i + j, size));
        return map;
    }

    /**
     * 添加释义
     *
     * @param wordsDtos
     */
    @Override
    public void addSymbol(List<WordsDto> wordsDtos) {
        List<Words> wordsList = new ArrayList<>();
        if (wordsDtos == null) {
            return;
        }
        wordsDtos.stream().filter(wordsDto -> wordsDto.getSymbolA() == null).forEach(wordsDto -> {
            String word = wordsDto.getWord();
            Map<String, String> map = null;
            Words words = new Words();
            words.setId(wordsDto.getWordId());

            try {
                map = SymbolUnit.getSymbol(word);

            } catch (Exception e) {
                log.error("获取音标出错", e);
                words.setSymbolA("");
                wordsList.add(words);
            }

            if (map != null) {
                wordsDto.setSymbolA(map.get("symbol_a"));
                wordsDto.setSymbolB(map.get("symbol_b"));
                words.setSymbolA(map.get("symbol_a"));
                words.setSymbolB(map.get("symbol_b"));
                wordsList.add(words);
            } else {
                words.setSymbolA("");
                wordsList.add(words);
            }
        });
        if (wordsList.size() > 0) {
            //添加注释到数据库
            boolean b = updateBatchById(wordsList);
            if (!b) {
                log.error("更新注释失败！");
                throw new AppBizException("系统异常！");
            }
        }
    }

    /**
     * 查询本单元单词数量
     *
     * @param bookId
     * @param unitId
     * @return
     */
    @Override
    public Long selectTotaleByUnit(Integer bookId, Integer unitId) {
        return baseMapper.selectTotaleByUnit(bookId, unitId);
    }

    /**
     * 获取总数，一测到底
     *
     * @param bookId
     * @return
     */
    @Override
    public Long getTotalByBookId(Integer bookId) {
        return baseMapper.getTotalByBookId(bookId);
    }

    @Override
    public int getMaxUnit(Integer bookId) {
        return baseMapper.getMaxUnit(bookId);
    }

    @Override
    public Integer getMaxWordIdByUnit(Integer bookId, Integer unitId) {
        if (bookId == null || unitId == null) {
            return 0;
        }
        return baseMapper.getMaxWordIdByUnit(bookId, unitId);
    }
}
