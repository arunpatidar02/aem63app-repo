package com.acc.aem64.core.services;

public interface PageJSONServletService {
	public String[] getExcludedProperties();
	public String[] getReferenceProperties();
	public String[] getRenameProperties();
	public long getLimit();
}
