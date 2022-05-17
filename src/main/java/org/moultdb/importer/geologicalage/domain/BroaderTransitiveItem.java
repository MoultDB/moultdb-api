package org.moultdb.importer.geologicalage.domain;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BroaderTransitiveItem{

	@SerializedName("label")
	private Label label;

	@SerializedName("narrowerTransitive")
	private List<String> narrowerTransitive;

	@SerializedName("_about")
	private String about;

	public Label getLabel(){
		return label;
	}

	public List<String> getNarrowerTransitive(){
		return narrowerTransitive;
	}

	public String getAbout(){
		return about;
	}
}