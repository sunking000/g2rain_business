package com.g2rain.business.gateway.shell.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;
import org.springframework.stereotype.Service;

import com.g2rain.business.common.bo.AbstractIdSequence;



@Service
public class SequenceBo extends AbstractIdSequence {

	@Autowired
	private MySQLMaxValueIncrementer idSequence;

	public final static String ROUTE_DEFINITION = "RD";


	public String getRouteDefinitionId() {
		return super.getSequenceId(ROUTE_DEFINITION, idSequence.nextStringValue());
	}
}
