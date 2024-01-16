package org.moultdb.importer.geologicalage.domain;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NarrowerItem{

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

	@SerializedName("intervalStarts")
	private String intervalStarts;

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

	public String getIntervalStarts(){
		return intervalStarts;
	}
}