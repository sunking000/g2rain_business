package com.g2rain.business.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @version V1.0
 * @Description:
 * @Title: StringToMapUtil
 * @date 2018\9\9 0009 2018
 */
@Slf4j
public class StringToMapUtil {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> strToMap(String str) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            if(!StringUtils.isEmpty(str)) {
                Gson gson = new Gson();
                map = gson.fromJson(str, map.getClass());
            }
        }catch (Exception e){
            log.error("str to map exception",e);
        }
        return map;
    }

}
