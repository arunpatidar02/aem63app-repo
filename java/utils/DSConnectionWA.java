package com.aem.community.core.utils;

import java.sql.Connection;

public interface DSConnectionWA {
	public Connection getConnection();
	public void closeConnection(Connection con);
}
