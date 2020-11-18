package com.g2rain.business.file.store.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;
import org.springframework.stereotype.Service;

import com.g2rain.business.common.bo.AbstractIdSequence;

@Service
public class SequenceBo extends AbstractIdSequence {

	@Autowired
	private MySQLMaxValueIncrementer idSequence;

	public String getDefaultSequenceId() {
		return super.getSequenceId("", idSequence.nextStringValue());
	}
}
