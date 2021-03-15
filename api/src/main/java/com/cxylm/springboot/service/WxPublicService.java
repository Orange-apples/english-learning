package com.cxylm.springboot.service;

public interface WxPublicService {
    void pushReportAll();

    void pushReportById(Integer uid);
}
