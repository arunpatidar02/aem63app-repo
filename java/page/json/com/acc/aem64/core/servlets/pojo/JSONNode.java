package com.acc.aem64.core.servlets.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONNode {
	private String name = null;
	private HashMap<String, Object> properties = null;
	private List<JSONNode> childnodes = new ArrayList<JSONNode>();

	public JSONNode(String nodename, HashMap<String, Object> properties) {
		this.name = nodename;
		if(!properties.isEmpty()) {
			this.properties = properties;
		}
	}

	public JSONNode addChild(String nodename, HashMap<String, Object> value) {
		JSONNode newChild = new JSONNode(nodename, value);
		childnodes.add(newChild);
		return newChild;
	}

}
