package org.moultdb.importer.geologicalage.domain;

import com.google.gson.annotations.SerializedName;

public class InScheme{

	@SerializedName("label")
	private Label label;

	@SerializedName("_about")
	private String about;

	public Label getLabel(){
		return label;
	}

	public String getAbout(){
		return about;
	}
}