package com.cxylm.springboot.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cxylm.springboot.enums.MQFailType;
import lombok.Getter;
import lombok.Setter;


@TableName("mq_fail_log")
@Getter
@Setter
public class MQFailLog extends Model<MQFailLog> {
    private static final long serialVersionUID = 3320289177397878567L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer delay;
    private MQFailType failType;
    private boolean isHandled;
    private String exchangeName;
    private String routingKey;
    private String payload;

    public MQFailLog() {

    }

    public MQFailLog(MQFailType failType, String exchangeName, String routingKey, Object payload) {
        this.failType = failType;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.payload = JSON.toJSONString(payload, SerializerFeature.WriteClassName);
    }
}
