package com.cxylm.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cxylm.springboot.enums.ChannelEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 消费记录实体类
 * @author HaoTi
 */
@Getter
@Setter
@TableName("wallet_record")
public class WalletRecord extends Model<WalletRecord> {
    private static final long serialVersionUID = -9211818426032350891L;
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 不折扣金额
     */
    private Integer outAmount;

    /**
     * 折扣
     */
    private Double discount;

    /**
     * 类型
     */
//    private WalletType type;

    /**
     * 标题
     */
    private String title;

    /**
     * 店铺id
     */
    private Integer storeId;

    /**
     * 买家id
     */
    private Integer benefitId;

    /**
     * 支付渠道
     */
    private ChannelEnum channel;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 提现人姓名
     */
    private String accountName;

    /**
     * 提现账号
     */
    private String accountNo;

    /**
     * 实际提现金额
     */
    private Integer originalAmount;

    /**
     * 服务费
     */
    private Integer fee;

    /**
     * 状态
     */
//    private WalletRecordType state;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 银行名
     */
    private String bankName;
}
