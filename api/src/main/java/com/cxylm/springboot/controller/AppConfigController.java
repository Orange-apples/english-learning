package com.cxylm.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxylm.springboot.annotation.PublicAPI;
import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.dao.SystemConfigMapper;
import com.cxylm.springboot.model.SystemConfig;
import com.cxylm.springboot.properties.SystemConfigProperties;
import com.cxylm.springboot.properties.WxPayProperties;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.util.HttpUtil;
import com.cxylm.springboot.util.RedisCacheUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/config")
public class AppConfigController extends ApiController {

    private final SystemConfigProperties systemConfigProperties;
    private final WxPayProperties wxPayProperties;
    private final SystemConfigMapper systemConfigMapper;
    private final RedisCacheUtil redisCacheUtil;
    ;

    @GetMapping
    @PublicAPI
    public ResponseEntity getConfig() {
        Map<String, Object> map = new HashMap<>(16);

        map.put("ZIMG_SERVERS", systemConfigProperties.getZimgAccess());
        map.put("PHONE", systemConfigProperties.getPhone());
        map.put("PWD_KEY", systemConfigProperties.getRsaKey());
        map.put("WX_APP_ID", wxPayProperties.getWxAppId());
//        map.put("WX_JS_APP_ID", wxPayProperties.getWxJsAppId());
        map.put("WX_KEY", wxPayProperties.getWxKey());
        map.put("WEIXIN_APP_SECRET", wxPayProperties.getWxAppSecret());
//        map.put("WEIXIN_JS_APP_SECRET", wxPayProperties.getWxJsAppSecret());
        map.put("WITH_DRAW_RATE", systemConfigProperties.getWithdrawRate());
        map.put("baiduAccessToken", redisCacheUtil.get(CacheName.BAIDU_AI_ACCESS_TOKEN));

        return AppResponse.ok(map);
    }

    @GetMapping("/member")
    @PublicAPI
    public ResponseEntity getMemberConfig() {
        final SystemConfig previewConfig = systemConfigMapper.selectOne(new QueryWrapper<SystemConfig>().eq("`key`", "previewMode"));
        Map<String, Object> res = new HashMap<>();
        res.put("previewMode", previewConfig.getValue() != null && "1".equals(previewConfig.getValue()));
        return AppResponse.ok(res);
    }

    @GetMapping("/getWxOpenId")
    @PublicAPI
    public ResponseEntity getWxOpenId(String code) {

        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map map = new HashMap<>(4);
        map.put("appid", wxPayProperties.getWxJsAppId());
        map.put("secret", wxPayProperties.getWxJsAppSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String ret = HttpUtil.doGet(url, map);
        Map result = JSON.parseObject(ret, Map.class);
        return AppResponse.ok(result);
    }
}
