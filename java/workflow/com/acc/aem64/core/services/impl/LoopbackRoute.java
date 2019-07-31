package com.acc.aem64.core.services.impl;

import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowNode;
import com.adobe.granite.workflow.model.WorkflowTransition;
import java.util.ArrayList;
import java.util.List;

class LoopbackRoute
  implements Route
{
  private WorkItem item;
  private List<WorkflowTransition> transitions = new ArrayList();
  
  LoopbackRoute(WorkItem item)
  {
    this.item = item;
    
    this.transitions.add(new WorkflowTransition()
    {
      public WorkflowNode getFrom()
      {
        return LoopbackRoute.this.item.getNode();
      }
      
      public void setFrom(WorkflowNode workflowNode)
      {
        throw new UnsupportedOperationException("operation not supported in loopback route");
      }
      
      public WorkflowNode getTo()
      {
        return LoopbackRoute.this.item.getNode();
      }
      
      public void setTo(WorkflowNode workflowNode)
      {
        throw new UnsupportedOperationException("operation not supported in loopback route");
      }
      
      public String getRule()
      {
        return null;
      }
      
      public void setRule(String s)
      {
        throw new UnsupportedOperationException("operation not supported in loopback route");
      }
      
      public MetaDataMap getMetaDataMap()
      {
        return null;
      }
    });
  }
  
  public String getId()
  {
    String result = "";
    for (WorkflowTransition transition : this.transitions)
    {
      result = result + transition.getFrom().getId();
      result = result + transition.getTo().getId();
    }
    return String.valueOf(result.hashCode());
  }
  
  public String getName()
  {
    String result = "";
    String separator = "";
    for (WorkflowTransition transition : this.transitions)
    {
      result = result + separator + transition.getTo().getTitle();
      separator = " & ";
    }
    return result;
  }
  
  public boolean hasDefault()
  {
    return false;
  }
  
  public List<WorkflowTransition> getDestinations()
  {
    return this.transitions;
  }
  
  public boolean isBackRoute()
  {
    return false;
  }
}
