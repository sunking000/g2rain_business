package com.g2rain.business.core.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.g2rain.business.common.asserts.Asserts;
import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.enums.OrganTypeEnum;
import com.g2rain.business.common.enums.PermissionTypeEnum;
import com.g2rain.business.common.exception.BussinessRuntimeException;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.utils.CommonContextContainer;
import com.g2rain.business.core.mapper.AuthorityRoleMapper;
import com.g2rain.business.core.mapper.OrganMapper;
import com.g2rain.business.core.po.AuthorityRolePo;
import com.g2rain.business.core.po.OrganPo;
import com.g2rain.business.core.po.OrganSelectParam;
import com.g2rain.business.core.utils.AuthorityRoleTypeEnum;
import com.g2rain.business.core.utils.ErrorCode;
import com.g2rain.business.core.utils.OrganStatusEnum;
import com.g2rain.business.core.utils.SystemRoleCodeEnum;
import com.g2rain.business.core.vo.AuthorityPermissionRoleVo;
import com.g2rain.business.core.vo.AuthorityRoleVo;
import com.g2rain.business.core.vo.OrganVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrganBo {

	// private static final String DEFAULT_CODE_NAME = "默认角色";
	private static final String DEFAULT_COMPANY_CODE_NAME = "默认公司角色";
	private static final String DEFAULT_STORE_CODE_NAME = "默认门店角色";

	@Autowired
	private OrganMapper organMapper;
	@Autowired
	private SequenceBo sequenceBo;
	@Autowired
	private AuthorityRoleMapper authorityRoleMapper;
	@Autowired
	private AuthorityRoleBo authorityRoleBo;
	@Autowired
	private AuthorityPermissionRoleBo authorityPermissionRoleBo;

	public OrganVo get(String organId) {
		OrganVo organ = getOrgan(organId);
		return organ;
	}

	public OrganVo getOrgan(String organId) {
		OrganPo organPo = organMapper.get(organId);
		if (organPo == null) {
			log.error("组织机构不存在,机构id:{}",organId);
			throw new BussinessRuntimeException(
					ErrorCodeEnum.HTTP_CLIENT_REQUEST_PARAM_ERROR.name());
		}
		OrganVo organ = new OrganVo(organPo);
		organ = fullName(organ);

		return organ;
	}

	public int update(OrganVo organ) {
		OrganPo organPo = organMapper.get(organ.getOrganId());
		if (organPo == null) {
			log.error("组织机构不存在,机构id:{}",organ.getOrganId());
			throw new BussinessRuntimeException(
					ErrorCodeEnum.HTTP_CLIENT_REQUEST_PARAM_ERROR.name());
		}

		if (StringUtils.isNotBlank(organ.getName())) {
			organPo.setName(organ.getName());
		}
		if(StringUtils.isNotBlank(organ.getOutOrganId())){
			organPo.setOutOrganId(organ.getOutOrganId());
		}
		organPo.setAddress(organ.getAddress());
		organPo.setCityCode(organ.getCityCode());
		organPo.setLatitude(organ.getLatitude());
		organPo.setLongitude(organPo.getLongitude());

		return organMapper.update(organPo);
	}

	public int delete(String organId) {
		int deleteRowCount = organMapper.delete(organId);
		if (deleteRowCount != 1) {
			log.error("组织机构更新失败,机构id:{}",organId);
			throw new BussinessRuntimeException(
					ErrorCodeEnum.DATA_SOURCE_ERROR.name());
		}
		return deleteRowCount;
	}

	@Transactional
	public String add(OrganVo organ) {
		organ.setOrganId(sequenceBo.getOrganId());

		OrganSelectParam param = new OrganSelectParam();
		param.setName(organ.getName());
		int organCount = organMapper.countByParam(param);
		if (organCount > 0) {
			log.error("名称已存在,organ:{}", JSONObject.toJSONString(organ));
			throw new BussinessRuntimeException(ErrorCode.NAME_EXIST.name());
		}

		if (organ.getStatus() == null) {
			organ.setStatus(OrganStatusEnum.NORMAL);
		}

		OrganPo po = new OrganPo(organ);

		int insert = organMapper.insert(po);
		if (insert < 1) {
			throw new BussinessRuntimeException(
					ErrorCodeEnum.DATA_SOURCE_ERROR.name());
		}

		addRole(organ);

		return organ.getOrganId();
	}
	
	public String addRole(OrganVo organ) {
		String roleCode = SystemRoleCodeEnum.CLIENT.name();
		if (organ.getAdmin()) {
			roleCode = SystemRoleCodeEnum.OPERATE.name();
		}
		AuthorityRolePo authorityRolePoByCode = authorityRoleMapper.getByCode(roleCode);
		if (authorityRolePoByCode == null) {
			return null;
		}
		
		AuthorityRoleVo addAuthorityRoleVo = new AuthorityRoleVo();
		if (OrganTypeEnum.STORE.name().equals(organ.getType())) {
			addAuthorityRoleVo.setName(DEFAULT_STORE_CODE_NAME);
		} else {
			addAuthorityRoleVo.setName(DEFAULT_COMPANY_CODE_NAME);
		}

		addAuthorityRoleVo.setType(AuthorityRoleTypeEnum.SPECIAL);
		AuthorityRoleVo authorityRoleVo = authorityRoleBo.add(addAuthorityRoleVo);
		
		AuthorityPermissionRoleVo authorityPermissionRoleVo = new AuthorityPermissionRoleVo();
		authorityPermissionRoleVo.setRoleId(authorityRoleVo.getRoleId());
		authorityPermissionRoleVo.setPermissionId(authorityRolePoByCode.getRoleId());
		authorityPermissionRoleVo.setPermissionType(PermissionTypeEnum.ROLE.name());
		authorityPermissionRoleBo.addOrEdit(authorityPermissionRoleVo);


		return authorityRoleVo.getRoleId();
	}


	/**
	 * 获取组织列表
	 * 
	 * @param param
	 * @return
	 */
	public SpecificPageInfoResult<OrganVo> list(OrganSelectParam param) {
		if (!CommonContextContainer.isAdminCompany()) {
			// 如果不是运营公司的管理员仅仅显示非admin的组织结构
			param.setAdmin(false);
		}
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<OrganPo> organList = organMapper.selectByParam(param);
		PageInfo<OrganPo> page = new PageInfo<OrganPo>(organList);

		List<OrganVo> organs = new ArrayList<OrganVo>();
		if (CollectionUtils.isNotEmpty(organList)) {
			for (OrganPo item : organList) {
				OrganVo organ = new OrganVo(item);
				organ = fullName(organ);
				organs.add(organ);
			}
		}

		SpecificPageInfoResult<OrganVo> specificPageInfoResult = new SpecificPageInfoResult<>(
				page.getPageNum(), param.getSize(), page.getTotal(), page.getPages());
		specificPageInfoResult.setObjects(organs);

		return specificPageInfoResult;
	}
	
	private OrganVo fullName(OrganVo organ) {
		
		StringBuilder organIdsStringBuilder = new StringBuilder(); 
		if(organIdsStringBuilder.length() > 0) {
			organIdsStringBuilder.deleteCharAt(organIdsStringBuilder.length()-1);
			OrganSelectParam param = new OrganSelectParam();
			param.setOrganIds(organIdsStringBuilder.toString());
			List<OrganPo> organPoList = organMapper.selectByParam(param );
			
			if(CollectionUtils.isNotEmpty(organPoList)) {
				Map<String, OrganPo> organMap = new HashMap<String, OrganPo>();
				for(OrganPo item : organPoList) {
					organMap.put(item.getOrganId(), item);
				}
			}
		}
		
		return organ;
	}

	public OrganPo getByOutOrganId(String outOrganId){
		OrganPo organPo = organMapper.getByOutOrganId(outOrganId);
		return organPo;
	}

	public int updateStatus(String organId, String status) {
		Asserts.assertBlank(organId, ErrorCodeEnum.PARAMETER_ERROR.name(), organId);
		Asserts.assertBlank(status, ErrorCodeEnum.PARAMETER_ERROR.name(), status);

		if (OrganStatusEnum.valueOf(status) == null) {
			log.error("状态不存在,organId:{}, status:{}", organId, status);
			throw new BussinessRuntimeException(ErrorCode.ORGAN_STATUS_ERROR.name());
		}

		int updateRowCount = organMapper.updateStatus(organId, status);

		return updateRowCount;
	}
}