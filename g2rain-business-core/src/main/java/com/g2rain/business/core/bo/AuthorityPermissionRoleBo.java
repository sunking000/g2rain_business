package com.g2rain.business.core.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.g2rain.business.common.enums.PermissionTypeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.utils.BeanTransferUtils;
import com.g2rain.business.core.mapper.AuthorityPermissionRoleMapper;
import com.g2rain.business.core.po.AuthorityPermissionRolePo;
import com.g2rain.business.core.po.AuthorityPermissionRoleSelectParam;
import com.g2rain.business.core.utils.ErrorCode;
import com.g2rain.business.core.vo.AuthorityPermissionRoleBatchParam;
import com.g2rain.business.core.vo.AuthorityPermissionRoleVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorityPermissionRoleBo {

	@Autowired
	private AuthorityPermissionRoleMapper authorityPermissionRoleMapper;

	public AuthorityPermissionRoleVo addOrEdit(AuthorityPermissionRoleVo vo) {
		if (PermissionTypeEnum.ROLE.name().equals(vo.getPermissionType())
				&& vo.getRoleId().equals(vo.getPermissionId())) {
			log.error("角色不能包含当前角色, AuthorityPermissionRole:{}", JSONObject.toJSONString(vo));
			throw new BussinessRuntimeException(ErrorCode.ROLE_CANNOT_CONTAIN_SELF.name());
		}

		AuthorityPermissionRolePo curAuthorityPermissionRolePo = authorityPermissionRoleMapper.get(vo.getRoleId(),
				vo.getPermissionId());
		AuthorityPermissionRolePo lastestAuthorityPermissionRolePo = null;
		if (curAuthorityPermissionRolePo == null) {
			authorityPermissionRoleMapper.insert(new AuthorityPermissionRolePo(vo));
			lastestAuthorityPermissionRolePo = authorityPermissionRoleMapper
					.get(vo.getRoleId(), vo.getPermissionId());
		} else {
			lastestAuthorityPermissionRolePo = curAuthorityPermissionRolePo;
		}

		return new AuthorityPermissionRoleVo(lastestAuthorityPermissionRolePo);
	}

	public List<AuthorityPermissionRoleVo> listAuthorityPermissionRoles(AuthorityPermissionRoleSelectParam param) {
		List<AuthorityPermissionRolePo> poList = authorityPermissionRoleMapper.selectByParam(param);
		List<AuthorityPermissionRoleVo> voList = new ArrayList<AuthorityPermissionRoleVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (AuthorityPermissionRolePo item : poList) {
				AuthorityPermissionRoleVo vo = new AuthorityPermissionRoleVo(item);
				voList.add(vo);
			}
		}

		return voList;
	}

	public SpecificPageInfoResult<AuthorityPermissionRoleVo> list(AuthorityPermissionRoleSelectParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<AuthorityPermissionRolePo> poList = authorityPermissionRoleMapper.selectByParam(param);
		PageInfo<AuthorityPermissionRolePo> page = new PageInfo<AuthorityPermissionRolePo>(poList);
		List<AuthorityPermissionRoleVo> voList = new ArrayList<AuthorityPermissionRoleVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (AuthorityPermissionRolePo item : poList) {
				AuthorityPermissionRoleVo vo = new AuthorityPermissionRoleVo(item);
				voList.add(vo);
			}
		}

		SpecificPageInfoResult<AuthorityPermissionRoleVo> specificPageInfoResult = new SpecificPageInfoResult<>(
				page.getPageNum(), param.getSize(), page.getTotal(), page.getPages());
		specificPageInfoResult.setObjects(voList);

		return specificPageInfoResult;
	}

	public List<AuthorityPermissionRolePo> listByParam(String roleId, String permissionType){
		AuthorityPermissionRoleSelectParam param = new AuthorityPermissionRoleSelectParam();
		if(StringUtils.isNotBlank(roleId)) {
			param.setRoleId(roleId);
		}
		if(StringUtils.isNotBlank(permissionType)) {
			param.setPermissionType(permissionType);
		}
		return authorityPermissionRoleMapper.selectByParam(param);
	}

	public List<AuthorityPermissionRolePo> listByRoleIdList(List<String> roleIds, String permissionType) {
		AuthorityPermissionRoleSelectParam param = new AuthorityPermissionRoleSelectParam();
		if (CollectionUtils.isNotEmpty(roleIds)) {
			param.setRoleIds(roleIds);
		}
		if (StringUtils.isNotBlank(permissionType)) {
			param.setPermissionType(permissionType);
		}
		return authorityPermissionRoleMapper.selectByParam(param);
	}

	@Transactional
	public Map<String, Integer> batchAddOrUpdate(AuthorityPermissionRoleBatchParam authorityPermissionRoleBatchVo) {
		Set<String> permissionIds = authorityPermissionRoleBatchVo.getPermissionIds();
		if (PermissionTypeEnum.ROLE.name().equals(authorityPermissionRoleBatchVo.getPermissionType())
				&& CollectionUtils.isNotEmpty(permissionIds) 
				&& permissionIds.contains(authorityPermissionRoleBatchVo.getRoleId())) {
			log.error("角色不能包含当前角色, AuthorityPermissionRole:{}",
					JSONObject.toJSONString(authorityPermissionRoleBatchVo));
			throw new BussinessRuntimeException(ErrorCode.ROLE_CANNOT_CONTAIN_SELF.name());
		}

		List<AuthorityPermissionRolePo> permissionRoles = new ArrayList<>();
		AuthorityPermissionRoleVo vo = null;
		for(String permissionId : permissionIds){
			vo = BeanTransferUtils.beanTransfer(authorityPermissionRoleBatchVo, AuthorityPermissionRoleVo.class);
			vo.setPermissionId(permissionId);
			AuthorityPermissionRolePo po = new AuthorityPermissionRolePo(vo);
			log.info("po:{}", JSONObject.toJSONString(po));
			permissionRoles.add(po);
		}

		Map<String, Integer> result = new HashMap<String, Integer>();
		if (CollectionUtils.isNotEmpty(permissionRoles)) {
			int insertRowCount = authorityPermissionRoleMapper.insertBatch(permissionRoles);
			result.put("insertRowCount", insertRowCount);
		}

		if (CollectionUtils.isNotEmpty(authorityPermissionRoleBatchVo.getDeletePermissionIds())) {
			int deleteRowCount = authorityPermissionRoleMapper.deleteBatch(authorityPermissionRoleBatchVo.getRoleId(),
					authorityPermissionRoleBatchVo.getDeletePermissionIds());
			result.put("deleteRowCount", deleteRowCount);

		}

		return result;
	}
}