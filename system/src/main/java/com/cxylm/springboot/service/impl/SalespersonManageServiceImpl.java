package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.SalespersonManageMapper;
import com.cxylm.springboot.exception.AppBizException;
import com.cxylm.springboot.model.Salesperson;
import com.cxylm.springboot.service.SalespersonManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SalespersonManageServiceImpl extends ServiceImpl<SalespersonManageMapper, Salesperson> implements SalespersonManageService {

    @Override
    public void createSalesperson(String name) {
        Salesperson salesperson = new Salesperson();
        salesperson.setName(name);
        boolean b = salesperson.insert();
        if(!b){
            throw new AppBizException("录入销售员失败，请重新尝试！");
        }
    }
}
