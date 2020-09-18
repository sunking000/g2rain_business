package com.g2rain.business.example.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2rain.business.common.asserts.Asserts;
import com.g2rain.business.common.enums.ErrorCodeEnum;
import com.g2rain.business.common.result.SpecificPageInfoResult;
import com.g2rain.business.example.mapper.ExampleMapper;
import com.g2rain.business.example.po.ExamplePo;
import com.g2rain.business.example.po.param.ExampleSelectParam;
import com.g2rain.business.example.vo.ExampleVo;
import com.g2rain.business.example.vo.param.AddOrUpdateExampleParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ExampleBo {
	private static final String segmentationChar = ",";

	@Autowired
	private SequenceBo sequenceBo;
	@Autowired
	private ExampleMapper exampleMapper;

	public int addOrUpdate(AddOrUpdateExampleParam param) {
		Asserts.assertNull(param, ErrorCodeEnum.PARAMETER_ERROR.name());

		ExamplePo examplePo = param.toExamplePo();
		int editRowCount = 0;
		if (StringUtils.isNotBlank(param.getExampleId())) {
			editRowCount = exampleMapper.update(examplePo);
		} else {
			examplePo.setExampleId(sequenceBo.getDefaultSequenceId());
			editRowCount = exampleMapper.insert(examplePo);
		}

		return editRowCount;
	}

	public int addBatch(List<AddOrUpdateExampleParam> addParams) {
		Asserts.assertNull(addParams, ErrorCodeEnum.PARAMETER_ERROR.name());
		Asserts.assertEmpty(addParams, ErrorCodeEnum.PARAMETER_ERROR.name());
		
		List<ExamplePo> examplePos = new ArrayList<ExamplePo>();
		for (AddOrUpdateExampleParam item : addParams) {
			ExamplePo examplePo = item.toExamplePo();
			examplePo.setExampleId(sequenceBo.getDefaultSequenceId());
			examplePos.add(examplePo);
		}
		
		return exampleMapper.insertBatch(examplePos);
	}

	public int delete(String exampleIds) {
		Asserts.assertBlank(exampleIds, ErrorCodeEnum.PARAMETER_ERROR.name());
		
		int deleteRowCount = 0;
		if (exampleIds.indexOf(segmentationChar) > -1) {
			deleteRowCount = exampleMapper.delete(exampleIds);
		} else {
			String[] exampleIdArray = exampleIds.split(segmentationChar);
			List<String> exampleIdList = Arrays.asList(exampleIdArray);
			Set<String> exampleIdSet = new HashSet<>(exampleIdList);
			deleteRowCount = exampleMapper.deleteBatch(exampleIdSet);
		}
		

		return deleteRowCount;
	}

	public SpecificPageInfoResult<ExampleVo> list(ExampleSelectParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		List<ExamplePo> poList = exampleMapper.selectByParam(param);
		PageInfo<ExamplePo> page = new PageInfo<ExamplePo>(poList);

		List<ExampleVo> organs = new ArrayList<ExampleVo>();
		if (CollectionUtils.isNotEmpty(poList)) {
			for (ExamplePo item : poList) {
				ExampleVo vo = new ExampleVo(item);
				organs.add(vo);
			}
		}

		SpecificPageInfoResult<ExampleVo> specificPageInfoResult = new SpecificPageInfoResult<>(page.getPageNum(),
				param.getSize(), page.getTotal(), page.getPages());
		specificPageInfoResult.setObjects(organs);

		return specificPageInfoResult;
	}
}
