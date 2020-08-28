package com.g2rain.business.core.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.enums.PermissionTypeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.core.mapper.AuthorityDictMapper;
import com.g2rain.business.core.po.AuthorityDictPo;
import com.g2rain.business.core.po.AuthorityDictSelectParam;
import com.g2rain.business.core.po.AuthorityPermissionRolePo;
import com.g2rain.business.core.po.AuthorityRolePo;
import com.g2rain.business.core.po.param.AuthorityRoleDictSelectParam;
import com.g2rain.business.core.vo.AuthorityDictUserParam;
import com.g2rain.business.core.vo.AuthorityDictVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class AuthorityDictBo {
	@Autowired
	private SequenceBo sequenceBo;
	@Autowired
	private AuthorityDictMapper authorityDictMapper;
	@Autowired
	private AuthorityRoleUserBo authorityRoleUserBo;
	@Autowired
	private AuthorityPermissionRoleBo authorityPermissionRoleBo;

	public AuthorityDictVo add(AuthorityDictVo vo) {
		vo.setDictId(sequenceBo.getAuthorityDictId());
		
		authorityDictMapper.insert(new AuthorityDictPo(vo));
		AuthorityDictPo lastestAuthorityDictPo = authorityDictMapper.get(vo.getDictId());
		
		return new AuthorityDictVo(lastestAuthorityDictPo);
	}

	public SpecificPageInfoResult<AuthorityDictVo> list(AuthorityDictSelectParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<AuthorityDictPo> poList = authorityDictMapper.selectByParam(param);
		PageInfo<AuthorityDictPo> page = new PageInfo<AuthorityDictPo>(poList);
		List<AuthorityDictVo> voList = new ArrayList<AuthorityDictVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (AuthorityDictPo item : poList) {
				AuthorityDictVo vo = new AuthorityDictVo(item);
				voList.add(vo);
			}
		}

		SpecificPageInfoResult<AuthorityDictVo> specificPageInfoResult = new SpecificPageInfoResult<>(
				page.getPageNum(), param.getSize(), page.getTotal(), page.getPages());
		specificPageInfoResult.setObjects(voList);

		return specificPageInfoResult;
	}

	public AuthorityDictPo getByDictId(String dictId){
		return authorityDictMapper.get(dictId);
	}

	public AuthorityDictPo getByComplexParam(String dictId,String code,String pageCode){
		if(StringUtils.isEmpty(dictId)){
			return null;
		}
		AuthorityDictPo authorityDictPo = getByDictId(dictId);
		if(authorityDictPo == null){
			return null;
		}
		if(code != null && !authorityDictPo.getCode().equals(code)){
			return null;
		}
		if(pageCode != null && !authorityDictPo.getPageCode().equals(pageCode)){
			return null;
		}
		return authorityDictPo;
	}

	public int update(AuthorityDictVo vo) {

		AuthorityDictPo dictPo = getByDictId(vo.getDictId());
		if(dictPo == null){
			throw new BussinessRuntimeException(ErrorCodeEnum.DATA_NON_EXISTENT);
		}
		if(!StringUtils.isEmpty(vo.getCode())) {
			dictPo.setCode(vo.getCode());
		}
		if(!StringUtils.isEmpty(vo.getName())) {
			dictPo.setName(vo.getName());
		}
		if(!StringUtils.isEmpty(vo.getPageCode())) {
			dictPo.setPageCode(vo.getPageCode());
		}
		if(!StringUtils.isEmpty(vo.getParentDictId())) {
			dictPo.setParentDictId(vo.getParentDictId());
		}
		if(!StringUtils.isEmpty(vo.getType())) {
			dictPo.setType(vo.getType());
		}
		int updateRowCount = authorityDictMapper.update(dictPo);
		return updateRowCount;
	}

	public List<AuthorityDictVo> listAll() {
		List<AuthorityDictPo> authorityDictPoList = authorityDictMapper.selectAll();
		return transfer(authorityDictPoList);
	}

	public List<AuthorityDictVo> list(AuthorityRoleDictSelectParam param) {
		List<AuthorityDictPo> authorityDictPoList = authorityDictMapper.selectByRoleDictParam(param);

		return transfer(authorityDictPoList);
	}

	private List<AuthorityDictVo> transfer(List<AuthorityDictPo> authorityDictPoList) {
		if (CollectionUtils.isEmpty(authorityDictPoList)) {
			return null;
		}

		Map<String, AuthorityDictVo> parentIdZeroMap = new HashMap<String, AuthorityDictVo>();
		Map<String, Set<AuthorityDictVo>> parentIdNotZeroMap = new HashMap<String, Set<AuthorityDictVo>>();
		authorityDictPoList.forEach(item -> {
			AuthorityDictVo itemVo = new AuthorityDictVo(item);
			if (org.apache.commons.lang.StringUtils.isNotBlank(itemVo.getParentDictId())
					&& !itemVo.getParentDictId().equals("0")) {
				Set<AuthorityDictVo> set = parentIdNotZeroMap.get(item.getParentDictId());
				if (set == null) {
					set = new HashSet<>();
					parentIdNotZeroMap.put(item.getParentDictId(), set);
				}
				set.add(itemVo);
			} else {
				parentIdZeroMap.put(item.getDictId(), itemVo);
			}
		});

		if (MapUtils.isNotEmpty(parentIdNotZeroMap)) {
			for (String parentId : parentIdNotZeroMap.keySet()) {
				AuthorityDictVo authorityDictVo = parentIdZeroMap.get(parentId);
				if (authorityDictVo != null) {
					authorityDictVo.setChildren(parentIdNotZeroMap.get(parentId));
				}
			}
		}

		return new ArrayList<>(parentIdZeroMap.values());
	}

	public List<AuthorityDictVo> listByUserId(AuthorityDictUserParam param) {
		List<AuthorityRolePo> authorityRolePos = authorityRoleUserBo.listRoleByUserId(param.getUserId());
		List<AuthorityDictVo> authorityDictVos = new ArrayList<>();
		for (AuthorityRolePo authorityRolePo : authorityRolePos) {
			List<AuthorityPermissionRolePo> authorityPermissionRolePos = authorityPermissionRoleBo.listByParam(
					authorityRolePo.getRoleId(),PermissionTypeEnum.DICT.name());
			for(AuthorityPermissionRolePo authorityPermissionRolePo : authorityPermissionRolePos){
				if (authorityPermissionRolePo.isDeleteFlag()) {
					continue;
				}
				AuthorityDictPo authorityDictPo = getByComplexParam(authorityPermissionRolePo.getPermissionId(),param.getCode(),param.getPageCode());
				if (authorityDictPo != null && !authorityDictPo.isDeleteFlag()) {
					authorityDictVos.add(new AuthorityDictVo(authorityDictPo));
				}
			}
		}
		return authorityDictVos;
	}

	public List<AuthorityDictVo> getListByRoleId(String roleId) {
		// List<AuthorityDictVo> authorityDictVos = new ArrayList<>();
		// List<AuthorityPermissionRolePo> authorityPermissionRolePos =
		// authorityPermissionRoleBo.listByParam(roleId,PermissionTypeEnum.DICT.name());
		// for (AuthorityPermissionRolePo authorityPermissionRolePo :
		// authorityPermissionRolePos) {
		// AuthorityDictPo authorityDictPo =
		// getByDictId(authorityPermissionRolePo.getPermissionId());
		// if (authorityDictPo != null) {
		// authorityDictVos.add(new AuthorityDictVo(authorityDictPo));
		// }
		// }
		// return authorityDictVos;
		AuthorityRoleDictSelectParam param = new AuthorityRoleDictSelectParam();
		param.setPageSize(Integer.MAX_VALUE);
		param.setRoleId(roleId);
		List<AuthorityDictVo> dictResult = this.list(param);
		return dictResult;
	}

	public List<AuthorityDictVo> listByRoleId(String roleId) {
		return getListByRoleId(roleId);
	}

	public int delete(String dictId) {
		return authorityDictMapper.delete(dictId);
	}
}
