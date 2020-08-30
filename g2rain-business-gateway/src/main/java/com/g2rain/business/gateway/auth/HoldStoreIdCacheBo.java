package com.g2rain.business.gateway.auth;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2rain.business.gateway.adapter.CoreClient;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Service
public class HoldStoreIdCacheBo {

	@Autowired
	private CoreClient coreClient;

	private Cache<String, String> holdStoreIdsCache = CacheBuilder.newBuilder().maximumSize(100000)
			.expireAfterWrite(60, TimeUnit.MINUTES).build();

	public String holdStoreIds(String organId) {
		String holdStoreIds = null;
		if ((holdStoreIds = holdStoreIdsCache.getIfPresent(organId)) == null) {
			holdStoreIds = coreClient.getHoldStoreIds(organId);
			holdStoreIdsCache.put(organId, holdStoreIds);
		}

		return holdStoreIds;
	}
}
