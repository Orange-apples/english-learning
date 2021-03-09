package com.cxylm.springboot.util;


import cn.hutool.core.date.DatePattern;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description: 生成全局唯一序列号工具类
 * @author HaoTi
 * @date 2019-01-25
 * @version V1.0
 */
public class MySeq {

	private static AtomicLong pay_seq = new AtomicLong(0L);
	private static String pay_seq_prefix = "P";
	private static AtomicLong trans_seq = new AtomicLong(0L);
	private static String trans_seq_prefix = "T";
	private static String deposit_seq_prefix = "D";
	private static String withdraw_seq_prefix = "W";
	private static AtomicLong clear_seq = new AtomicLong(0L);
	private static String clear_seq_prefix = "C";
	private static AtomicLong refund_seq = new AtomicLong(0L);
	private static String refund_seq_prefix = "R";

	/**
	 * 订单id生成
	 */
	private static AtomicLong order_seq = new AtomicLong(0L);
	private static String order_seq_prefix = "D";

	private static String node = "00";
	static {
		try {
			//URL url = Thread.currentThread().getContextClassLoader().getResource("config" + File.separator + "system.properties");
			//Properties properties = new Properties();
			//properties.load(url.openStream());
			//node = properties.getProperty(ConfigEnum.SERVER_NAME.getKey());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getPay() {
		return getSeq(pay_seq_prefix, pay_seq);
	}

	public static String getTrans() {
		return getSeq(trans_seq_prefix, trans_seq);
	}

	public static String getDeposit() {
		return getSeq(deposit_seq_prefix, trans_seq);
	}

	public static String getWithdraw() {
		return getSeq(withdraw_seq_prefix, trans_seq);
	}

	public static String getClear() {
		return getSeq(clear_seq_prefix, clear_seq);
	}

	public static String getRefund() {
		return getSeq(refund_seq_prefix, refund_seq);
	}

	public static String getOrder() {
		return getSeq(order_seq_prefix, order_seq);
	}

	private static String getSeq(String prefix, AtomicLong seq) {
		prefix += node;
		// TODO 需要保证唯一、无序
		return String.format("%s%s%06d", prefix, DatePattern.PURE_DATETIME_MS_FORMAT.format(new Date()), ThreadLocalRandom.current().nextInt(1, 999999));
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println("pay=" + getPay());
			System.out.println("trans=" + getTrans());
			System.out.println("refund=" + getRefund());
		}

	}

}
