package com.acc.aem64.core.servlets.pojo;

import java.util.ArrayList;
import java.util.List;

public class MetadataSchemaDropdownPojo {
	
	private List<Option> options = new ArrayList<Option>();
	
	public static class Option {
		private String text;
	    private String value;
	    
	    public Option(String text, String value){
			this.text = text;
			this.value = value;
		}
    }
	
	public void addOption(Option opt) {
		options.add(opt);
	}
}
