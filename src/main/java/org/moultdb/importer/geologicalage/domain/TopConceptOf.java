package org.moultdb.importer.geologicalage.domain;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TopConceptOf{

	@SerializedName("component")
	private String component;

	@SerializedName("hasTopConcept")
	private List<String> hasTopConcept;

	@SerializedName("label")
	private Label label;

	@SerializedName("_about")
	private String about;

	public String getComponent(){
		return component;
	}

	public List<String> getHasTopConcept(){
		return hasTopConcept;
	}

	public Label getLabel(){
		return label;
	}

	public String getAbout(){
		return about;
	}
}