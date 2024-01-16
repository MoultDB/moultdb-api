package org.moultdb.importer.geologicalage.domain;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BroaderItem{

	@SerializedName("narrower")
	private List<String> narrower;

	@SerializedName("intervalContains")
	private String intervalContains;

	@SerializedName("label")
	private Label label;

	@SerializedName("_about")
	private String about;

	public List<String> getNarrower(){
		return narrower;
	}

	public String getIntervalContains(){
		return intervalContains;
	}

	public Label getLabel(){
		return label;
	}

	public String getAbout(){
		return about;
	}
}