package com.hungama.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hungama.Repo.PosgreyRepository;
import com.hungama.TranscodingAndRightsAPICall.TranscodingAndRightsAPICall;
import com.hungama.net.HttpException;
import com.hungama.purgeApi.PurgeApiCall;

@Controller
@SpringBootApplication
public class TranscodingAndPurging {

	public Set<Integer> list = new HashSet<Integer>();
	public static String transcodingSuccess;
	private static final Logger log = LogManager.getLogger(TranscodingAndPurging.class);
	public static String retunrJson;
	TranscodingAndRightsAPICall transcodingAndRightsAPICall = new TranscodingAndRightsAPICall();
	PurgeApiCall purgeApiCall = new PurgeApiCall();
	public static int checkSqsRequestCount = 0;
	public static ArrayList<String> requestStatusCheckList = new ArrayList<String>();

	@Autowired
	PosgreyRepository posgreyRepository;
	

	@RequestMapping(path = "/TranscodingAndPurging")
	public String purgecontroller(@RequestParam(required = false) Set<Integer> list, Model model)
			throws HttpException, IOException, InterruptedException, ParseException

	{
		log.info("transcodeAndPurge_list " + list);
		checkSqsRequestCount = posgreyRepository.TranscodingSqsRequestCheck(list);
		List<Integer> integerList = new ArrayList<>(list);

		 requestStatusCheckList = posgreyRepository.requestStatus(integerList);

			while (posgreyRepository.TranscodingSqsRequestCheck(list) > 0) 
			{
				//requestStatusCheckList = posgreyRepository.requestStatus(arr);
				for (int i = 0; posgreyRepository.requestStatus(integerList).size() > i; i++) 
				{
					
					if (posgreyRepository.requestStatus(integerList).size() > 0) 
					{

						if (posgreyRepository.requestStatus(integerList).size() > 0 && posgreyRepository.requestStatus(integerList).get(i).contains("FAILED")) {
							log.info("requestStatusCheckList:FAILED: "
									+ posgreyRepository.requestStatus(integerList).get(i).split(",")[0]);
							int callingUpdateQuery_Faild = posgreyRepository.updatecount_faild(
									Long.valueOf(posgreyRepository.requestStatus(integerList).get(i).split(",")[0]));
							log.info("callingUpdateQuery_Faild  " + callingUpdateQuery_Faild);
							transcodingSuccess = transcodingAndRightsAPICall.dotranscode(list);
							Thread.sleep(120000);

						}
						if (posgreyRepository.requestStatus(integerList).size() > 0 && posgreyRepository.requestStatus(integerList).get(i).contains("QUEUED")) {
							log.info("requestStatusCheckList;QUEUED: "
									+ posgreyRepository.requestStatus(integerList).get(i).split(",")[0]);
							transcodingSuccess = transcodingAndRightsAPICall.dotranscode(list);
							Thread.sleep(120000);
						}
					} 
				}
			}
		if ((posgreyRepository.TranscodingSqsRequestCheck(list) == 0)) 
		{
			retunrJson = purgeApiCall.dopurge(list);

			model.addAttribute("sucessjson", retunrJson);

			return "UPC_delivered_status";
		}

		return null;
	}

}
