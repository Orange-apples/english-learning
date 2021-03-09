package com.cxylm.springboot.service.mq;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MQRetryMsg implements Serializable {
    private static final long serialVersionUID = 1360812751418511138L;
    private int retry;
    private int maxRetry = 5;
}
