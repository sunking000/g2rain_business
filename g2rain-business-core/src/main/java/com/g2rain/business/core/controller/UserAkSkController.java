package com.g2rain.business.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.g2rain.business.common.result.BaseResult;
import com.g2rain.business.common.result.SpecificResult;
import com.g2rain.business.core.bo.UserAkSkBo;
import com.g2rain.business.core.vo.UserVo;

/**
 * @author Administrator
 * @version V1.0
 * @Description:
 * @Title: UserAkSkController
 * @date 2018\11\18 0018 2018
 */
@Controller
@RequestMapping("aksk")
public class UserAkSkController {

    @Autowired
    private UserAkSkBo userAkSkBo;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, path = "/{userId}")
	public SpecificResult<Map<String, String>> get(@PathVariable String userId) {
		Map<String, String> aksk = userAkSkBo.get(userId);
		SpecificResult<Map<String, String>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(aksk);
		return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public BaseResult reset(@RequestBody UserVo userVo) {
		Map<String, String> aksk = userAkSkBo.reset(userVo.getUserId());
		SpecificResult<Map<String, String>> result = new SpecificResult<>(BaseResult.SUCCESS);
		result.setResultData(aksk);
		return result;
    }
}
