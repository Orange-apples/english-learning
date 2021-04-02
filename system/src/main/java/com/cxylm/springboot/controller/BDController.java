package com.cxylm.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cxylm.springboot.annotation.PublicAPI;
import com.cxylm.springboot.dto.BDStaDto;
import com.cxylm.springboot.dto.form.BDForm;
import com.cxylm.springboot.enums.EnableState;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.model.BD;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.SysBDService;
import com.cxylm.springboot.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.cxylm.springboot.constant.AppMessage.ERROR_RECORD_NOT_EXIST;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class BDController extends ApiController {

    private final SysBDService bdService;

    @PublicAPI
    @GetMapping("/bds")
    public ResponseEntity<?> userList(String name, String mobile) {
        QueryWrapper<BD> wrapper = new QueryWrapper<>();
        wrapper.eq("del", EnableState.ENABLED.getValue());
        if (StrUtil.isNotBlank(name)) {
            wrapper.like("name", name);
        }
        if (StrUtil.isNotBlank(mobile)) {
            wrapper.like("mobile", mobile);
        }
        IPage<BD> list = bdService.page(ApiPageFactory.getPage(), wrapper);
        return AppResponse.ok(list);
    }

    @GetMapping("/bd/{id}")
    public ResponseEntity<?> getEntity(@PathVariable Integer id) {
        final BD bd = bdService.getById(id);
        return AppResponse.ok(bd);
    }

    @PutMapping("/bd/{id}")
    public ResponseEntity<?> updateAppUser(@PathVariable Integer id, @RequestBody BD form) {
        final BD bd = bdService.getById(id);
        AssertUtil.badRequestWhenNull(bd, ERROR_RECORD_NOT_EXIST);
        BeanUtils.copyProperties(form, bd, "id", "createTime", "updateTime");
        bdService.updateById(bd);
        return AppResponse.ok(bd);
    }

    @PutMapping("/bd/{id}/switch-state")
    public ResponseEntity<?> switchState(@PathVariable Integer id, @RequestParam Integer state) {
        final BD bd = bdService.getById(id);
        bd.setDel(EnableState.values()[state]);
        bdService.updateById(bd);
        return AppResponse.ok(bd);
    }

    @PostMapping("/bd")
    public ResponseEntity<?> addEntity(@RequestBody @Validated BDForm form) {
        BD bd = new BD();
        BeanUtils.copyProperties(form, bd);
        bdService.save(bd);
        return SUCCESS;
    }

    @DeleteMapping("/bd/{id}")
    public ResponseEntity<?> deleteBD(@PathVariable Integer id) {
        final BD bd = bdService.getById(id);
        AssertUtil.badRequestWhenNull(bd, ERROR_RECORD_NOT_EXIST);
        bd.setDel(EnableState.DISABLED);
        bdService.updateById(bd);
        return SUCCESS;
    }

    /**
     * 销售员统计
     */
    @GetMapping("/bd/{bdId}/sta")
    public ResponseEntity<?> staBD(@PathVariable Integer bdId, @RequestParam(required = false) Long timestamp) {
        Date date = timestamp == null ? new Date() : new Date(timestamp);
        final BDStaDto monthSta = bdService.monthSta(bdId, date);
        monthSta.setName("月统计");
        final BDStaDto yearSta = bdService.yearSta(bdId, date);
        yearSta.setName("年统计");
        return AppResponse.ok(new BDStaDto[]{monthSta, yearSta});
    }
}
