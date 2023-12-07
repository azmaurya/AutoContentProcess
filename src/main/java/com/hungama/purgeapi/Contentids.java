package com.hungama.purgeapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contentids 
{
	
	
	@SerializedName("CONTENTID")
	@Expose
	private String contentid;
	@SerializedName("MDNREDIS")
	@Expose
	private String mdnredis;
	@SerializedName("TATACDN")
	@Expose
	private Object tatacdn;
	@SerializedName("AKMCDN")
	@Expose
	private Object akmcdn;
	@SerializedName("AKMDLS")
	@Expose
	private Object akmdls;
	@SerializedName("VerizonCDN")
	@Expose
	private String verizonCDN;
	public String getContentid() {
		return contentid;
	}
	public void setContentid(String contentid) {
		this.contentid = contentid;
	}
	public String getMdnredis() {
		return mdnredis;
	}
	public void setMdnredis(String mdnredis) {
		this.mdnredis = mdnredis;
	}
	public Object getTatacdn() {
		return tatacdn;
	}
	public void setTatacdn(Object tatacdn) {
		this.tatacdn = tatacdn;
	}
	public Object getAkmcdn() {
		return akmcdn;
	}
	public void setAkmcdn(Object akmcdn) {
		this.akmcdn = akmcdn;
	}
	public Object getAkmdls() {
		return akmdls;
	}
	public void setAkmdls(Object akmdls) {
		this.akmdls = akmdls;
	}
	public String getVerizonCDN() {
		return verizonCDN;
	}
	public void setVerizonCDN(String verizonCDN) {
		this.verizonCDN = verizonCDN;
	}
	
	
	
	
	
}
