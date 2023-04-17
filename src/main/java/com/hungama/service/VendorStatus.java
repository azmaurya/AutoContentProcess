package com.hungama.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hungama.Repo.PosgreyRepository;
import com.hungama.Repository.MysqlRepository;
import com.hungama.TranscodingAndRightsAPICall.TranscodingAndRightsAPICall;
import com.hungama.fileMap.FileMapApiCall;
import com.hungama.model.RightsStatusPojo;
import com.hungama.mysqlpojo.MoveFilePojo;
import com.hungama.net.Http;

@Controller
@SpringBootApplication
public class VendorStatus {
	//public List<String> batchList = new ArrayList<String>();
	public int deleteRawfileIdCount;
	public String Album_id;
	public String Track_id;
	public List<String> status;
	public ArrayList<String> inputContentId = new ArrayList<String>();
	public int checkSqsRequestCount;
	public ArrayList<String> getAlbumIdWithContentId;
	public int getAlbumIdWithUpc;
	public List<String> RightsStatusContentId = null;
	Set<Integer> list = new HashSet<Integer>();
	Set<Integer> userInput = new HashSet<Integer>();
	Set<String> rightsList = new HashSet<String>();
	String[] sepratedcontentList = null;
	String contentId;
	String RetailerId;
	String Status;
	List<RightsStatusPojo> batchList = new ArrayList<RightsStatusPojo>();

	private static final Logger log = LogManager.getLogger(VendorStatus.class);

	Http http = new Http();

	@Autowired
	PosgreyRepository posgreyRepository;
	@Autowired
	MysqlRepository mysqlRepository;

	@RequestMapping(value = "/VendorStatus")
	public String ServiceController(@RequestParam long content_id, Model model,HttpServletRequest request ) throws Exception {
		TranscodingAndRightsAPICall transcodingAndRightsAPICall = new TranscodingAndRightsAPICall();

		log.info("content_id:->" + content_id);

		String flag = "fail";

		// Input id is UPC

		if (String.valueOf(content_id).length() > 9) {
			int upc_albumid = posgreyRepository.getAlbumTrackIdWithUPC(String.valueOf(content_id));
			log.info("upc_albumid:->" + upc_albumid);
			content_id = upc_albumid;
		}

		// Input id is content_id
		getAlbumIdWithContentId = posgreyRepository.getAlbumTrackContentId(content_id);

		log.info(" AlbumId " + getAlbumIdWithContentId.size());
		if (getAlbumIdWithContentId != null) {

			String[] sepratedListIdWithComma = null;

			for (String getAlbumId : getAlbumIdWithContentId) {

				sepratedListIdWithComma = getAlbumId.toString().split(",");

				list.add(Integer.parseInt(sepratedListIdWithComma[0]));
				list.add(Integer.parseInt(sepratedListIdWithComma[1]));

			}
			log.info("list size::" + list.size());

			try {

				String contentCode = "", ddexVendor = "";
				String dbDetails = null;

				inputContentId = posgreyRepository.findByContentCode(content_id);

				log.info("inputContentId " + inputContentId);
				String checkIsDDEX = null;
				String contentDetailsTest[] = inputContentId.get(0).split(",");

				if (contentDetailsTest.length > 0) {
					ddexVendor = contentDetailsTest[2];
					checkIsDDEX = contentDetailsTest[3];
					contentCode = contentDetailsTest[4];

				}
				if (checkIsDDEX.equalsIgnoreCase("Content.DDEX")) {

					switch (ddexVendor) {
					case "The Orchard Enterprises":
						status = mysqlRepository.getOrchardFileMapping(contentCode);
						break;
					case "Zee Music Company_Hungama":
						status = mysqlRepository.getFugaFileMapping(contentCode);
						break;
					case "Believe SAS":
						status = mysqlRepository.getBeliveFileMapping(contentCode);
						break;
					case "Universal Music Group":
						status = mysqlRepository.getUniversalFileMapping(contentCode);
						break;
					case "ERIK Business Consultancy Services LLP":
						status = mysqlRepository.geterikFileMapping(contentCode);
						break;
					case "Saregama India Ltd":
						status = mysqlRepository.getSaregamaFileMapping(contentCode);
						break;
					case "T-Series":
						status = mysqlRepository.getTSeriseFileMapping(contentCode);
						break;
					case "Warner Music Group":
						status = mysqlRepository.getWarnerFileMapping(contentCode);
						break;

					}

					log.info("contentCode " + contentCode);
					log.info("dbDetails " + dbDetails);
					log.info("ddexVendor " + ddexVendor);

					// status = mysqlRepository.getddexfilemappingstatus(contentCode);
					log.info("status " + status.size());

					if (status.size() > 0) {
						List<MoveFilePojo> list = new ArrayList<>();

						for (int i = 0; i < status.size(); i++) {
							MoveFilePojo moveFilePojo = new MoveFilePojo();

							String[] sArr = status.get(i).split(",");
							log.info("status " + sArr[0]);

							moveFilePojo.setId(Integer.parseInt(sArr[0]));
							moveFilePojo.setContentCode(sArr[1]);
							moveFilePojo.setSourcePath(sArr[2]);
							moveFilePojo.setContentType(sArr[3]);
							moveFilePojo.setContentSubType(sArr[4]);

							list.add(moveFilePojo);

						}
						for (MoveFilePojo moveFilePojo1 : list) {
							String FileMapApiCallSuccess = FileMapApiCall.call(moveFilePojo1);
							log.info(" FileMapApiCall status " + FileMapApiCallSuccess);

							long fileId = moveFilePojo1.getId();
							if (FileMapApiCallSuccess.equals("Success")) {

								switch (ddexVendor) {
								case "The Orchard Enterprises":
									log.info("fileId " + fileId);
									int deleteRawfileIdCount = mysqlRepository.getOrchardFileDelete((fileId));
									log.info("deletefileIdCount " + deleteRawfileIdCount);

									break;
								case "Zee Music Company_Hungama":
									log.info("fileId " + fileId);
									int getFugaFileDelete = mysqlRepository.getOrchardFileDelete((fileId));
									log.info("deletefileIdCount " + getFugaFileDelete);

									break;
								case "Believe SAS":
									log.info("fileId " + fileId);
									int getBeliveFileMapping = mysqlRepository.getOrchardFileDelete((fileId));
									log.info("deletefileIdCount " + getBeliveFileMapping);

									break;
								case "Universal Music Group":
									log.info("fileId " + fileId);
									int getUniversalFileDelete = mysqlRepository.getOrchardFileDelete((fileId));
									log.info("deletefileIdCount " + getUniversalFileDelete);

									break;

								case "ERIK Business Consultancy Services LLP":
									log.info("fileId " + fileId);
									int geterikFileDelete = mysqlRepository.geterikFileDelete((fileId));
									log.info("deletefileIdCount " + geterikFileDelete);

									break;

								case "Saregama India Ltd":
									log.info("fileId " + fileId);
									int getsaregamaFileDelete = mysqlRepository.geterikFileDelete((fileId));
									log.info("deletefileIdCount " + getsaregamaFileDelete);

									break;

								case "T-Series":
									log.info("fileId " + fileId);
									int getTSeriseFileDelete = mysqlRepository.geterikFileDelete((fileId));
									log.info("deletefileIdCount " + getTSeriseFileDelete);

									break;

								case "Warner Music Group":
									log.info("fileId " + fileId);
									int getWarnerFileDelete = mysqlRepository.geterikFileDelete((fileId));
									log.info("deletefileIdCount " + getWarnerFileDelete);

									break;

								}

							}

						}

					}
				}
				try {
					if (list.size() > 0) {

						String transcodingSuccess = transcodingAndRightsAPICall.dotranscode(list);

						// Thread.sleep(300000);

						checkSqsRequestCount = posgreyRepository.TranscodingSqsRequestCheck(list);
						log.info("Transcoding Api Call Successfully");
						log.info("checkSqsRequestCount " + checkSqsRequestCount);

						if (checkSqsRequestCount == 0) {
							String rightsAssignmentSuccess = transcodingAndRightsAPICall.doRightsAssigment(list);
							log.info("Rights Api Call Successfully");

							RightsStatusContentId = posgreyRepository.RightsStatusCheck(list);
							log.info("RightsStatusContentId " + RightsStatusContentId.size());

							for (String getRightsdetails : RightsStatusContentId) {

								sepratedcontentList = getRightsdetails.split(",");

								 contentId = sepratedcontentList[0];
								 RetailerId = sepratedcontentList[1];
								 Status = sepratedcontentList[2];

								   
								   // model.addAttribute("contentId", contentId);
									//model.addAttribute("RetailerId", RetailerId);
									//model.addAttribute("Status", Status);
									//model.addAttribute("myString", myString);
								    
								   // RightsStatusPojo pojo=new RightsStatusPojo();
								  //  pojo.setContent_id(Integer.parseInt(contentId));
								  //  pojo.setRetailer_id(Integer.parseInt(RetailerId));
								   // pojo.setRights_status(Status);
								    
								//	model.addAttribute(pojo);

									String message = "Hello, world!";
									model.addAttribute("message", message);
								    
									log.info("contentId :-" + contentId);
									log.info("RetailerId :-" + RetailerId);
									log.info("Status :-" + Status);
									
															
							}
							
							return "VendorRightsStatus";		
							

						

						}

					}

					log.info("Program Executed Successfully");
					flag = "Success";
					return "Success";

				} catch (Exception e) {
					e.printStackTrace();
					flag = "fail";
				}

			} catch (NumberFormatException n) {
				n.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// }
		}

		// }

		return flag;

	}

}
