package com.g2rain.business.core.bo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.enums.PermissionTypeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.core.mapper.AuthorityRoleMapper;
import com.g2rain.business.core.po.AuthorityPermissionRoleSelectParam;
import com.g2rain.business.core.po.AuthorityRolePo;
import com.g2rain.business.core.po.AuthorityRoleSelectParam;
import com.g2rain.business.core.utils.ErrorCode;
import com.g2rain.business.core.vo.AuthorityPermissionRoleVo;
import com.g2rain.business.core.vo.AuthorityRoleVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorityRoleBo {

	@Autowired
	private SequenceBo sequenceBo;
	@Autowired
	private AuthorityRoleMapper authorityRoleMapper;
	@Autowired
	private AuthorityPermissionRoleBo authorityPermissionRoleBo;

	public AuthorityRoleVo getRole(String roleId) {
		AuthorityRolePo authorityRolePo = authorityRoleMapper.getRoleId(roleId);
		return new AuthorityRoleVo(authorityRolePo);
	}

	public List<AuthorityRoleVo> selectByParam(AuthorityRoleSelectParam param) {
		List<AuthorityRolePo> poList = authorityRoleMapper.selectByParam(param);
		List<AuthorityRoleVo> voList = new ArrayList<AuthorityRoleVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (AuthorityRolePo item : poList) {
				AuthorityRoleVo vo = new AuthorityRoleVo(item);
				voList.add(vo);
			}
		}
		return voList;
	}

	public AuthorityRoleVo add(AuthorityRoleVo vo) {

		vo.setRoleId(sequenceBo.getAuthorityRoleId());
		if (vo.getCode() == null) {
			vo.setCode(vo.getRoleId());
		} else {
			AuthorityRolePo authorityRolePo = authorityRoleMapper.getByCode(vo.getCode());
			if (authorityRolePo != null) {
				log.error("添加用户角色,code已存在, param:{}", JSONObject.toJSONString(vo));
				throw new BussinessRuntimeException(ErrorCode.ROLE_CODE_EXIST.name());
			}
		}
		AuthorityRolePo po = new AuthorityRolePo(vo);

		authorityRoleMapper.insert(po);
		AuthorityRolePo resultPo = authorityRoleMapper.getRoleId(po.getRoleId());
		return new AuthorityRoleVo(resultPo);
	}

	public SpecificPageInfoResult<AuthorityRoleVo> list(AuthorityRoleSelectParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<AuthorityRolePo> poList = authorityRoleMapper.selectByParam(param);
		PageInfo<AuthorityRolePo> page = new PageInfo<AuthorityRolePo>(poList);
		List<AuthorityRoleVo> voList = new ArrayList<AuthorityRoleVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (AuthorityRolePo item : poList) {
				AuthorityRoleVo vo = new AuthorityRoleVo(item);
				voList.add(vo);
			}
		}
		
		SpecificPageInfoResult<AuthorityRoleVo> result = new SpecificPageInfoResult<>(page.getPageNum(), param.getSize(), page.getTotal(),
				page.getPages());
		result.setObjects(voList);

		// BaseResult success = new BaseResult(BaseResult.SUCCESS);
		// success.putData("authorityRoles", voList);
		// PageInfoResult pageInfo = new PageInfoResult(page.getPageNum(),
		// param.getSize(), page.getTotal(),
		// page.getPages());
		// success.putData("pageInfo", pageInfo);

		return result;
	}

	public int update(AuthorityRoleVo authorityRoleParam) {
		AuthorityRolePo authorityRolePo = authorityRoleMapper.getRoleId(authorityRoleParam.getRoleId());
		if(authorityRolePo == null){
			throw new BussinessRuntimeException(ErrorCodeEnum.DATA_NON_EXISTENT);
		}
		if (StringUtils.isNotBlank(authorityRoleParam.getCode())) {
			authorityRolePo.setCode(authorityRoleParam.getCode());
			AuthorityRolePo authorityRolePoByCode = authorityRoleMapper.getByCode(authorityRoleParam.getCode());
			if (authorityRolePo != null && !authorityRolePoByCode.getRoleId().equals(authorityRoleParam.getRoleId())) {
				log.error("添加用户角色,code已存在, param:{}", JSONObject.toJSONString(authorityRoleParam));
				throw new BussinessRuntimeException(ErrorCode.ROLE_CODE_EXIST.name());
			}
		}
		if (StringUtils.isNotBlank(authorityRoleParam.getName())) {
			authorityRolePo.setName(authorityRoleParam.getName());
		}
		if (StringUtils.isNotBlank(authorityRoleParam.getOrganId())) {
			authorityRolePo.setOrganId(authorityRoleParam.getOrganId());
		}
		if (authorityRoleParam.getType() != null) {
			authorityRolePo.setType(authorityRoleParam.getType().name());
		}

		return authorityRoleMapper.update(authorityRolePo);
	}

	public int delete(String roleId) {
		return authorityRoleMapper.delete(roleId);
	}

	public List<AuthorityRoleVo> listByRoleId(String roleId) {
		AuthorityRolePo authorityRolePo = authorityRoleMapper.getRoleId(roleId);
		Set<String> roleIdParam = new HashSet<>();
		roleIdParam.add(roleId);
		Set<String> roleIds = listRoleByRoleId(roleIdParam,
				authorityRolePo.getOrganId());
		roleIds.removeAll(roleIdParam);
		List<AuthorityRoleVo> roleVos = new ArrayList<>();
		if (CollectionUtils.isEmpty(roleIds)) {
			return roleVos;
		}
		AuthorityRoleSelectParam param = new AuthorityRoleSelectParam();
		param.setRoleIds(new ArrayList<>(roleIds));
		List<AuthorityRolePo> authorityRolePos = authorityRoleMapper.selectByParam(param);
		if (CollectionUtils.isNotEmpty(authorityRolePos)) {
			for (AuthorityRolePo item : authorityRolePos) {
				roleVos.add(new AuthorityRoleVo(item));
			}
		}

		return roleVos;
	}

	public Set<String> listRoleByRoleId(Set<String> roleIds, String organId) {
		Set<String> roleIdList = new HashSet<>();
		roleIdList.addAll(roleIds);
		AuthorityPermissionRoleSelectParam authorityPermissionRoleSelectParam = new AuthorityPermissionRoleSelectParam();
		authorityPermissionRoleSelectParam.setOrganId(organId);
		authorityPermissionRoleSelectParam.setPermissionType(PermissionTypeEnum.ROLE.name());
		authorityPermissionRoleSelectParam.setRoleIds(new ArrayList<String>(roleIds));
		List<AuthorityPermissionRoleVo> authorityPermissionRoleVos = authorityPermissionRoleBo
				.listAuthorityPermissionRoles(authorityPermissionRoleSelectParam);

		if (CollectionUtils.isNotEmpty(authorityPermissionRoleVos)) {
			Set<String> resultRoleIds = new HashSet<>();
			for (AuthorityPermissionRoleVo item : authorityPermissionRoleVos) {
				if (!roleIdList.contains(item.getPermissionId())) {
					roleIdList.add(item.getPermissionId());
					resultRoleIds.add(item.getPermissionId());
				}
			}
			roleIdList.addAll(listRoleByRoleId(resultRoleIds, organId));
		}

		return roleIdList;
	}
}