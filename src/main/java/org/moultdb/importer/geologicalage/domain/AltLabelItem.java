package org.moultdb.importer.geologicalage.domain;

import com.google.gson.annotations.SerializedName;

public class AltLabelItem{

	@SerializedName("_lang")
	private String lang;

	@SerializedName("_value")
	private String value;

	public String getLang(){
		return lang;
	}

	public String getValue(){
		return value;
	}
}