package org.moultdb.importer.geologicalage.domain;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class IntervalContains{

	@SerializedName("intervalIn")
	private String intervalIn;

	@SerializedName("intervalDuring")
	private String intervalDuring;

	@SerializedName("broader")
	private List<String> broader;

	@SerializedName("label")
	private Label label;

	@SerializedName("_about")
	private String about;

	public String getIntervalIn(){
		return intervalIn;
	}

	public String getIntervalDuring(){
		return intervalDuring;
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