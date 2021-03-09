package com.cxylm.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxylm.springboot.model.UserWallet;
import org.apache.ibatis.annotations.Param;

/**
 * 用户钱包
 * @author HaoTi
 */
public interface UserWalletMapper extends BaseMapper<UserWallet> {

    /**
     * 用户消费
     * 减少余额
     *
     * @param userId
     * @param amount
     * @param discountAmount
     * @return
     */
    boolean decreaseBalance(@Param("userId") Integer userId, @Param("amount") Integer amount, @Param("discountAmount") int discountAmount);

    /**
     * 收益
     * @param userId
     * @param amount
     */
    void increaseBalance(@Param("userId") Integer userId, @Param("amount") Integer amount);

    /**
     * 提现扣款
     * @param userId
     * @param amount
     */
    boolean doWithdraw(@Param("userId") Integer userId, @Param("amount") Integer amount);
}
