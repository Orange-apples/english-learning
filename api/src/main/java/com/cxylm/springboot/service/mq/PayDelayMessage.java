package com.cxylm.springboot.service.mq;

import lombok.Setter;

import java.io.Serializable;

@Setter
public class PayDelayMessage implements Serializable {
    public Integer id;
    public PayOrderType type;

    @Override
    public String toString() {
        return "PayDelayMessage{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}
