package com.g2rain.business.common.mybatis;

import java.util.HashMap;
import java.util.Map;

public class DataOrganIsolationFlagCache {
	private final static Map<String, DataOrganIsolation> sqlIdDataOrganIsolationFlagCache = new HashMap<String, DataOrganIsolation>();

	public static void put(String sqlId, DataOrganIsolation dataOrganIsolation) {
		sqlIdDataOrganIsolationFlagCache.put(sqlId, dataOrganIsolation);
	}

	public static DataOrganIsolation get(String sqlId) {
		return sqlIdDataOrganIsolationFlagCache.get(sqlId);
	}

	public static boolean containsKey(String sqlId) {
		return sqlIdDataOrganIsolationFlagCache.containsKey(sqlId);
	}
}