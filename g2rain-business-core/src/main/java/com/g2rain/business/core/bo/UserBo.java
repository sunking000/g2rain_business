package com.g2rain.business.core.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.g2rain.business.common.asserts.Asserts;
import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.utils.BeanTransferUtils;
import com.g2rain.business.common.utils.MD5Util;
import com.g2rain.business.core.mapper.OrganMapper;
import com.g2rain.business.core.mapper.UserMapper;
import com.g2rain.business.core.po.OrganPo;
import com.g2rain.business.core.po.UserPo;
import com.g2rain.business.core.po.UserQueryParam;
import com.g2rain.business.core.po.UserSelectParam;
import com.g2rain.business.core.utils.ErrorCode;
import com.g2rain.business.core.vo.AddAuthorityRoleUserParam;
import com.g2rain.business.core.vo.AddUserParam;
import com.g2rain.business.core.vo.OrganVo;
import com.g2rain.business.core.vo.UserUpdatePwdParam;
import com.g2rain.business.core.vo.UserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserBo {
	private static Logger logger = LoggerFactory.getLogger(UserBo.class);

	@Autowired
	private SequenceBo sequenceBo;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private OrganMapper organMapper;
	@Autowired
	private AuthorityRoleUserBo authorityRoleUserBo;

	/**
	 * 根据门店查询用户id
	 * 
	 * @return
	 */
	public List<UserVo> listByOrganId(String organId) {
		UserSelectParam param = new UserSelectParam();
		param.setOrganId(organId);
		List<UserPo> userPoList = userMapper.selectByParam(param);
		List<UserVo> users = new ArrayList<UserVo>();
		if (CollectionUtils.isNotEmpty(userPoList)) {
			for (UserPo item : userPoList) {
				UserVo user = new UserVo(item);

				if (StringUtils.isNotBlank(user.getOrganId())) {
					OrganPo organPo = organMapper.get(user.getOrganId());
					OrganVo storeOrgan = new OrganVo(organPo);
					user.setStoreOrgan(storeOrgan);
				}

				users.add(user);
			}
		}

		return users;
	}

	public SpecificPageInfoResult<UserVo> list(UserSelectParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<UserPo> userPoList = userMapper.selectByParam(param);
		PageInfo<UserPo> page = new PageInfo<UserPo>(userPoList);

		List<UserVo> users = new ArrayList<UserVo>();
		if (CollectionUtils.isNotEmpty(userPoList)) {
			for (UserPo item : userPoList) {
				UserVo user = new UserVo(item);
				
				if (StringUtils.isNotBlank(user.getOrganId())) {
					OrganPo organPo = organMapper.get(user.getOrganId());
					OrganVo storeOrgan = new OrganVo(organPo);
					user.setStoreOrgan(storeOrgan);
				}
				
				users.add(user);
			}
		}

		SpecificPageInfoResult<UserVo> specificPageInfoResult = new SpecificPageInfoResult<>(
				page.getPageNum(), param.getSize(), page.getTotal(), page.getPages());
		specificPageInfoResult.setObjects(users);

		return specificPageInfoResult;
	}

	@Transactional
	public int add(AddUserParam user) {
		Asserts.assertBlank(user.getMobile(), ErrorCodeEnum.PARAMETER_ERROR.name(), "mobile");
		UserSelectParam userSelectParam = new UserSelectParam();
		userSelectParam.setUsername(user.getUsername());
		// userSelectParam.setMobile(user.getMobile());
		if(userMapper.countByParam(userSelectParam) > 0 ) {
			log.error("用户已存在, user:{}", JSONObject.toJSONString(user));
			throw new BussinessRuntimeException(ErrorCode.USER_EXIST.name());
		}

		user.setUserId(sequenceBo.getUserId());
		if(user.getPassword() == null) {
			// 不填写密码时密码为手机号的后六位
			user.setPassword(MD5Util.md5(user.getMobile().substring(user.getMobile().length() - 6)));
		}
		user.setPassword(MD5Util.md5(user.getPassword()));
		UserPo userPo = user.toUserPo();
		int insertRowCount = userMapper.insert(userPo);
		if (CollectionUtils.isNotEmpty(user.getRoleIds())) {
			AddAuthorityRoleUserParam param = new AddAuthorityRoleUserParam();
			param.setOrganId(userPo.getOrganId());
			param.setUserId(userPo.getUserId());
			param.setRoleIds(user.getRoleIds());
			insertRowCount += authorityRoleUserBo.add(param);
		}

		return insertRowCount;
	}

	public UserVo get(String userId) {
		UserVo user = getUser(userId);
		return user;
	}

	public UserVo getUser(String userId) {
		UserPo userPo = userMapper.get(userId);
		if(userPo == null){
			logger.info("user不存在,userId:"+userId);
			return null;
		}
		logger.info("user:{}", JSONObject.toJSONString(userPo));
		UserVo user = new UserVo(userPo);
		
		if (StringUtils.isNotBlank(user.getOrganId())) {
			OrganPo organPo = organMapper.get(user.getOrganId());
			OrganVo storeOrgan = new OrganVo(organPo);
			user.setStoreOrgan(storeOrgan);
		}
		
		return user;
	}

	public int update(UserVo user) {
		UserPo dbUserPo = userMapper.get(user.getUserId());
		UserPo userPo = new UserPo(user);
		userPo.setVersion(dbUserPo.getVersion());
		
		if(user.getPassword() != null) {
			userPo.setPassword(MD5Util.md5(user.getPassword()));
		}
		
		int updateRowCount = userMapper.update(userPo);
		//
		// BaseResult success = new BaseResult(BaseResult.SUCCESS);
		// UserPo lastestUser = userMapper.get(user.getUserId());
		// UserVo resultUser = new UserVo(lastestUser);
		// success.putData("user", resultUser);
		
		return updateRowCount;
	}

	public void updateAkSk(UserVo userVo) {
		UserPo userPo = userMapper.get(userVo.getUserId());
		userPo.setAccessKey(userVo.getAccessKey());
		userPo.setSecretAccessKey(userVo.getSecretAccessKey());
		userMapper.updateAkSk(userPo);
	}

	public List<UserVo> getByQueryParam(UserQueryParam userQueryParam) {
		logger.info("获取用户参数,"+userQueryParam.toString());
		List<UserPo> userPos = userMapper.getByQueryParam(userQueryParam);
		List<UserVo> userVos = new ArrayList<>();
		if(CollectionUtils.isEmpty(userPos)){
			return userVos;
		}
		userVos = BeanTransferUtils.batchBeanTransfer(userPos,UserVo.class);
		return userVos;
	}

	public UserVo getByMobile(UserQueryParam userQueryParam){
		List<UserVo> userVos = getByQueryParam(userQueryParam);
		if(CollectionUtils.isEmpty(userVos)){
			return null;
		}
		return userVos.get(0);
	}

	public int updatePwd(UserUpdatePwdParam userUpdatePwdParam) {
		if(userUpdatePwdParam == null){
			logger.error("参数为空");
			throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
		}
		if(StringUtils.isBlank(userUpdatePwdParam.getUserId())){
			logger.error("参数userId为空");
			throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
		}

		if (StringUtils.isBlank(userUpdatePwdParam.getPassword())) {
			logger.error("密码不存在");
			throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
		}
		UserPo userPo = userMapper.get(userUpdatePwdParam.getUserId());
		if(userPo == null){
			logger.error("用户不存在");
			throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
		}
		String passwordMd5 = MD5Util.md5(userUpdatePwdParam.getPassword());
		userPo.setPassword(passwordMd5);
		int updateRowCount = userMapper.update(userPo);
		return updateRowCount;
	}

	public int delete(String userId) {
		int deleteRowCount = userMapper.delete(userId);
		if (deleteRowCount != 1) {
			log.error("用户删除失败,用户id:{}", userId);
			throw new BussinessRuntimeException(ErrorCodeEnum.DATA_SOURCE_ERROR.name());
		}
		return deleteRowCount;
	}
}