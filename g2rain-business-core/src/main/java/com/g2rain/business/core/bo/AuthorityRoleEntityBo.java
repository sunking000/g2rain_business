package com.g2rain.business.core.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.common.utils.BeanTransferUtils;
import com.g2rain.business.core.mapper.AuthorityRoleEntityMapper;
import com.g2rain.business.core.po.AuthorityRoleEntityPo;
import com.g2rain.business.core.po.AuthorityRoleEntitySelectParam;
import com.g2rain.business.core.vo.AuthorityRoleEntityBatchParam;
import com.g2rain.business.core.vo.AuthorityRoleEntityVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class AuthorityRoleEntityBo {

	@Autowired
	private AuthorityRoleEntityMapper authorityRoleEntityMapper;

	public AuthorityRoleEntityVo add(AuthorityRoleEntityVo vo) {
		AuthorityRoleEntityPo authorityRoleEntityPo = authorityRoleEntityMapper.get(vo.getRoleId(), vo.getEntityId());
		AuthorityRoleEntityPo lastestAuthorityRoleEntityPo = null;
		if (authorityRoleEntityPo != null) {
			lastestAuthorityRoleEntityPo = authorityRoleEntityPo;
		} else {
			authorityRoleEntityMapper.insert(new AuthorityRoleEntityPo(vo));
			lastestAuthorityRoleEntityPo = authorityRoleEntityMapper.get(vo.getRoleId(),
					vo.getEntityId());
		}
		

		return new AuthorityRoleEntityVo(lastestAuthorityRoleEntityPo);
	}


	public SpecificPageInfoResult<AuthorityRoleEntityVo> list(AuthorityRoleEntitySelectParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<AuthorityRoleEntityPo> poList = authorityRoleEntityMapper.selectByParam(param);
		PageInfo<AuthorityRoleEntityPo> page = new PageInfo<AuthorityRoleEntityPo>(poList);
		List<AuthorityRoleEntityVo> voList = new ArrayList<AuthorityRoleEntityVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (AuthorityRoleEntityPo item : poList) {
				AuthorityRoleEntityVo vo = new AuthorityRoleEntityVo(item);
				voList.add(vo);
			}
		}

		SpecificPageInfoResult<AuthorityRoleEntityVo> specificPageInfoResult = new SpecificPageInfoResult<>(
				page.getPageNum(), param.getSize(), page.getTotal(), page.getPages());
		specificPageInfoResult.setObjects(voList);

		return specificPageInfoResult;

	}

	public List<AuthorityRoleEntityVo> listAuthorityRoleEntitys(AuthorityRoleEntitySelectParam param) {
		List<AuthorityRoleEntityPo> poList = authorityRoleEntityMapper.selectByParam(param);
		List<AuthorityRoleEntityVo> voList = new ArrayList<AuthorityRoleEntityVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (AuthorityRoleEntityPo item : poList) {
				AuthorityRoleEntityVo vo = new AuthorityRoleEntityVo(item);
				voList.add(vo);
			}
		}
		return voList;
	}

	public Map<String, Integer> batchAddAndDelete(AuthorityRoleEntityBatchParam authorityRoleEntityBatchVo) {
		Set<String> entityIds = authorityRoleEntityBatchVo.getEntityIds();
		List<AuthorityRoleEntityPo> entityRoles = new ArrayList<>();
		AuthorityRoleEntityPo po = null;
		for (String entityId : entityIds) {
			po = BeanTransferUtils.beanTransfer(authorityRoleEntityBatchVo, AuthorityRoleEntityPo.class);
			po.setEntityId(entityId);
			entityRoles.add(po);
		}

		Map<String, Integer> result = new HashMap<>();
		if (CollectionUtils.isNotEmpty(entityRoles)) {
			int insertRowCount = authorityRoleEntityMapper.insertBatch(entityRoles);
			result.put("insertRowCount", insertRowCount);
		}

		if (CollectionUtils.isNotEmpty(authorityRoleEntityBatchVo.getDeleteEntityIds())) {
			int deleteRowCount = authorityRoleEntityMapper.deleteBatch(authorityRoleEntityBatchVo.getRoleId(),
					authorityRoleEntityBatchVo.getDeleteEntityIds());
			result.put("deleteRowCount", deleteRowCount);
		}

		return result;
	}
}