package com.cxylm.springboot.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.FieldPosition;

/**
 * @author dingzhiwei jmdhappy@126.com
 * @version V1.0
 * @Description: 金额工具类
 * @date 2017-07-05
 * @Copyright: www.xxpay.org
 */
public class AmountUtil {

    /**
     * 将字符串"元"转换成"分"
     *
     * @param str
     * @return
     */
    public static String convertDollar2Cent(String str) {
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer sb = df.format(Double.parseDouble(str),
                new StringBuffer(), new FieldPosition(0));
        int idx = sb.toString().indexOf(".");
        sb.deleteCharAt(idx);
        for (; sb.length() != 1; ) {
            if (sb.charAt(0) == '0') {
                sb.deleteCharAt(0);
            } else {
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串"分"转换成"元"（长格式），如：100分被转换为1.00元。
     *
     * @param s
     * @return
     */
    public static String convertCent2Dollar(String s) {
        if ("".equals(s) || s == null) {
            return "";
        }
        long l;
        if (s.length() != 0) {
            if (s.charAt(0) == '+') {
                s = s.substring(1);
            }
            l = Long.parseLong(s);
        } else {
            return "";
        }
        boolean negative = false;
        if (l < 0) {
            negative = true;
            l = Math.abs(l);
        }
        s = Long.toString(l);
        if (s.length() == 1) {
            return (negative ? ("-0.0" + s) : ("0.0" + s));
        }
        if (s.length() == 2) {
            return (negative ? ("-0." + s) : ("0." + s));
        } else {
            return (negative ? ("-" + s.substring(0, s.length() - 2) + "." + s
                    .substring(s.length() - 2)) : (s.substring(0,
                    s.length() - 2)
                    + "." + s.substring(s.length() - 2)));
        }
    }

    /**
     * 将字符串"分"转换成"元"（短格式），如：100分被转换为1元。
     *
     * @param s
     * @return
     */
    public static String convertCent2DollarShort(String s) {
        String ss = convertCent2Dollar(s);
        ss = "" + Double.parseDouble(ss);
        if (ss.endsWith(".0")) {
            return ss.substring(0, ss.length() - 2);
        }
        if (ss.endsWith(".00")) {
            return ss.substring(0, ss.length() - 3);
        } else {
            return ss;
        }
    }

    /**
     * 扣除手续费
     *
     * @param amount
     * @param fee    费率
     * @return
     */
    public static Long reduceCharge(Long amount, Double fee) {
//        Long l=new BigDecimal(amount).multiply(new BigDecimal(994l)).divide(new BigDecimal(1000l)).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
        Long l = BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(fee)).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
        return l;
    }

    /**
     * 计算折扣（四舍五入）
     *
     * @param amount
     * @param discount 折扣
     * @return
     */
    public static Integer discountCharge(Integer amount, Double discount) {
        return BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(discount)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    /**
     * 计算折扣(向上取整)
     *
     * @param amount
     * @param discount 折扣
     * @return
     */
    public static Integer discountChargeByRoundUp(Integer amount, Double discount) {
        return BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(discount)).setScale(0, BigDecimal.ROUND_UP).intValue();
    }

    /**
     * 计算折扣(向下取整)
     *
     * @param amount
     * @param discount 折扣
     * @return
     */
    public static Integer discountChargeByRoundDown(Integer amount, Double discount) {
        return BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(discount)).setScale(0, BigDecimal.ROUND_DOWN).intValue();
    }

    /**
     * 计算折扣5%
     *
     * @param amount
     * @return
     */
    public static Integer discountCharge5(Integer amount) {
        if (amount > 0) {
            return BigDecimal.valueOf(amount).multiply(new BigDecimal("0.05")).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        } else {
            return 0;
        }
    }

    /**
     * 计算折扣45%(向下取整)
     *
     * @param amount
     * @return
     */
    public static Integer discountCharge45(Integer amount) {
        if (amount > 0) {
            return BigDecimal.valueOf(amount).multiply(new BigDecimal("0.45")).setScale(0, BigDecimal.ROUND_DOWN).intValue();
        } else {
            return 0;
        }
    }

    /**
     * 计算折扣50%
     *
     * @param amount
     * @param discount 折扣
     * @return
     */
    public static Integer discountCharge50(Integer amount, Double discount) {
        if (amount > 0 && discount > 0) {
            return BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(discount)).multiply(new BigDecimal(50)).divide(new BigDecimal(100L)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        } else {
            return 0;
        }
    }

    /**
     * 计算折扣55%
     *
     * @param amount
     * @param discount 折扣
     * @return
     */
    public static Integer discountCharge55(Integer amount, Double discount) {
        if (amount > 0 && discount > 0) {
            return BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(discount)).multiply(new BigDecimal(55)).divide(new BigDecimal(100L)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        } else {
            return 0;
        }
    }

    /**
     * 用户提现扣除手续费
     *
     * @param amount
     * @param withdrawRate
     */
    public static Integer feeChange(Integer amount, Float withdrawRate) {
        return BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(withdrawRate)).divide(new BigDecimal(100L)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    /**
     * 换算折扣
     * 将9折变为0.9
     * @return
     */
    public static Double changeDiscount(Double discount) {
        if(discount == null){
            return 0D;
        }
        return BigDecimal.valueOf(discount).divide(new BigDecimal(10L)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
