package com.g2rain.business.core.bo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.common.utils.RandomBitStringUtil;
import com.g2rain.business.core.vo.UserVo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @version V1.0
 * @Description:
 * @Title: UserAkSkBo
 * @Package com.sunhaojie.pos.core.bo
 * @date 2018\11\18 0018 2018
 */
@Slf4j
@Service
public class UserAkSkBo {

    @Autowired
    private UserBo userBo;

	public Map<String, String> get(String userId) {
        UserVo userVo = userBo.getUser(userId);
        if(userVo == null){
			log.error("用户不存在,userId:{}", userId);
			throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
        }
		Map<String, String> map = new HashMap<String, String>();
        map.put("ak",userVo.getAccessKey());
        map.put("sk",userVo.getSecretAccessKey());
		return map;
    }

	public Map<String, String> reset(String userId) {
        UserVo userVo = userBo.getUser(userId);
        if(userVo == null){
			log.error("用户不存在,userId:{}", userId);
			throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
        }
        userVo.setAccessKey(RandomBitStringUtil.generateLongUuid());
        userVo.setSecretAccessKey(RandomBitStringUtil.generateMiddleUuid());
        userBo.updateAkSk(userVo);

		Map<String, String> map = new HashMap<String, String>();
        map.put("ak",userVo.getAccessKey());
        map.put("sk",userVo.getSecretAccessKey());
		return map;
    }
}
