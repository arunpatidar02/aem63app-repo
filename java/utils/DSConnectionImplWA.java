package com.aem.community.core.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.commons.datasource.poolservice.DataSourceNotFoundException;
import com.day.commons.datasource.poolservice.DataSourcePool;

@Component(immediate = true,
scope = ServiceScope.SINGLETON)
public class DSConnectionImplWA implements DSConnectionWA {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	private final String DATA_SOURCE_NAME = "aem63app";

	@Reference
	private DataSourcePool source;

	DataSource dataSource = null;
	
	
	
	// Returns a connection using the configured DataSourcePool
	@Override
	public Connection getConnection() {
		Connection con = null;
		if (dataSource != null) {
            try {
                 con = dataSource.getConnection();
                 log.info("Already instantiated ...");
                 return con;
             } catch (Exception e) {
                 log.error(e.getMessage());
            }
       } else {
            try {
                 dataSource = (DataSource) source.getDataSource(DATA_SOURCE_NAME);
                 con = dataSource.getConnection();
                 log.info("dataSource in instantiated ...");
            } catch (Exception e) {
                 log.error(e.getMessage());
            }
            return con;
       }
       return null;
		
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
	
	@Activate
	@Modified
    public void activate() {
          try {
                   dataSource = (DataSource) source.getDataSource(DATA_SOURCE_NAME);
                   log.info("activation step: dataSource in instantiated ...");
         } catch(DataSourceNotFoundException e) {
                   log.info(e.getMessage());
          }
   }

}

