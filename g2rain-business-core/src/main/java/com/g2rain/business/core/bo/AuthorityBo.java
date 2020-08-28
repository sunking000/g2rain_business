package com.g2rain.business.core.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.g2rain.business.common.asserts.Asserts;
import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.enums.PermissionTypeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.common.utils.CommonContextContainer;
import com.g2rain.business.core.mapper.AuthorityRoleMapper;
import com.g2rain.business.core.mapper.OrganMapper;
import com.g2rain.business.core.mapper.UserMapper;
import com.g2rain.business.core.po.AuthorityPermissionRoleSelectParam;
import com.g2rain.business.core.po.AuthorityRoleLinkSelectParam;
import com.g2rain.business.core.po.AuthorityRolePo;
import com.g2rain.business.core.po.AuthorityRoleSelectParam;
import com.g2rain.business.core.po.OrganPo;
import com.g2rain.business.core.po.OrganSelectParam;
import com.g2rain.business.core.po.UserPo;
import com.g2rain.business.core.po.UserSelectParam;
import com.g2rain.business.core.po.param.AuthorityRoleDictSelectParam;
import com.g2rain.business.core.utils.AuthorityLinkType;
import com.g2rain.business.core.utils.AuthorityPointTypeEnum;
import com.g2rain.business.core.utils.ErrorCode;
import com.g2rain.business.core.vo.AuthorityDictVo;
import com.g2rain.business.core.vo.AuthorityLinkVo;
import com.g2rain.business.core.vo.AuthorityPermissionRoleVo;
import com.g2rain.business.core.vo.AuthorityRoleUserBatchParam;
import com.g2rain.business.core.vo.AuthorityRoleVo;
import com.g2rain.business.core.vo.AuthorityVo;
import com.g2rain.business.core.vo.OrganVo;
import com.g2rain.business.core.vo.UserVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorityBo {

	@Autowired
	private AuthorityRoleBo autorityRoleBo;
	// @Autowired
	// private AuthorityRoleEntityBo authorityRoleEntityBo;
	@Autowired
	private AuthorityLinkBo authorityLinkBo;
	@Autowired
	private AuthorityPermissionRoleBo authorityPermissionRoleBo;
	@Autowired
	private AuthorityRoleUserBo authorityRoleUserBo;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private OrganMapper organMapper;
	@Autowired
	private AuthorityRoleMapper authorityRoleMapper;
	@Autowired
	private OrganBo organBo;
	@Autowired
	private AuthorityDictBo AuthorityDictBo;

	public Map<String, Integer> fillAuthority(String companyOrganId, String organId) {
		if (!CommonContextContainer.isAdminCompany()) {
			return null;
		}
		// 获取需要处理的组织信息
		Set<OrganPo> organs = new HashSet<>();
		if (StringUtils.isNotBlank(organId)) {
			OrganPo storeOrganPo = organMapper.get(organId);
			organs.add(storeOrganPo);
		} else if (StringUtils.isNotBlank(companyOrganId)) {
			OrganPo companyOrganPo = organMapper.get(companyOrganId);
			organs.add(companyOrganPo);
			OrganSelectParam param = new OrganSelectParam();
			param.setPageSize(1000);
			List<OrganPo> selectByParam = organMapper.selectByParam(param);
			organs.addAll(selectByParam);
		} else {
			OrganSelectParam param = new OrganSelectParam();
			param.setPageSize(1000);
			param.setAdmin(false);
			List<OrganPo> selectByParam = organMapper.selectByParam(param);
			organs.addAll(selectByParam);
		}

		// 为每个组织创建角色，如果创建了默认角色，组织下所有用户添加默认角色
		int addRoleCount = 0;
		int addRoleUserCount = 0;
		if (CollectionUtils.isNotEmpty(organs)) {
			for (OrganPo organPo : organs) {
				AuthorityRoleSelectParam param = new AuthorityRoleSelectParam();
				int roleByOrganId = authorityRoleMapper.countByParam(param);
				if (roleByOrganId > 0) {
					continue;
				} else {
					String roleId = organBo.addRole(new OrganVo(organPo));
					addRoleCount++;
					UserSelectParam userSelectParam = new UserSelectParam();
					List<UserPo> userSelectByParam = userMapper.selectByParam(userSelectParam);
					if (CollectionUtils.isNotEmpty(userSelectByParam)) {
						AuthorityRoleUserBatchParam authorityRoleUserBatchParam = new AuthorityRoleUserBatchParam();
						authorityRoleUserBatchParam.setOrganId(organPo.getOrganId());
						authorityRoleUserBatchParam.setRoleId(roleId);
						Set<String> userIds = new HashSet<>();
						for (UserPo userItem : userSelectByParam) {
							userIds.add(userItem.getUserId());
						}
						authorityRoleUserBatchParam.setUserIds(userIds);
						Map<String, Integer> batchAddOrUpdateResult = authorityRoleUserBo
								.batchAddOrUpdate(authorityRoleUserBatchParam);
						addRoleUserCount += batchAddOrUpdateResult.get("insertRowCount");
					}
				}
			}
		}
		
		Map<String, Integer> result = new HashMap<>();
		result.put("addRoleCount", addRoleCount);
		result.put("addRoleUserCount", addRoleUserCount);

		return result;
	}


	public AuthorityVo getAuthority(String userId, AuthorityPointTypeEnum type) {
		Asserts.assertBlank(userId, ErrorCodeEnum.PARAMETER_ERROR.name());
		UserPo userPo = userMapper.get(userId);
		Asserts.assertNull(userPo, ErrorCode.USER_NON_EXISTENT.name());
		if (userPo.isDeleteFlag()) {
			log.error("用户已被删除, userPo:{}", JSONObject.toJSONString(userPo));
			throw new BussinessRuntimeException(ErrorCode.USER_INFO_ERROR.name());
		}

		AuthorityVo authority = null;
		if (userPo.isAdminFlag()) {
			authority = new AuthorityVo();
			List<AuthorityLinkVo> menus = authorityLinkBo.listAll(AuthorityLinkType.MENU);
			authority.setMenus(menus);
			List<AuthorityDictVo> dicts = AuthorityDictBo.listAll();
			authority.setDicts(dicts);
		} else {
			UserVo user = new UserVo(userPo);
			if (type == null) {
				authority = auth(user);
			} else {
				authority = auth(user, type);
			}
		}

		return authority;
	}

	public AuthorityVo auth(UserVo user) {
		AuthorityVo result = new AuthorityVo();
		List<AuthorityRoleVo> roles = getRoles(user.getOrganId(), user.getUserId());
		result.setRoles(roles);
		List<AuthorityLinkVo> menus = getMenus(result.getRoles());
		result.setMenus(menus);
		List<AuthorityDictVo> dicts = getDicts(roles);
		result.setDicts(dicts);

		return result;
	}

	public AuthorityVo auth(UserVo user, AuthorityPointTypeEnum type) {
		AuthorityVo result = new AuthorityVo();
		List<AuthorityRoleVo> roles = getRoles(user.getOrganId(), user.getUserId());
		result.setRoles(roles);

		switch (type) {
		case MENU:
			List<AuthorityLinkVo> menus = getMenus(result.getRoles());
			result.setMenus(menus);
			break;
		case DICT:
			List<AuthorityDictVo> dicts = getDicts(roles);
			result.setDicts(dicts);
			break;
		default:
			break;
		}
		return result;
	}

	private List<AuthorityDictVo> getDicts(List<AuthorityRoleVo> roles) {
		List<AuthorityDictVo> dictResult = null;
		if (CollectionUtils.isNotEmpty(roles)) {
			List<String> roleIds = new ArrayList<>();
			for (AuthorityRoleVo role : roles) {
				roleIds.add(role.getRoleId());
			}

			AuthorityRoleDictSelectParam param = new AuthorityRoleDictSelectParam();
			param.setPageSize(Integer.MAX_VALUE);
			param.setRoleIds(roleIds);
			dictResult = AuthorityDictBo.list(param);
		}

		return dictResult;
	}

	private List<AuthorityLinkVo> getMenus(List<AuthorityRoleVo> roles) {
		return getLink(AuthorityLinkType.MENU, roles);
	}

	public List<AuthorityLinkVo> getActions(List<AuthorityRoleVo> roles) {
		return getLink(AuthorityLinkType.ACTION, roles);
	}

	public List<AuthorityLinkVo> getLink(AuthorityLinkType linkType, List<AuthorityRoleVo> roles) {
		List<AuthorityLinkVo> menus = null;
		if (CollectionUtils.isNotEmpty(roles)) {
			List<String> roleIds = new ArrayList<>();
			for (AuthorityRoleVo role : roles) {
				roleIds.add(role.getRoleId());
			}

			AuthorityRoleLinkSelectParam param = new AuthorityRoleLinkSelectParam();
			param.setPageSize(Integer.MAX_VALUE);
			param.setType(linkType.name());
			param.setRoleIds(roleIds);
			menus = authorityLinkBo.listBy(param);
//			if (CollectionUtils.isNotEmpty(list)) {
//				for (AuthorityLinkVo item : list) {
//					if (StringUtils.isNotBlank(item.getParentLinkId()) && !"0".equals(item.getParentLinkId())) {
//						AuthorityLinkVo authorityLinkVo = menuMap.get(item.getParentLinkId());
//						if (authorityLinkVo == null) {
//							authorityLinkVo = new AuthorityLinkVo();
//							menuMap.put(item.getParentLinkId(), authorityLinkVo);
//						}
//						authorityLinkVo.addSubMenu(item);
//					} else {
//						AuthorityLinkVo authorityLinkVo = menuMap.get(item.getLinkId());
//						if (authorityLinkVo == null) {
//							menuMap.put(item.getLinkId(), item);
//						} else {
//							if (authorityLinkVo.getLinkId() == null) {
//								authorityLinkVo.getSubMenuMap();
//								item.setSubMenuMap(menuMap);
//								menuMap.put(item.getLinkId(), item);
//							}
//						}
//					}
//				}
//			}
		}

		if (CollectionUtils.isNotEmpty(menus) && menus.size() > 1) {
			Collections.sort(menus);
		}

		return menus;
	}

	private List<AuthorityRoleVo> getRoles(String organId, String userId) {
		List<AuthorityRolePo> authorityRoles = authorityRoleUserBo.listRoleByUserId(userId);

		Set<String> roleIds = new HashSet<>();
		if (CollectionUtils.isNotEmpty(authorityRoles)) {
			for (AuthorityRolePo item : authorityRoles) {
				roleIds.add(item.getRoleId());
			}
			roleIds.addAll(getRefRoleIdByRoleId(roleIds, organId));
		}

		List<AuthorityRoleVo> roles = new ArrayList<AuthorityRoleVo>();
		if (CollectionUtils.isNotEmpty(roleIds)) {
			AuthorityRoleSelectParam param = new AuthorityRoleSelectParam();
			List<String> roleIdList = new LinkedList<>();
			roleIdList.addAll(roleIds);
			param.setRoleIds(roleIdList);
			roles = autorityRoleBo.selectByParam(param);
		}

		return roles;
	}

	private Set<String> getRefRoleIdByRoleId(Set<String> roleIds, String organId) {
		List<String> roleIdList = new ArrayList<>();
		roleIdList.addAll(roleIds);

		AuthorityPermissionRoleSelectParam authorityPermissionRoleSelectParam = new AuthorityPermissionRoleSelectParam();
		authorityPermissionRoleSelectParam.setOrganId(organId);
		authorityPermissionRoleSelectParam.setPermissionType(PermissionTypeEnum.ROLE.name());
		List<AuthorityPermissionRoleVo> authorityPermissionRoleVos = authorityPermissionRoleBo
				.listAuthorityPermissionRoles(authorityPermissionRoleSelectParam);
		
		Map<String, Set<String>> authorityPermissionRoleIdMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(authorityPermissionRoleVos)) {
			for (AuthorityPermissionRoleVo item : authorityPermissionRoleVos) {
				Set<String> permissionIdSet = authorityPermissionRoleIdMap.get(item.getRoleId());
				if (permissionIdSet == null) {
					permissionIdSet = new HashSet<>();
				}
				permissionIdSet.add(item.getPermissionId());
			}
		}

		Set<String> resultRoleIds = new HashSet<>();
		resultRoleIds.addAll(roleIds);
		if (MapUtils.isNotEmpty(authorityPermissionRoleIdMap)) {
			int size = roleIdList.size() + authorityPermissionRoleVos.size();
			// Set<String> refRolesId = new HashSet<>();
			for (int i = 0; i < size; i++) {
				// refRolesId.add(item.getPermissionId());
				if (i >= roleIdList.size()) {
					break;
				}
				String roleId = roleIdList.get(i);
				Set<String> set = authorityPermissionRoleIdMap.get(roleId);
				if (CollectionUtils.isNotEmpty(set)) {
					roleIdList.addAll(set);
					resultRoleIds.addAll(set);
				}
			}
			// resultRoleIds.addAll(getRefRoleIdByRoleId(refRolesId));
		}

		return resultRoleIds;
	}

	// private Set<String> getRefRoleIdByRoleId(Set<String> roleIds) {
	// List<String> roleIdList = new ArrayList<>();
	// roleIdList.addAll(roleIds);
	// List<AuthorityPermissionRolePo> authorityPermissionRolePos =
	// authorityPermissionRoleBo
	// .listByRoleIdList(roleIdList,
	// PermissionTypeEnum.ROLE.name());
	// Set<String> resultRoleIds = new HashSet<>();
	// if (CollectionUtils.isNotEmpty(authorityPermissionRolePos)) {
	// Set<String> refRolesId = new HashSet<>();
	// for (AuthorityPermissionRolePo item : authorityPermissionRolePos) {
	// refRolesId.add(item.getPermissionId());
	// }
	// resultRoleIds.addAll(getRefRoleIdByRoleId(refRolesId));
	// }
	//
	// return resultRoleIds;
	// }
}