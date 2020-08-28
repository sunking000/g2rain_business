package com.github.sunking000.g2rain.business.gateway.utils;


/**
 * 组织类型
 * 
 * @author SUNHAOJIE
 *
 */
public enum OrganTypeEnum {
	COMPANY("公司"), STORE("门店");
	
	public static OrganTypeEnum typeOf(String type) {
		for(OrganTypeEnum item : OrganTypeEnum.values()) {
			if(item.name().equals(type)) {
				return item;
			}
		}
		
		return null;
	}
	
	private OrganTypeEnum(String typeName) {
		this.typeName = typeName;
	}
	
	public String typeName;
}
