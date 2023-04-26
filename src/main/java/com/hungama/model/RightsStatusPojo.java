package com.hungama.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="tbl_content_rights_status")
@ToString
public class RightsStatusPojo 
{
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	@Column(name="content_id")
	private int content_id;
	
	@Column(name="retailer_id")
	private int retailer_id;
	
	@Column(name="rights_status")
	private String rights_status;
	
	
	public int getContent_id() {
		return content_id;
	}

	public void setContent_id(int content_id) {
		this.content_id = content_id;
	}

	public int getRetailer_id() {
		return retailer_id;
	}

	public void setRetailer_id(int retailer_id) {
		this.retailer_id = retailer_id;
	}

	public String getRights_status() {
		return rights_status;
	}

	public void setRights_status(String rights_status) {
		this.rights_status = rights_status;
	}

	public RightsStatusPojo(String contentId, String retailerId, String status) {
		super();
		// TODO Auto-generated constructor stub
	}
	
	


	

}
