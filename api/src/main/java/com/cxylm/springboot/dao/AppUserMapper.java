package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxylm.springboot.dto.result.AppuserDto;
import com.cxylm.springboot.model.AppUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppUserMapper extends BaseMapper<AppUser> {
    AppUser findByMobile(@Param("mobile") String mobile, @Param("merchant") boolean merchant);

    AppUser findByUsername(@Param("username") String username, @Param("merchant") boolean merchant);

    /**
     * 学习状态更新
     *
     * @param userId
     * @param bookId
     * @param unitId
     * @param wordId
     * @param unitState
     * @param listenUnitId
     * @param coin
     * @param learningTime
     * @param spellTime
     * @param lastReviewTime
     * @return
     */
    boolean updateStudyState(Integer userId, Integer bookId, Integer unitId, Integer wordId, Integer unitState, Integer listenUnitId, Integer coin, Integer learningTime, Integer spellTime, Long lastReviewTime);

    /**
     * 更新测试时长
     * @param userId
     * @param times
     * @return
     */
    boolean updateTestTime(Integer userId, Integer times);

    /**
     * 查询当日新增学员
     * @param accountState
     * @return
     */
    Integer todayIncrese(@Param("accountState") Integer accountState, @Param("merchant") Integer merchant, @Param("schoolId") Integer schoolId);

    /**
     * 根据会员名查询学员访问信息
     * @param nickName
     * @param accountState
     * @return
     */
    List<AppuserDto> selectStudentVisitInfo(Page<AppuserDto> page, @Param("schoolId") Integer schoolUserId, @Param("nickName") String nickName,
                                            @Param("accountState") Integer accountState, @Param("merchant") Integer merchant);
}
