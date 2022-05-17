package org.moultdb.importer.geologicalage.domain;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Result {

	@SerializedName("primaryTopic")
	private PrimaryTopic primaryTopic;

	@SerializedName("extendedMetadataVersion")
	private String extendedMetadataVersion;

	@SerializedName("definition")
	private String definition;

	@SerializedName("type")
	private List<String> type;

	@SerializedName("_about")
	private String about;

	public PrimaryTopic getPrimaryTopic(){
		return primaryTopic;
	}

	public String getExtendedMetadataVersion(){
		return extendedMetadataVersion;
	}

	public String getDefinition(){
		return definition;
	}

	public List<String> getType(){
		return type;
	}

	public String getAbout(){
		return about;
	}
}