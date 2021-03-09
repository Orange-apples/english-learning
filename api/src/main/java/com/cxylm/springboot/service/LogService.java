package com.cxylm.springboot.service;

import com.cxylm.springboot.model.MQFailLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LogService {
    //private final ExecutorService executorService;

    @Async("cxylmTaskExecutor")
    public void insertMQFailLog(MQFailLog mqFailLog) {
        log.info("插入MQ失败日志");
        mqFailLog.insert();
    }
}
