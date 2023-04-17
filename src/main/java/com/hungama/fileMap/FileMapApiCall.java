package com.hungama.fileMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.hungama.Repository.MysqlRepository;
import com.hungama.mysqlpojo.MoveFilePojo;
import com.hungama.net.Http;
import com.hungama.net.HttpResponse;
import com.hungama.service.VendorStatus;

public class FileMapApiCall {

	private static final String URL = "http://ddexfile.hungama.com/SonyMusicFileMovementService.svc/CheckContent";
    //private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	private static final Gson gson = new Gson();
	 private static final Logger log = LogManager.getLogger(VendorStatus.class);


	@Autowired
	MysqlRepository mysqlRepository;

	public static String call(MoveFilePojo moveFilePojo) throws Exception {

		String status = "fail";

		Http http = new Http();

		long startTime = System.currentTimeMillis();

		log.info("MoveFile Json=" + gson.toJson(moveFilePojo).toString().replaceAll("(^\\[|\\]$)", ""));

		try (HttpResponse response = http.get(URL, gson.toJson(moveFilePojo).toString().replaceAll("(^\\[|\\]$)", ""),
				"application/json")) {
			String responseStr = response.getResponseAsString();
			log.info(gson.toJson(moveFilePojo.toString().replaceAll("(^\\[|\\]$)", "")) + "--" + responseStr
					+ "--" + (System.currentTimeMillis() - startTime));

			if (response.getStatusCode() == 200 && responseStr.contains(">OK</HttpStatusCode>")) {

				return "Success";
			}

			else {

				return "Faild";
			}

		}

	}

}
