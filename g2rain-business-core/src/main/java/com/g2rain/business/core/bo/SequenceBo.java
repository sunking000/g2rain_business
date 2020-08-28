package com.g2rain.business.core.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;
import org.springframework.stereotype.Service;

import com.g2rain.business.common.bo.AbstractIdSequence;

@Service
public class SequenceBo extends AbstractIdSequence {

	@Value("${server.prefix}")
	private String paymentRecordIdPrefix;

	@Autowired
	private MySQLMaxValueIncrementer idSequence;

	public final static String AUTHORITY_DICT = "AUTHD";
	public final static String AUTHORITY_ROLE = "AUTHR";
	public final static String AUTHORITY_ROLE_USER = "AUTHRU";
	public final static String AUTHORITY_LINK = "AUTHL";
	public final static String DEVICE = "DEV";
	public final static String ORGAN = "ORG";
	public final static String ORGAN_EXT = "ORGEXT";
	public final static String USER = "USER";
	public final static String MEMBER = "MEM";
	public final static String PERSON = "PER";
	public final static String CARD = "CARD";
	public final static String ORDER = "OR";
	public final static String PAYMENT = "PA";
	public final static String PAYMENT_RECORD = "PR";
	public final static String MEMBER_CARD_OPERATION_RECORD = "MCOR";
	public final static String REFUND_ORDER_NO = "RON";
	public final static String REFUND_PAY_ORDER_NO = "RPON";


	public String getAuthorityDictId() {
		return super.getSequenceId(AUTHORITY_DICT, idSequence.nextStringValue());
	}

	public String getAuthorityRoleId() {
		return super.getSequenceId(AUTHORITY_ROLE, idSequence.nextStringValue());
	}

	public String getAuthorityRoleUserId() {
		return super.getSequenceId(AUTHORITY_ROLE_USER, idSequence.nextStringValue());
	}

	public String getAuthorityLinkId() {
		return super.getSequenceId(AUTHORITY_LINK, idSequence.nextStringValue());
	}

	public String getDeviceId() {
		return super.getSequenceId(DEVICE, idSequence.nextStringValue());
	}
	
	public String getOrganId() {
		return super.getSequenceId(ORGAN, idSequence.nextStringValue());
	}

	public String getUserId() {
		return super.getSequenceId(USER, idSequence.nextStringValue());
	}

	public String getMemberId() {
		return super.getSequenceId(MEMBER, idSequence.nextStringValue());
	}

	public String getPersonId() {
		return super.getSequenceId(PERSON, idSequence.nextStringValue());
	}

	public String getCardId() {
		return super.getSequenceId(CARD, idSequence.nextStringValue());
	}

	public String getOrderNo() {
		return super.getSequenceId(ORDER, idSequence.nextStringValue());
	}

	public String getPaymentNo() {
		return super.getSequenceId(PAYMENT, idSequence.nextStringValue());
	}

	public String getPaymentRecordId() {
		String prefix = paymentRecordIdPrefix == "@server.prefix@" ? PAYMENT_RECORD
				: paymentRecordIdPrefix + PAYMENT_RECORD;
		return super.getSequenceId(prefix, idSequence.nextStringValue());
	}

	public String getMemberCardOperationRecordId() {
		return super.getSequenceId(MEMBER_CARD_OPERATION_RECORD, idSequence.nextStringValue());
	}

    public String getRefundOrderNo() {
		return super.getSequenceId(REFUND_ORDER_NO, idSequence.nextStringValue());
    }

	public String getRefundPayOrderNo() {
		return super.getSequenceId(REFUND_PAY_ORDER_NO, idSequence.nextStringValue());
	}

	public String getOrganExtId() {
		return super.getSequenceId(ORGAN_EXT, idSequence.nextStringValue());
	}
}
