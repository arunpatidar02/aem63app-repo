package com.aem.community.core.workflows;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.community.core.bean.AEM63WorkflowBean;
import com.aem.community.core.database.dao.AEM63WorkflowDAO;
import com.aem.community.core.utils.DSConnection;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.HistoryItem;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;

@Component(service = WorkflowProcess.class, property = {
		Constants.SERVICE_DESCRIPTION
				+ "=Workflow process implementation to Write Workflow Id and Initiater in the Database.",
		Constants.SERVICE_VENDOR + "=Adobe", "process.label=Write Workflow details in Database" })
public class WriteWFdetailsInDBProcess implements WorkflowProcess {
	private static final String TYPE_JCR_PATH = "JCR_PATH";

	@Reference
	private DSConnection dc;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
		WorkflowData workflowData = item.getWorkflowData();
		if (workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {
			String path = workflowData.getPayload().toString() + "/jcr:content";
			/*
			 * String user = item.getCurrentAssignee().toString();
			 * log.info("Assignee user : "+user);
			 */
			Connection con = null;
			try {

				String strStratTime = item.getWorkflow().getTimeStarted().toString();
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
				cal.setTime(sdf.parse(strStratTime));
				java.sql.Timestamp wfStartDate = new java.sql.Timestamp(cal.getTimeInMillis());

				String wfId = getWFInstancePath(item.getId());
				String wfInitiator = item.getWorkflow().getInitiator();
				String wfPayload = workflowData.getPayload().toString();
				String wfName = item.getWorkflow().getWorkflowModel().getTitle();
				String wfTitle = workflowData.getMetaDataMap().get("workflowTitle").toString();
				String wfDetails = "";

				JSONArray jsonArr = new JSONArray();

				List<HistoryItem> historyList = session.getHistory(item.getWorkflow());
				int listSize = historyList.size();
			//	log.info("listSize = {}", listSize);
				for (int i = 0; i < listSize; i++) {
					JSONObject jsonObj = new JSONObject();
					HistoryItem lastItem = historyList.get(i);

					jsonObj.put("lastComment", lastItem.getComment());
					jsonObj.put("lastAction", lastItem.getAction());
					jsonObj.put("lastUser", lastItem.getUserId());
					jsonObj.put("workItemId", lastItem.getWorkItem().getId());
					jsonObj.put("workItemStartDate", lastItem.getWorkItem().getTimeStarted().toString());
					jsonObj.put("workItemEndDate", lastItem.getWorkItem().getTimeEnded().toString());

					jsonArr.put(jsonObj);
				}

				String wfApprover = "";
				String wfReviewer = "";

				Node node = (Node) session.getSession().getItem(path);
				if (node != null) {
					if (node.hasProperty("mywfapprover")) {
						wfApprover = node.getProperty("mywfapprover").getString();
					}
					if (node.hasProperty("mywfreviewer")) {
						wfReviewer = node.getProperty("mywfreviewer").getString();
					}
				}

				JSONObject jsonObj = new JSONObject();

				jsonObj.put("wfApprover", wfApprover);
				jsonObj.put("wfReviewer", wfReviewer);
				jsonObj.put("wfDetails", jsonArr);

				wfDetails = jsonObj.toString();
				//log.info("wfDetails : {}", wfDetails);

				con = dc.getConnection("aem63app");
				AEM63WorkflowBean aem63wfBean = new AEM63WorkflowBean();

				aem63wfBean.setWfId(wfId);
				aem63wfBean.setWfName(wfName);
				aem63wfBean.setWfTitle(wfTitle);
				aem63wfBean.setWfInitiator(wfInitiator);
				aem63wfBean.setWfPayload(wfPayload);
				aem63wfBean.setWfStatTime(wfStartDate);
				aem63wfBean.setWfDetails(wfDetails);

				AEM63WorkflowDAO aem63wfDao = new AEM63WorkflowDAO();

				aem63wfDao.insertAEM63APPWorkflow(con, aem63wfBean);

			} catch (Exception re) {
				throw new WorkflowException(re.getMessage(), re);
			} finally {
				dc.closeConnection(con);
			}
		}
	}

	private String getWFInstancePath(String wfid) {
		String neWfid = wfid.replaceAll(".*(_etc_workflow_instances_)(.*)", "$1$2");
		String instid = neWfid.split(".*([0-9]{4}-[0-9]{2}-[0-9]{1,2}_)")[1];
		String newwfid = neWfid.split(instid)[0];
		newwfid = newwfid.replaceAll("_", "/");
		return newwfid + instid;
	}

}
