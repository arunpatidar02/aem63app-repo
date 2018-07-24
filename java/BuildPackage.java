package com.aem.community.core.workflows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;

import sun.misc.BASE64Encoder;



@Component(service = WorkflowProcess.class, property = {
		Constants.SERVICE_DESCRIPTION + "=A workflow process to Build Package.",
		"process.label=A workflow process to Build Package." })
public class BuildPackage implements WorkflowProcess {
//	private static final String PACKAGE_LOCATION = "http://localhost:4502/crx/packmgr/service/script.html/etc/packages/AEM63App/AEM63App_WfChanges-1.0.zip";
// http://localhost:4505:/crx/packmgr/service/.json/etc/packages/AEM63App/AEM63App-3.zip?cmd=build
	
//	http://localhost:4502/crx/packmgr/service/script.html/etc/packages/AEM63App/AEM63App_WfChanges-1.0.zip
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static final String POST_URL = "http://localhost:4502/crx/packmgr/service/script.html/etc/packages/AEM63App/AEM63App_WfChanges-1.0.zip";
	private static final String username = "admin";
	private static final String password = "admin";
	private static final String POST_PARAMS="cmd=build";
	
	
	public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
		
		try {
			log.info("POST START");
			sendPOST(log);
			log.info("POST DONE");
			
		}catch(Exception e) {
			
		}
		
			}
	

	private static void sendPOST(Logger log) throws IOException {
		URL obj = new URL(POST_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		BASE64Encoder enc = new sun.misc.BASE64Encoder();
	      String userpassword = username + ":" + password;
	      String encodedAuthorization = enc.encode( userpassword.getBytes() );
	      con.setRequestProperty("Authorization", "Basic "+
	            encodedAuthorization);
	
		con.setRequestMethod("POST");
		
		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		log.info("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			log.info(response.toString());
		} else {
			log.info("POST request not worked");
		}
	}

	
}