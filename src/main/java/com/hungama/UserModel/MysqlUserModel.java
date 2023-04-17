package com.hungama.UserModel;

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
@Table(name ="TBL_TSERIES_S3_UPLOAD")
@ToString
public class MysqlUserModel
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	@Column(name="vendor")
	private String  vendor ;
	
	@Column(name="batchId")
	private String batchId;
	
	@Column(name="urlPath")
	private String urlPath;

}
