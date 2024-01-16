package org.moultdb.importer.geologicalage.domain;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class IntervalFinishedBy{

	@SerializedName("intervalIn")
	private String intervalIn;

	@SerializedName("broader")
	private List<String> broader;

	@SerializedName("label")
	private Label label;

	@SerializedName("intervalFinishes")
	private String intervalFinishes;

	@SerializedName("_about")
	private String about;

	public String getIntervalIn(){
		return intervalIn;
	}

	public List<String> getBroader(){
		return broader;
	}

	public Label getLabel(){
		return label;
	}

	public String getIntervalFinishes(){
		return intervalFinishes;
	}

	public String getAbout(){
		return about;
	}
}