package com.aem.community.core.services;

public interface DisableCRXService {
	public boolean isRestrictedUser(String currentUserId);
	public boolean isServiceEnabled();
}
