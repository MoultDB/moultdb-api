package org.moultdb.importer.geologicalage.domain;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.moultdb.importer.geologicalage.adapter.AlwaysListTypeAdapterFactory;

import java.util.List;

public class HasEnd{
	
	@JsonAdapter(AlwaysListTypeAdapterFactory.class)
	@SerializedName("label")
	private List<Label> labels;

	@SerializedName("_about")
	private String about;

	public List<Label> getLabels(){
		return labels;
	}

	public String getAbout(){
		return about;
	}
}