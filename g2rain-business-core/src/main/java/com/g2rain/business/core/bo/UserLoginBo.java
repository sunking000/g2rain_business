package com.g2rain.business.core.bo;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.g2rain.business.common.asserts.Asserts;
import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.enums.OrganTypeEnum;
import com.g2rain.business.common.enums.SessionTypeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.common.utils.MD5Util;
import com.g2rain.business.common.utils.RandomBitStringUtil;
import com.g2rain.business.core.mapper.OrganMapper;
import com.g2rain.business.core.mapper.UserMapper;
import com.g2rain.business.core.po.OrganPo;
import com.g2rain.business.core.po.UserPo;
import com.g2rain.business.core.po.UserSelectParam;
import com.g2rain.business.core.utils.ErrorCode;
import com.g2rain.business.core.utils.OrganStatusEnum;
import com.g2rain.business.core.vo.LoginToken;
import com.g2rain.business.core.vo.UserLoginParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserLoginBo {
	@Autowired
	private LoginTokenStorageBo loginTokenStorageBo;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private OrganMapper organMapper;
	// @Autowired
	// private AuthorityBo authorityBo;

	public LoginToken getLoginToken(String accessKey) {
		if (StringUtils.isBlank(accessKey)) {
			log.error("accessKey:{}为空", accessKey);
			throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
		}
		Asserts.assertBlank(accessKey, ErrorCodeEnum.PARAMETER_ERROR.name(), "accessKey");
		LoginToken loginToken = loginTokenStorageBo.getLoginToken(accessKey);
		if (loginToken == null) {
			UserPo userPo = userMapper.getByAccessKey(accessKey);
			if (userPo == null) {
				log.error("accessKey:{}未找到", accessKey);
				throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
			}
			loginToken = generateLoginToken(userPo);
		}

		if (loginToken == null) {
			log.error("accessKey:{}未找到", accessKey);
			throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
		}

		return loginToken;
	}

	public LoginToken login(UserLoginParam param) {
		UserSelectParam userSelectParam = new UserSelectParam();
		if (StringUtils.isNotBlank(param.getPassword()) && StringUtils.isNotBlank(param.getUsername())) {
			userSelectParam.setUsername(param.getUsername());
			String passwordMd5 = MD5Util.md5(param.getPassword());
			userSelectParam.setPassword(passwordMd5);
		} else if (StringUtils.isNotBlank(param.getAccessKey())) {
			userSelectParam.setAccessKey(param.getAccessKey());
		} else {
			log.error("param:{}", JSONObject.toJSONString(param));
			throw new BussinessRuntimeException(ErrorCode.USER_LOGIN_PARAM_ERROR.name());
		}

		List<UserPo> userPos = userMapper.selectByParam(userSelectParam);
		if (CollectionUtils.isEmpty(userPos)) {
			throw new BussinessRuntimeException(
					ErrorCode.USER_NOT_EXIST_OR_PASSWORD_ERROR.name());
		}

		if (userPos.size() > 1) {
			log.error("用户查询到多个,userPos:{}", JSONObject.toJSONString(userPos));
			throw new BussinessRuntimeException(
					ErrorCode.USER_INFO_ERROR.name());
		}

		UserPo userPo = userPos.get(0);

		OrganPo storeOrganPo = null;
		if (StringUtils.isNotBlank(userPo.getOrganId())) {
			storeOrganPo = organMapper.get(userPo.getOrganId());
			if (storeOrganPo == null || storeOrganPo.isDeleteFlag()
					|| !storeOrganPo.getStatus().equals(OrganStatusEnum.NORMAL.name())) {
				log.error("门店状态不正常,organ:{}", JSONObject.toJSONString(storeOrganPo));
				throw new BussinessRuntimeException(ErrorCode.STORE_ORGAN_STATUS_ABNORMAL.name());
			}
		}

		LoginToken userToken = generateLoginToken(userPo);
		if (StringUtils.isBlank(userToken.getAccessKey()) && StringUtils.isBlank(userToken.getSecretAccessKey())) {
			userToken.setAccessKey(RandomBitStringUtil.generateLongUuid());
			userToken.setSecretAccessKey(RandomBitStringUtil.generateMiddleUuid());
		}

		loginTokenStorageBo.setLoginToken(userToken);

		return userToken;
	}

	private LoginToken generateLoginToken(UserPo userPo) {
		LoginToken userToken = new LoginToken();
		userToken.setUserId(userPo.getUserId());
		userToken.setName(userPo.getName());
		userToken.setAdminUser(userPo.isAdminFlag());
		if (StringUtils.isNotBlank(userPo.getOrganId())) {
			userToken.setOrganId(userPo.getOrganId());
			OrganPo organPo = organMapper.get(userPo.getOrganId());
			userToken.setOrganName(organPo.getName());
			if (OrganTypeEnum.COMPANY.name().equals(organPo.getType()) && organPo.getAdmin()) {
				userToken.setAdminCompany(organPo.getAdmin());
			}
			userToken.setOrganAddress(organPo.getAddress());
			userToken.setOrganType(organPo.getType());
		}

		userToken.setAccessKey(userPo.getAccessKey());
		userToken.setSecretAccessKey(userPo.getSecretAccessKey());
		userToken.setSessionType(SessionTypeEnum.USER.name());
		return userToken;
	}

	public void logout(String accessKey) {
		log.error("accessKey:{} 已登出", accessKey);
		loginTokenStorageBo.deleteSession(accessKey);
	}
}
