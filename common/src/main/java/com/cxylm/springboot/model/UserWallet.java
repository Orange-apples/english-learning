package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户钱包
 * @author HaoTi
 */
@Getter
@Setter
@TableName("user_wallet")
public class UserWallet extends Model<UserWallet> {

    /**
     * 用户id
     */
    @TableId
    private Integer userId;

    /**
     * 用户余额
     */
    private Integer amount;

    /**
     * 累计收益
     */
    private Integer incomeAll;

    /**
     * 今日收益
     */
    private Integer incomeToday;

    /**
     * 累计消费
     */
    private Integer expenseAll;

    /**
     * 今日消费
     */
    private Integer expenseToday;

    /**
     * 累积折扣
     */
    private Integer discountAll;

    /**
     * 今日折扣
     */
    private Integer discountToday;

}
