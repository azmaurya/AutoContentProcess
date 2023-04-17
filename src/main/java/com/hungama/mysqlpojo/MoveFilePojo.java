package com.hungama.mysqlpojo;

import lombok.Getter;
import lombok.Setter;

public class MoveFilePojo {

	private long id;

	private String contentCode;

	private String sourcePath;

	private String contentType;

	private String contentSubType;

	private String type = "Raw Content";

	private String userid = "Content.DDEX";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContentCode() {
		return contentCode;
	}

	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentSubType() {
		return contentSubType;
	}

	public void setContentSubType(String contentSubType) {
		this.contentSubType = contentSubType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSourcebucket() {
		return sourcebucket;
	}

	public void setSourcebucket(String sourcebucket) {
		this.sourcebucket = sourcebucket;
	}

	private String sourcebucket = "storage.googleapis.com/ddexftp-hungama/nuemeta";
	// private String sourcebucket = "ddexftp.hungama.com/emi";

}
