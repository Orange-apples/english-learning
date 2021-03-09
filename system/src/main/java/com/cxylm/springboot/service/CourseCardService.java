package com.cxylm.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxylm.springboot.dto.form.CourseCardForm;
import com.cxylm.springboot.model.CourseCard;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CourseCardService extends IService<CourseCard> {
    @Transactional
    List<CourseCard> batchGenerate(CourseCardForm form);
}
