package org.moultdb.importer.geologicalage.domain;

import com.google.gson.annotations.SerializedName;

public class IntervalMetByItem{

	@SerializedName("intervalMeets")
	private String intervalMeets;

	@SerializedName("label")
	private Label label;

	@SerializedName("_about")
	private String about;

	public String getIntervalMeets(){
		return intervalMeets;
	}

	public Label getLabel(){
		return label;
	}

	public String getAbout(){
		return about;
	}
}