package com.hungama.purgeapi;

import java.io.IOException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.hungama.net.Http;
import com.hungama.net.HttpException;
import com.hungama.net.HttpResponse;
import com.hungama.service.VendorStatus;


public class PurgeApiCall
{
	@Autowired
	PurgePojo pojo;
	private static final Logger log = LogManager.getLogger(VendorStatus.class);
	public  String dopurge(Set<Integer> content_id) throws HttpException, IOException {
		Http http = new Http();
		// calling Transcoding API
		String purgeApi = "http://metasea-api.hungama.com/PurgeFiles/" + content_id.toString().replaceAll("(^\\[|\\]$)", "")+ "";

		log.info("PurgeApi " + purgeApi);

		HttpResponse response = http.get(purgeApi);

		String responseStr = response.getResponseAsString();
		
		if (response.getStatusCode() == 200) 
		{
			
			pojo= new Gson().fromJson(responseStr,PurgePojo.class);
			
			log.info("puregpojo "+pojo.getContentids().get(0).getVerizonCDN());
			
			return pojo.getContentids().get(0).getVerizonCDN();

		}

		else {

			return "purge Faild";

		}

	}


}
