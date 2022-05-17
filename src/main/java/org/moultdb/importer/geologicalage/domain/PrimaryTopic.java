package org.moultdb.importer.geologicalage.domain;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.moultdb.importer.geologicalage.adapter.AlwaysListTypeAdapterFactory;

import java.util.List;

public class PrimaryTopic{

	@SerializedName("intervalStartedBy")
	private IntervalStartedBy intervalStartedBy;

	@SerializedName("intervalFinishedBy")
	private IntervalFinishedBy intervalFinishedBy;
	
	@JsonAdapter(AlwaysListTypeAdapterFactory.class)
	@SerializedName("intervalMeets")
	private List<IntervalMeetsItem> intervalMeets;

	@SerializedName("inScheme")
	private InScheme inScheme;

	@SerializedName("broaderTransitive")
	private List<BroaderTransitiveItem> broaderTransitive;
	
	@JsonAdapter(AlwaysListTypeAdapterFactory.class)
	@SerializedName("hasEnd")
	private HasEnd hasEnd;

	@SerializedName("intervalDuring")
	private IntervalDuring intervalDuring;
	
	@JsonAdapter(AlwaysListTypeAdapterFactory.class)
	@SerializedName("prefLabel")
	private List<PrefLabelItem> prefLabel;
	
	@JsonAdapter(AlwaysListTypeAdapterFactory.class)
	@SerializedName("intervalMetBy")
	private List<IntervalMetByItem> intervalMetBy;

	@SerializedName("narrower")
	private List<NarrowerItem> narrower;

	@SerializedName("altLabel")
	private List<AltLabelItem> altLabel;

	@SerializedName("label")
	private Label label;

	@SerializedName("type")
	private List<String> type;

	@SerializedName("primaryTopicOf")
	private String primaryTopicOf;

	@SerializedName("_about")
	private String about;

	@SerializedName("intervalIn")
	private IntervalIn intervalIn;

	@SerializedName("notation")
	private String notation;

	@SerializedName("rank")
	private String rank;

	@SerializedName("comment")
	private List<CommentItem> comment;

	@SerializedName("broader")
	private List<BroaderItem> broader;
	
	@JsonAdapter(AlwaysListTypeAdapterFactory.class)
	@SerializedName("hasBeginning")
	private List<HasBeginning> hasBeginning;

	@SerializedName("sameAs")
	private String sameAs;

	public IntervalStartedBy getIntervalStartedBy(){
		return intervalStartedBy;
	}

	public IntervalFinishedBy getIntervalFinishedBy(){
		return intervalFinishedBy;
	}

	public List<IntervalMeetsItem> getIntervalMeets(){
		return intervalMeets;
	}

	public InScheme getInScheme(){
		return inScheme;
	}

	public List<BroaderTransitiveItem> getBroaderTransitive(){
		return broaderTransitive;
	}

	public HasEnd getHasEnd(){
		return hasEnd;
	}

	public IntervalDuring getIntervalDuring(){
		return intervalDuring;
	}
	
	public List<PrefLabelItem> getPrefLabel(){
		return prefLabel;
	}

	public List<IntervalMetByItem> getIntervalMetBy(){
		return intervalMetBy;
	}

	public List<NarrowerItem> getNarrower(){
		return narrower;
	}

	public List<AltLabelItem> getAltLabel(){
		return altLabel;
	}

	public Label getLabel(){
		return label;
	}

	public List<String> getType(){
		return type;
	}

	public String getPrimaryTopicOf(){
		return primaryTopicOf;
	}

	public String getAbout(){
		return about;
	}

	public IntervalIn getIntervalIn(){
		return intervalIn;
	}

	public String getNotation(){
		return notation;
	}

	public String getRank(){
		return rank;
	}

	public List<CommentItem> getComment(){
		return comment;
	}

	public List<BroaderItem> getBroader(){
		return broader;
	}

	public List<HasBeginning> getHasBeginning(){
		return hasBeginning;
	}

	public String getSameAs(){
		return sameAs;
	}
}