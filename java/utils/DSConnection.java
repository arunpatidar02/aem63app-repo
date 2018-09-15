package com.aem.community.core.utils;

import java.sql.Connection;

public interface DSConnection {
	public Connection getConnection(String datasourceName);
	public void closeConnection(Connection con);
}
