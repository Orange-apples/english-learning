package com.cxylm.springboot.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PayParams {

	private String mchId;

	private String appId;

	private String key;
}
