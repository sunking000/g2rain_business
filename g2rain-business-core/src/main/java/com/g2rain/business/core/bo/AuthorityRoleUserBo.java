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
import org.springframework.transaction.annotation.Transactional;

import com.g2rain.business.common.enums.PermissionTypeEnum;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.utils.BeanTransferUtils;
import com.g2rain.business.core.mapper.AuthorityRoleMapper;
import com.g2rain.business.core.mapper.AuthorityRoleUserMapper;
import com.g2rain.business.core.mapper.UserMapper;
import com.g2rain.business.core.po.AuthorityPermissionRolePo;
import com.g2rain.business.core.po.AuthorityRolePo;
import com.g2rain.business.core.po.AuthorityRoleSelectParam;
import com.g2rain.business.core.po.AuthorityRoleUserPo;
import com.g2rain.business.core.po.AuthorityRoleUserSelectParam;
import com.g2rain.business.core.po.UserPo;
import com.g2rain.business.core.vo.AddAuthorityRoleUserParam;
import com.g2rain.business.core.vo.AuthorityRoleUserBatchParam;
import com.g2rain.business.core.vo.AuthorityRoleUserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class AuthorityRoleUserBo {

	@Autowired
	private AuthorityRoleUserMapper authorityRoleUserMapper;
	@Autowired
	private AuthorityRoleMapper authorityRoleMapper;
	@Autowired
	private AuthorityPermissionRoleBo authorityPermissionRoleBo;
	@Autowired
	private UserMapper userMapper;

	public int add(AddAuthorityRoleUserParam param) {
		int insertRowCount = 0;
		UserPo userPo = userMapper.get(param.getUserId());
		param.setOrganId(userPo.getOrganId());

		if (CollectionUtils.isNotEmpty(param.getRoleIds())) {
			// 查询是否已存在的用户
			List<AuthorityRoleUserPo> authorityRoleUserPos = authorityRoleUserMapper
					.selectByRoleIdsAndUserId(param.getUserId(), param.getRoleIds());
			Set<String> addRoleIdSet = new HashSet<>(param.getRoleIds());
			if (CollectionUtils.isNotEmpty(authorityRoleUserPos)) {
				for (AuthorityRoleUserPo item : authorityRoleUserPos) {
					addRoleIdSet.remove(item.getRoleId());
				}
			}

			if (CollectionUtils.isNotEmpty(addRoleIdSet)) {
				List<AuthorityRoleUserPo> roleUsers = new ArrayList<>();
				for (String roleId : addRoleIdSet) {
					AuthorityRoleUserPo po = param.toAuthorityRoleUserPo();
					po.setRoleId(roleId);
					roleUsers.add(po);
				}
				insertRowCount = authorityRoleUserMapper.insertBatch(roleUsers);
			}
		} else {
			AuthorityRoleUserPo authorityRoleUserPo = authorityRoleUserMapper.getRoleIdAndUserId(param.getRoleId(),
					param.getUserId());
			if (authorityRoleUserPo == null) {
				AuthorityRoleUserPo po = param.toAuthorityRoleUserPo();
				insertRowCount = authorityRoleUserMapper.insert(po);
			}
		}


		return insertRowCount;
	}

	public SpecificPageInfoResult<AuthorityRoleUserVo> list(AuthorityRoleUserSelectParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<AuthorityRoleUserPo> poList = authorityRoleUserMapper.selectByParam(param);
		PageInfo<AuthorityRoleUserPo> page = new PageInfo<AuthorityRoleUserPo>(poList);
		List<AuthorityRoleUserVo> voList = new ArrayList<AuthorityRoleUserVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (AuthorityRoleUserPo item : poList) {
				AuthorityRoleUserVo vo = new AuthorityRoleUserVo(item);
				voList.add(vo);
			}
		}

		SpecificPageInfoResult<AuthorityRoleUserVo> specificPageInfoResult = new SpecificPageInfoResult<>(
				page.getPageNum(), param.getSize(), page.getTotal(), page.getPages());
		specificPageInfoResult.setObjects(voList);

		return specificPageInfoResult;

	}

	public int delete(String roleId, String userId) {
		int deleteRowCount = authorityRoleUserMapper.delete(roleId, userId);
		return deleteRowCount;
	}

	public List<AuthorityRolePo> listRoleByUserId(String userId) {
		AuthorityRoleUserSelectParam param = new AuthorityRoleUserSelectParam();
		param.setUserId(userId);
		List<AuthorityRoleUserPo> authorityRoleUserPos = authorityRoleUserMapper.selectByParam(param);
		List<AuthorityRolePo> roles = null;
		if (CollectionUtils.isNotEmpty(authorityRoleUserPos)) {
			Map<String, AuthorityRolePo> roleMap = new HashMap<>();
			List<String> roleIdList = new ArrayList<String>();
			for (AuthorityRoleUserPo item : authorityRoleUserPos) {
				roleIdList.add(item.getRoleId());
			}
			Map<String, AuthorityRolePo> itemResult = getRolesByRoleId(roleIdList);
			if (MapUtils.isNotEmpty(itemResult)) {
				roleMap.putAll(itemResult);
				roles = new ArrayList<AuthorityRolePo>();
				roles.addAll(roleMap.values());
			}
		}

		return roles;
	}


	public Map<String, AuthorityRolePo> getRolesByRoleId(List<String> roleIdList) {
		if (CollectionUtils.isEmpty(roleIdList)) {
			return null;
		}
		AuthorityRoleSelectParam param = new AuthorityRoleSelectParam();
		param.setRoleIds(roleIdList);
		List<AuthorityRolePo> authorityRolePos = authorityRoleMapper.selectByParam(param);
		Map<String, AuthorityRolePo> results = new HashMap<String, AuthorityRolePo>();
		for (AuthorityRolePo item : authorityRolePos) {
			results.put(item.getRoleId(), item);
		}

		List<AuthorityPermissionRolePo> authorityPermissionRolePos = authorityPermissionRoleBo
				.listByRoleIdList(roleIdList,
				PermissionTypeEnum.ROLE.name());
		if (CollectionUtils.isNotEmpty(authorityPermissionRolePos)) {
			List<String> paramRoleIds = new ArrayList<>();
			for (AuthorityPermissionRolePo item : authorityPermissionRolePos) {
				if (!results.keySet().contains(item.getPermissionId())) {
					paramRoleIds.add(item.getPermissionId());
				}
			}
			Map<String, AuthorityRolePo> itemResult = getRolesByRoleId(paramRoleIds);
			if (MapUtils.isNotEmpty(itemResult)) {
				results.putAll(itemResult);
			}
		}

		return results;
	}

	public AuthorityRoleUserPo get(String roleId,String userId){
		return authorityRoleUserMapper.getRoleIdAndUserId(roleId,userId);
	}

	@Transactional
	public Map<String, Integer> batchAddOrUpdate(AuthorityRoleUserBatchParam authorityRoleUserBatch) {
		Map<String, Integer> result = new HashMap<>();
		Set<String> userIds = authorityRoleUserBatch.getUserIds();

		List<AuthorityRoleUserPo> roleUsers = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(userIds)) {
			AuthorityRoleUserPo po = null;
			for (String userId : userIds) {
				po = BeanTransferUtils.beanTransfer(authorityRoleUserBatch, AuthorityRoleUserPo.class);
				po.setUserId(userId);
				roleUsers.add(po);
			}
		}

		if (CollectionUtils.isNotEmpty(roleUsers)) {
			int insertRowCount = authorityRoleUserMapper.insertBatch(roleUsers);
			result.put("insertRowCount", insertRowCount);
		}

		if (CollectionUtils.isNotEmpty(authorityRoleUserBatch.getDeleteUserIds())) {
			int deleteRowCount = authorityRoleUserMapper.deleteBatch(authorityRoleUserBatch.getRoleId(),
					authorityRoleUserBatch.getDeleteUserIds());
			result.put("deleteRowCount", deleteRowCount);
		}

		return result;
	}
}