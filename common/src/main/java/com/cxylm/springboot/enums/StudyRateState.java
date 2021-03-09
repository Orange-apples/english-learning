package com.cxylm.springboot.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StudyRateState implements IEnum<Integer> {
    /**
     * 单词学习状态，-1试用
     * 对比测试没有完成时
     * 0、未学，1、学习中，2、已学习，3、听读训练完成，4、单元训练完成，5、听读训练和单元训练完成
     * 对比测试已完成时
     * 10、未学，11、学习中，12、已学习，13、听读训练完成，14、单元训练完成，15、听读训练和单元训练完成
     */
    TRY(-1),
    CREATE(0),
    LEARNING(1),
    WORDS_OVER(2),
    LISTEN_OVER(3),
    UNIT_OVER(4),
    LISTEN_AND_UNIT_OVER(5),
    TEST_AND_CREATE(10),
    TEST_AND_LEARNING(11),
    TEST_AND_WORDS_OVER(12),
    TEST_AND_LISTEN_OVER(13),
    TEST_AND_UNIT_OVER(14),
    TEST_LISTEN_AND_UNIT_OVER(15);

    private final int value;

    StudyRateState(final int value) {
        this.value = value;
    }
//    @JsonCreator
//    public static StudyRateState valueOfInt(int value) {
//        for (StudyRateState type : StudyRateState.values()) {
//            if (type.value == value) {
//                return type;
//            }
//        }
//        return null;
//    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @JsonValue
    public int value() {
        return this.value;
    }
}
