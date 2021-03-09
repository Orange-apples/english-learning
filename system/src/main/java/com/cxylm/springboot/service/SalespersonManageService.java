package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.model.Salesperson;

/**
 * 学区管理
 */
public interface SalespersonManageService extends IService<Salesperson> {
    void createSalesperson(String name);
}
