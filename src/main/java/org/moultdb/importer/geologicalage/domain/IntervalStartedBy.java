package org.moultdb.importer.geologicalage.domain;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class IntervalStartedBy{

	@SerializedName("intervalIn")
	private String intervalIn;

	@SerializedName("intervalStarts")
	private String intervalStarts;

	@SerializedName("broader")
	private List<String> broader;

	@SerializedName("label")
	private Label label;

	@SerializedName("_about")
	private String about;

	public String getIntervalIn(){
		return intervalIn;
	}

	public String getIntervalStarts(){
		return intervalStarts;
	}

	public List<String> getBroader(){
		return broader;
	}

	public Label getLabel(){
		return label;
	}

	public String getAbout(){
		return about;
	}
}