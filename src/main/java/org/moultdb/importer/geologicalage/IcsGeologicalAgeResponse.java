package org.moultdb.importer.geologicalage;

import com.google.gson.annotations.SerializedName;
import org.moultdb.importer.geologicalage.domain.Result;

public class IcsGeologicalAgeResponse{

	@SerializedName("result")
	private Result result;

	@SerializedName("format")
	private String format;

	@SerializedName("version")
	private String version;

	public Result getResult(){
		return result;
	}

	public String getFormat(){
		return format;
	}

	public String getVersion(){
		return version;
	}
}