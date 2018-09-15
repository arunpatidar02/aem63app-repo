package com.aem.community.core.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.commons.datasource.poolservice.DataSourcePool;

@Component
public class DSConnectionImpl implements DSConnection {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Reference
	private DataSourcePool source;

	// Returns a connection using the configured DataSourcePool
	@Override
	public Connection getConnection(String datasourceName) {
		DataSource dataSource = null;
		Connection con = null;
		try {
			// Inject the DataSourcePool right here!
			dataSource = (DataSource) source.getDataSource("aem63app");
			con = dataSource.getConnection();
			log.debug("Connected to database");
			//return con;

		} catch (Exception e) {
			e.printStackTrace();
			log.debug("DSConnectionImpl : Unable to connect to database" + e);
		}
		return con;
	}

	@Override
	public void closeConnection(Connection con) {
		// TODO Auto-generated method stub
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

