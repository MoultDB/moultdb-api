package org.moultdb.importer.geologicalage.domain;

import com.google.gson.annotations.SerializedName;

public class IntervalMeetsItem{

	@SerializedName("intervalMetBy")
	private String intervalMetBy;

	@SerializedName("label")
	private Label label;

	@SerializedName("_about")
	private String about;

	public String getIntervalMetBy(){
		return intervalMetBy;
	}

	public Label getLabel(){
		return label;
	}

	public String getAbout(){
		return about;
	}
}