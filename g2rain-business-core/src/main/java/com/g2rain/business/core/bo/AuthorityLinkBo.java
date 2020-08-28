package com.g2rain.business.core.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2rain.business.common.asserts.Asserts;
import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.core.mapper.AuthorityLinkMapper;
import com.g2rain.business.core.po.AuthorityLinkPo;
import com.g2rain.business.core.po.AuthorityLinkSelectParam;
import com.g2rain.business.core.po.AuthorityRoleLinkSelectParam;
import com.g2rain.business.core.po.AuthorityRolePo;
import com.g2rain.business.core.utils.AuthorityLinkType;
import com.g2rain.business.core.vo.AuthorityLinkVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorityLinkBo {

	@Autowired
	private SequenceBo sequenceBo;
	@Autowired
	private AuthorityLinkMapper authorityLinkMapper;
	@Autowired
	private AuthorityRoleUserBo authorityRoleUserBo;

	public AuthorityLinkVo add(AuthorityLinkVo vo) {
		vo.setLinkId(sequenceBo.getAuthorityLinkId());
		AuthorityLinkPo po = new AuthorityLinkPo(vo);

		authorityLinkMapper.insert(po);
		AuthorityLinkPo resultPo = authorityLinkMapper.getByLinkId(vo.getLinkId());

		return new AuthorityLinkVo(resultPo);
	}

	public List<AuthorityLinkVo> list(AuthorityRoleLinkSelectParam param) {
		List<AuthorityLinkPo> poList = authorityLinkMapper.selectByAuthorityRoleLinkParam(param);
		List<AuthorityLinkVo> voList = new ArrayList<AuthorityLinkVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (AuthorityLinkPo item : poList) {
				AuthorityLinkVo vo = new AuthorityLinkVo(item);
				voList.add(vo);
			}
		}

		return voList;
	}

	public SpecificPageInfoResult<AuthorityLinkVo> list(AuthorityLinkSelectParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<AuthorityLinkPo> poList = authorityLinkMapper.selectByParam(param);
		PageInfo<AuthorityLinkPo> page = new PageInfo<AuthorityLinkPo>(poList);
		List<AuthorityLinkVo> voList = transfer(poList);

		SpecificPageInfoResult<AuthorityLinkVo> specificPageInfoResult = new SpecificPageInfoResult<>(
				page.getPageNum(), param.getSize(), page.getTotal(), page.getPages());
		specificPageInfoResult.setObjects(voList);

		return specificPageInfoResult;
	}

	public AuthorityLinkPo getByLinkId(String linkId){
		AuthorityLinkPo authorityLinkPo = authorityLinkMapper.getByLinkId(linkId);
		return authorityLinkPo;
	}

	public int update(AuthorityLinkVo authorityLink) {
		Asserts.assertNull(authorityLink, ErrorCodeEnum.PARAMETER_ERROR.name());
		AuthorityLinkPo authorityLinkPo = getByLinkId(authorityLink.getLinkId());
		if(authorityLinkPo == null){
			log.error("对象不存在,linkId:{}",authorityLink.getLinkId());
			throw new BussinessRuntimeException(ErrorCodeEnum.PARAMETER_ERROR);
		}
		if(!StringUtils.isEmpty(authorityLink.getName())){
			authorityLinkPo.setName(authorityLink.getName());
		}
		if (!StringUtils.isEmpty(authorityLink.getLinkPath())) {
			authorityLinkPo.setLinkPath(authorityLink.getLinkPath());
		}
		if(!StringUtils.isEmpty(authorityLink.getType())){
			authorityLinkPo.setType(authorityLink.getType());
		}
		if(!StringUtils.isEmpty(authorityLink.getMethod())){
			authorityLinkPo.setMethod(authorityLink.getMethod());
		}
		if(authorityLink.getParentLinkId() != null){
			authorityLinkPo.setParentLinkId(authorityLink.getParentLinkId());
		}
		authorityLinkPo.setSort(authorityLink.getSort());
		authorityLinkPo.setIcon(authorityLink.getIcon());
		int updateRowCount = authorityLinkMapper.update(authorityLinkPo);
		return updateRowCount;
		// result = new BaseResult(BaseResult.SUCCESS);
		// return result;
	}

	public List<AuthorityLinkVo> listByUserId(String userId, String linkType) {
		Map<String,AuthorityLinkVo> map = new HashMap<>();

		List<AuthorityRolePo> authorityRolePos = authorityRoleUserBo.listRoleByUserId(userId);
		List<AuthorityLinkVo> authorityLinkVos = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(authorityRolePos)) {
			for (AuthorityRolePo authorityRolePo : authorityRolePos) {
				authorityLinkVos.addAll(getListByRoleId(authorityRolePo.getRoleId(), linkType));
			}
		}
		for(AuthorityLinkVo vo : authorityLinkVos){
			map.put(vo.getLinkId(),vo);
		}
		List<AuthorityLinkVo> result = new ArrayList<>();
		for(Map.Entry<String,AuthorityLinkVo> entry : map.entrySet()){
			result.add(entry.getValue());
		}
		return result;
	}

	public List<AuthorityLinkVo> getListByRoleId(String roleId, String linkType) {
		AuthorityRoleLinkSelectParam param = new AuthorityRoleLinkSelectParam();
		param.setPageSize(Integer.MAX_VALUE);
		param.setType(linkType);
		param.setRoleId(roleId);
		Set<AuthorityLinkVo> set = new HashSet<>();
		List<AuthorityLinkPo> selectByAuthorityRoleLinkList = authorityLinkMapper.selectByAuthorityRoleLinkParam(param);
		if (CollectionUtils.isNotEmpty(selectByAuthorityRoleLinkList)) {
			selectByAuthorityRoleLinkList.forEach(item -> {
				set.add(new AuthorityLinkVo(item));
			});
		}

		return new ArrayList<>(set);
	}

	public List<AuthorityLinkVo> listBy(AuthorityRoleLinkSelectParam param) {
		List<AuthorityLinkPo> selectByAuthorityRoleLinkList = authorityLinkMapper.selectByAuthorityRoleLinkParam(param);
		return transfer(selectByAuthorityRoleLinkList);
	}

	public List<AuthorityLinkVo> listAll(AuthorityLinkType type) {
		AuthorityLinkSelectParam param = new AuthorityLinkSelectParam();
		param.setType(type == null ? null : type.name());
		List<AuthorityLinkPo> authorityLinkPos = authorityLinkMapper.selectByParam(param);
		return transfer(authorityLinkPos);
	}

	private List<AuthorityLinkVo> transfer(List<AuthorityLinkPo> poList) {
		if (CollectionUtils.isEmpty(poList)) {
			return null;
		}

		Map<String, AuthorityLinkVo> parentIdZeroMap = new HashMap<String, AuthorityLinkVo>();
		Map<String, Set<AuthorityLinkVo>> parentIdNotZeroMap = new HashMap<String, Set<AuthorityLinkVo>>();
		poList.forEach(item -> {
			AuthorityLinkVo itemVo = new AuthorityLinkVo(item);
			if (org.apache.commons.lang.StringUtils.isNotBlank(itemVo.getParentLinkId())
					&& !itemVo.getParentLinkId().equals("0")) {
				Set<AuthorityLinkVo> set = parentIdNotZeroMap.get(item.getParentLinkId());
				if (set == null) {
					set = new HashSet<>();
					parentIdNotZeroMap.put(item.getParentLinkId(), set);
				}
				set.add(itemVo);
			} else {
				parentIdZeroMap.put(item.getLinkId(), itemVo);
			}
		});

		if (MapUtils.isNotEmpty(parentIdNotZeroMap)) {
			for (String parentId : parentIdNotZeroMap.keySet()) {
				AuthorityLinkVo authorityLinkVo = parentIdZeroMap.get(parentId);
				if (authorityLinkVo != null) {
					authorityLinkVo.setSubMenuSet(parentIdNotZeroMap.get(parentId));
				}
			}
		}

		return new ArrayList<>(parentIdZeroMap.values());
	}

	public List<AuthorityLinkVo> listByRoleId(String roleId, String linkType) {
		return getListByRoleId(roleId, linkType);
	}

	public int deleteLink(String linkId) {
		return authorityLinkMapper.delete(linkId);
	}
}
