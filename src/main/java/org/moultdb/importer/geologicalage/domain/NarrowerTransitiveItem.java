package org.moultdb.importer.geologicalage.domain;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NarrowerTransitiveItem{

	@SerializedName("broaderTransitive")
	private List<String> broaderTransitive;

	@SerializedName("label")
	private Label label;

	@SerializedName("_about")
	private String about;

	public List<String> getBroaderTransitive(){
		return broaderTransitive;
	}

	public Label getLabel(){
		return label;
	}

	public String getAbout(){
		return about;
	}
}