package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.WalletRecordMapper;
import com.cxylm.springboot.model.WalletRecord;
import com.cxylm.springboot.service.WalletRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 钱包相关
 *
 * @author HaoTi
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletRecordServiceImpl extends ServiceImpl<WalletRecordMapper, WalletRecord> implements WalletRecordService {

}
