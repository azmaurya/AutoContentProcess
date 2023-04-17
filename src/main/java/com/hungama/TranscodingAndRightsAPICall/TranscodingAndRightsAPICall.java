package com.hungama.TranscodingAndRightsAPICall;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hungama.net.Http;
import com.hungama.net.HttpException;
import com.hungama.net.HttpResponse;
import com.hungama.service.VendorStatus;



public class TranscodingAndRightsAPICall {
	  private static final Logger log = LogManager.getLogger(VendorStatus.class);


	public  String dotranscode(Set<Integer> content_id) throws HttpException, IOException {
		Http http = new Http();
		// calling Transcoding API
		String TranscodingAPI = "http://34.86.122.205:90/api/Service/Transcoding?ContentId=" + content_id.toString().replaceAll("(^\\[|\\]$)", "")+ "";

		log.info("TranscodingAPI " + TranscodingAPI);

		HttpResponse response = http.get(TranscodingAPI);

		String responseStr = response.getResponseAsString();
		if (response.getStatusCode() == 200) {

			return "Transcoding Success";

		}

		else {

			return "Transcoding Faild";

		}

	}

	public String doRightsAssigment(Set<Integer> content_id) throws HttpException, IOException {
		Http http = new Http();
		// calling Transcoding API
		String RightsApi = "http://34.86.122.205:8080/RightsAssign/Create?contentId=" + content_id.toString().replaceAll("(^\\[|\\]$)", "") + "";

		log.info("RightsApi " + RightsApi);

		HttpResponse response = http.get(RightsApi);

		String responseStr = response.getResponseAsString();
		if (response.getStatusCode() == 200) {

			return "Rights assigned Success";

		}

		else {

			return "Rights assigned Faild";

		}

	}

}
