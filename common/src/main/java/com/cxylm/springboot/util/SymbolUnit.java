package com.cxylm.springboot.util;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取释义
 */
public class SymbolUnit {
    public static Map<String, String> getSymbol(String word) {
        try {
            word = URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(word.contains("+")){
            return null;
        }
        if(word.contains("(") || word.contains("（")){
            word = word.substring(word.indexOf("("));
            word = word.substring(word.indexOf("（"));
        }
        Map map = new HashMap(4);
        String s = HttpUtil.doGet("http://open.iciba.com/huaci_new/dict.php?word=" + word);
        int i = s.indexOf("xml:lang=\\\"EN-US\\\">") + 19;
        int i1 = s.indexOf("</strong>", i);
        String substring = s.substring(i, i1).trim();
        if (!StringUtils.isEmpty(substring)) {
            if(substring.length() > 100){
                substring = "";
            }
            map.put("symbol_b", substring);
        }
        int j = s.indexOf("xml:lang=\\\"EN-US\\\">", i1) + 19;
        int j1 = s.indexOf("</strong>", j);
        String substring2 = s.substring(j, j1).trim();
        if (!StringUtils.isEmpty(substring2)) {
            if(substring2.length() > 100){
                substring2 = "";
            }
            map.put("symbol_a", substring2);
        }
        if (map.size() == 0) {
            return null;
        }
        return map;
    }
}
