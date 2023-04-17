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
@Setter
@Getter
@Table(name ="TBL_contents")
@ToString
public class PosGreyUserModel
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	@Column(name="content_id")
	private int content_id;
	
	@Column(name="package_content_id")
	private int package_content_id;
	
	

}
