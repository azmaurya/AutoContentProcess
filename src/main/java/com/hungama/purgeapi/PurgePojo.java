package com.hungama.purgeapi;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurgePojo
{

	@SerializedName("CONTENTIDS")
	@Expose
	private List<Contentids> contentids;

	public List<Contentids> getContentids() {
	return contentids;
	}

	public void setContentids(List<Contentids> contentids) {
	this.contentids = contentids;
	}
	
}
