package com.g2rain.business.gateway.predicate;

import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.g2rain.business.gateway.utils.JsonObjectUtil;

@Service
public class ReadBodyPredicate implements Predicate<Object> {
	private static final Logger log = LoggerFactory.getLogger(ReadBodyPredicate.class);

	@Override
	public boolean test(Object t) {
		log.info("ReadBodyPredicate Object:{}", JsonObjectUtil.toJson(t));
		return true;
	}
}
