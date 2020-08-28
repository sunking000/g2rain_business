package com.g2rain.business.core.bo;

public class OperationLogBo {

	// @Autowired
	// private OperationLogMapper operationLogMapper;

	// public BaseResult add(OperationLogVo operationLog) {
	// OperationLogPo operationLogPo = new OperationLogPo(operationLog);
	// operationLogMapper.insert(operationLogPo);
	// BaseResult success = new BaseResult(BaseResult.SUCCESS);
	// return success;
	// }
	//
	// public BaseResult list(OperationLogSelectParam param) {
	// PageHelper.startPage(param.getPageNum(), param.getPageSize());
	// List<OperationLogPo> poList = operationLogMapper
	// .selectByParam(param);
	// PageInfo<OperationLogPo> page = new PageInfo<OperationLogPo>(poList);
	//
	// BaseResult success = new BaseResult(BaseResult.SUCCESS);
	// List<OperationLogVo> voList = new ArrayList<OperationLogVo>();
	// if (CollectionUtils.isNotEmpty(poList)) {
	// for (OperationLogPo item : poList) {
	// OperationLogVo order = new OperationLogVo(item);
	// voList.add(order);
	// }
	// }
	//
	// success.putData("operationLogs", voList);
	// PageInfoResult pageInfo = new PageInfoResult(page.getPageNum(),
	// param.getSize(), page.getTotal(), page.getPages());
	// success.putData("pageInfo", pageInfo);
	// return success;
	// }
}