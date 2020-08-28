package com.g2rain.business.common.bo;

import com.g2rain.business.common.utils.CommonStringUtil;
import com.g2rain.business.common.utils.DateUtil;

public abstract class AbstractIdSequence {

	public String getSequenceId(String prefix, String sequence) {
		String dateString = DateUtil.getDateSecondLongStringFrom2010();
		String sequenceString = CommonStringUtil.formatSixChar(sequence);
		return prefix + dateString + sequenceString;
	}


	public static void main(String[] args) {
		AbstractIdSequence test = new AbstractIdSequence() {
			@Override
			public String getSequenceId(String prefix, String sequence) {
				return super.getSequenceId(prefix, sequence);
			}
		};

		System.out.println(test.getSequenceId("TEST", "126"));
	}
}