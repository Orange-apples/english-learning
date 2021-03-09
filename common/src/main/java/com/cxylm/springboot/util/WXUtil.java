package com.cxylm.springboot.util;

import com.alibaba.fastjson.JSON;
import com.cxylm.springboot.config.property.AppProperty;
import com.cxylm.springboot.dto.request.WXSendTemplateMsg;
import com.cxylm.springboot.dto.request.WXSendTextMsg;
import com.cxylm.springboot.dto.response.WXOAuthResponse;
import com.cxylm.springboot.dto.response.WxAccessTokenResponse;
import com.cxylm.springboot.dto.response.WxBaseResponse;
import com.cxylm.springboot.exception.AppBadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.cxylm.springboot.constant.CacheName.WX_ACCESS_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WXUtil {
    public static final String WX_SERVER_HOST = "api.weixin.qq.com";
    public static final String BAIDU_SERVER_HOST = "aip.baidubce.com";
    public static final String WX_OAUTH_ACCESS_TOKEN_PATH = "/sns/oauth2/access_token";

    private final WebClient commonWebClient;
    private final AppProperty appProperty;
    private final RedisCacheUtil redisCacheUtil;

    public WXOAuthResponse getJsAPIAccessTokenByCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw new AppBadRequestException("获取用户信息失败");
        }
        try {
            String str = commonWebClient.get().uri(uriBuilder -> uriBuilder.scheme("https")
                    .host(WX_SERVER_HOST)
                    .path(WX_OAUTH_ACCESS_TOKEN_PATH)
                    .queryParam("appid", appProperty.getWxAppId())
                    .queryParam("secret", appProperty.getWxSecret())
                    .queryParam("code", code)
                    .queryParam("grant_type", "authorization_code")
                    .build())
                    .retrieve().bodyToMono(String.class).block(Duration.ofSeconds(15));
            WXOAuthResponse response = JSON.parseObject(str, WXOAuthResponse.class);
            if (response == null) return null;
            if (response.getErrcode() != null) {
                log.warn("获取用户信息失败. Error code: {}, error msg: {}", response.getErrcode(), response.getErrmsg());
                return null;
            }
            return response;
        } catch (Exception e) {
            log.error("[微信登录] 获取用户信息失败", e);
            return null;
        }
    }

    public void sendTextMsgToSpecificUser(String openid, String msg) {
        if (StringUtils.isBlank(msg)) {
            return;
        }
        WXSendTextMsg textMsg = new WXSendTextMsg(openid, msg);
        String accessToken = (String) redisCacheUtil.get(WX_ACCESS_TOKEN);
        try {
            String str = commonWebClient.post()
                    .uri("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(Mono.just(textMsg), WXSendTextMsg.class)
                    .retrieve().bodyToMono(String.class).block(Duration.ofSeconds(15));
            WxBaseResponse response = JSON.parseObject(str, WxBaseResponse.class);
            if (response == null) {
                log.warn("[微信公众号]发送信息失败, 服务器响应null");
                return;
            }
            if (!"0".equals(response.getErrcode())) {
                log.warn("[微信公众号]发送信息失败. Error code: {}, error msg: {}", response.getErrcode(), response.getErrmsg());
            }
        } catch (Exception e) {
            log.error("[微信公众号] 发送信息失败", e);
        }
    }

    public void sendTemplateMsgToSpecificUser(String openid, WXSendTemplateMsg templateMsg) {
        if (templateMsg == null) return;

        String accessToken = (String) redisCacheUtil.get(WX_ACCESS_TOKEN);
        try {
            String str = commonWebClient.post()
                    .uri("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(Mono.just(templateMsg), WXSendTemplateMsg.class)
                    .retrieve().bodyToMono(String.class).block(Duration.ofSeconds(15));
            WxBaseResponse response = JSON.parseObject(str, WxBaseResponse.class);
            if (response == null) {
                log.warn("[微信公众号]发送模板信息失败, 无法连接到服务器，响应null");
                return;
            }
            if (!"0".equals(response.getErrcode())) {
                log.warn("[微信公众号]发送模板信息失败. Error code: {}, error msg: {}", response.getErrcode(), response.getErrmsg());
            }
        } catch (Exception e) {
            log.error("[微信公众号] 发送模板信息失败", e);
        }
    }

    public String getAccessToken() {
        try {
            String str = commonWebClient.get().uri(uriBuilder -> uriBuilder.scheme("https")
                    .host(WX_SERVER_HOST)
                    .path("/cgi-bin/token")
                    .queryParam("appid", appProperty.getWxAppId())
                    .queryParam("secret", appProperty.getWxSecret())
                    .queryParam("grant_type", "client_credential")
                    .build())
                    .retrieve().bodyToMono(String.class).block(Duration.ofSeconds(15));
            WxAccessTokenResponse response = JSON.parseObject(str, WxAccessTokenResponse.class);
            if (response == null) return null;
            if (response.getErrcode() != null) {
                log.error("获取access token失败. Error code: {}, error msg: {}", response.getErrcode(), response.getErrmsg());
                return null;
            }
            return response.getAccess_token();
        } catch (Exception e) {
            log.error("[微信公众号] 获取access token失败", e);
            return null;
        }
    }

    public String getBaiduToken() {
        try {
            BaiduTokenResponse response = commonWebClient.post().uri(uriBuilder -> uriBuilder.scheme("https")
                    .host(BAIDU_SERVER_HOST)
                    .path("/oauth/2.0/token")
                    .queryParam("client_id", appProperty.getBaiduApiKey())
                    .queryParam("client_secret", appProperty.getBaiduApiSecret())
                    .queryParam("grant_type", "client_credentials")
                    .build())
                    .retrieve().bodyToMono(BaiduTokenResponse.class).block(Duration.ofSeconds(15));
            if (response == null) return null;
            if (response.error != null) {
                log.error("获取baidu access token失败. Error : {}, error msg: {}", response.error, response.error_description);
                return null;
            }
            return response.getAccess_token();
        } catch (Exception e) {
            log.error("[百度] 获取access token失败", e);
            return null;
        }
    }

    @Data
    public static class BaiduTokenResponse {
        private String error;
        private String error_description;
        private String refresh_token;
        private long expires_in;
        private String session_key;
        private String access_token;
        private String session_secret;
    }
}
