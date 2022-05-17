package org.moultdb.importer.geologicalage.domain;

import com.google.gson.annotations.SerializedName;

public class PrefLabelItem{

	@SerializedName("_lang")
	private String lang;

	@SerializedName("_value")
	private String value;
	
	public PrefLabelItem(String lang, String value) {
		this.lang = lang;
		this.value = value;
	}
	
	public String getLang(){
		return lang;
	}

	public String getValue(){
		return value;
	}
}