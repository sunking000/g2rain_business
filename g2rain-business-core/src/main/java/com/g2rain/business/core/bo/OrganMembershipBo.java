package com.g2rain.business.core.bo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2rain.business.common.asserts.Asserts;
import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.enums.OrganTypeEnum;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.core.mapper.OrganMapper;
import com.g2rain.business.core.mapper.OrganMembershipMapper;
import com.g2rain.business.core.po.OrganMembershipPo;
import com.g2rain.business.core.po.OrganPo;
import com.g2rain.business.core.po.param.OrganMembershipSelectParam;
import com.g2rain.business.core.utils.ErrorCode;
import com.g2rain.business.core.vo.AddOrganMembershipParam;
import com.g2rain.business.core.vo.OrganMembershipVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class OrganMembershipBo {

	@Autowired
	private OrganMembershipMapper organMembershipMapper;
	@Autowired
	private OrganMapper organMapper;

	public int add(AddOrganMembershipParam param) {
		Asserts.assertBlank(param.getParentOrganId(), ErrorCodeEnum.PARAMETER_ERROR.name());
		Asserts.assertBlank(param.getChildOrganId(), ErrorCodeEnum.PARAMETER_ERROR.name());

		OrganPo parentOrganPo = organMapper.get(param.getParentOrganId());
		Asserts.assertNull(parentOrganPo, ErrorCode.ORGAN_NON_EXISTENT.name());
		OrganPo childOrganPo = organMapper.get(param.getChildOrganId());
		Asserts.assertNull(childOrganPo, ErrorCode.ORGAN_NON_EXISTENT.name());

		OrganMembershipPo organMembershipPo = new OrganMembershipPo();
		organMembershipPo.setParentOrganId(param.getParentOrganId());
		organMembershipPo.setChildOrganId(param.getChildOrganId());
		organMembershipPo.setChildOrganType(childOrganPo.getType());

		return organMembershipMapper.insert(organMembershipPo);
	}

	public int delete(String parentOrganId, String childOrganId) {
		return organMembershipMapper.delete(parentOrganId, childOrganId);
	}

	public SpecificPageInfoResult<OrganMembershipVo> list(OrganMembershipSelectParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<OrganMembershipPo> organMembershipList = organMembershipMapper.selectByParam(param);
		PageInfo<OrganMembershipPo> page = new PageInfo<OrganMembershipPo>(organMembershipList);

		List<OrganMembershipVo> organMembershipVos = new ArrayList<OrganMembershipVo>();
		if (CollectionUtils.isNotEmpty(organMembershipList)) {
			for (OrganMembershipPo item : organMembershipList) {
				OrganMembershipVo organ = new OrganMembershipVo(item);
				organMembershipVos.add(organ);
			}
		}

		SpecificPageInfoResult<OrganMembershipVo> specificPageInfoResult = new SpecificPageInfoResult<>(
				page.getPageNum(), param.getSize(), page.getTotal(), page.getPages());
		specificPageInfoResult.setObjects(organMembershipVos);

		return specificPageInfoResult;
	}
	
	public List<OrganMembershipVo> listObjects(OrganMembershipSelectParam param) {
		List<OrganMembershipPo> organMembershipList = organMembershipMapper.selectByParam(param);
		List<OrganMembershipVo> organMembershipVos = new ArrayList<OrganMembershipVo>();
		if (CollectionUtils.isNotEmpty(organMembershipList)) {
			for (OrganMembershipPo item : organMembershipList) {
				OrganMembershipVo organ = new OrganMembershipVo(item);
				organMembershipVos.add(organ);
			}
		}

		return organMembershipVos;
	}

	public Set<String> getHoldStoreIds(String organId) {
		Asserts.assertBlank(organId, ErrorCodeEnum.PARAMETER_ERROR.name());
		OrganPo organPo = organMapper.get(organId);
		Asserts.assertNull(organPo, ErrorCodeEnum.PARAMETER_ERROR.name());

		if (OrganTypeEnum.STORE.name().equals(organPo.getType())) {
			return null;
		}
		Set<String> organIds = new HashSet<>();
		organIds.add(organId);
		Set<String> holdStoreIds = getHoldStoreIds(organIds, null);

		return holdStoreIds;
	}

	public Set<String> getHoldStoreIds(Set<String> organIds, Set<String> checkedCompanyOrganIds) {
		if (CollectionUtils.isEmpty(organIds)) {
			return null;
		}

		final Set<String> holdStoreIds = new HashSet<>();
		OrganMembershipSelectParam param = new OrganMembershipSelectParam();
		param.setKeyIds(organIds);
		List<OrganMembershipPo> organMembershipList = organMembershipMapper.selectByParam(param);
		if (CollectionUtils.isNotEmpty(organMembershipList)) {
			Set<String> companyOrganIds = new HashSet<>();
			final Set<String> checkedCompanyOrganIdsTemp = checkedCompanyOrganIds == null ? new HashSet<>()
					: checkedCompanyOrganIds;
			checkedCompanyOrganIdsTemp.addAll(organIds);
			organMembershipList.forEach(item -> {
				if (OrganTypeEnum.COMPANY.name().equals(item.getChildOrganType())
						&& !checkedCompanyOrganIdsTemp.contains(item.getChildOrganId())) {
					companyOrganIds.add(item.getChildOrganId());
				} else {
					holdStoreIds.add(item.getChildOrganId());
				}
			});

			if (CollectionUtils.isNotEmpty(companyOrganIds)) {
				holdStoreIds.addAll(getHoldStoreIds(companyOrganIds, checkedCompanyOrganIdsTemp));
			}
		}

		return holdStoreIds;
	}
}