package com.hungama.service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.hungama.Repo.PosgreyRepository;
import com.hungama.Repository.MysqlRepository;
import com.hungama.TranscodingAndRightsAPICall.TranscodingAndRightsAPICall;
import com.hungama.filemap.FileMapApiCall;
import com.hungama.mysqlpojo.MoveFilePojo;

@Controller
@SpringBootApplication
public class VendorStatus {

	public String[] sepratedListIdWithComma = null;
	public String Album_id;
	public String Track_id;
	public List<String> status;
	public ArrayList<String> inputContentId = new ArrayList<String>();
	public int checkSqsRequestCount;
	public ArrayList<String> getAlbumIdWithContentId;
	public ArrayList<String> getTrackContentid;
	public int getAlbumIdWithUpc;
	public List<String[]> rightsStatusContents = null;
	public List<String[]> rightsStatusPending = null;
	public Set<String> rightsList = new HashSet<String>();
	public String[] sepratedcontentList = null;
	private static final Logger log = LogManager.getLogger(VendorStatus.class);
	public static String transcodingSuccess;
	public static Set<Integer> bulkTranscode;
	String timesleep = null;
	String[] multipleBatch = null;

	@Autowired
	PosgreyRepository posgreyRepository;
	@Autowired
	MysqlRepository mysqlRepository;

	@RequestMapping(value = "/VendorStatus")
	public String ServiceController(@RequestParam String content_id, Model model) throws Exception {
		TranscodingAndRightsAPICall transcodingAndRightsAPICall = new TranscodingAndRightsAPICall();
		PendingToQueued pendingToQueued = new PendingToQueued();

		Set<Integer> getcontentlist = new HashSet<Integer>();

		log.info(content_id);
		String UPC = null;

		String flag = "fail";
		List<Integer> contentIds = new ArrayList<>();
		String[] arrOfStr = null;
		String upc_albumidfrombatch = null;
		List<String> UrlpathFrombatch = null;
		List<String> stringList = new ArrayList<>();

		// input id is batchId
		if (content_id.contains("2023") || content_id.contains("2024")) {
			log.info("content_id " + content_id);

			String[] multipleBatch = content_id.split(",");
			for (String batch : multipleBatch) {
				log.info("Batch: " + batch);

				// log.info("multipleBatch[i] "+multipleBatch[i]);

				UrlpathFrombatch = mysqlRepository.getUPCFromBatchId(String.valueOf(batch));

				log.info("UrlpathFrombatch " + UrlpathFrombatch);
				log.info("UrlpathFrombatch " + UrlpathFrombatch.size());

				String[] urls = UrlpathFrombatch.toString().split(", ");
				for (String url : urls) {
					log.info("url " + url);
					stringList.add(url.trim());
				}
				log.info("Number of elements in stringList: " + stringList.size());

				UrlpathFrombatch.stream();

			}

			if (UrlpathFrombatch == null) {
				String msg = content_id + ".Batch has not delivered";
				model.addAttribute("message", msg);
				return "UPC_delivered_status";
			}

			// List<String> listupc = new ArrayList<String>();

			for (int i = 0; i < stringList.size(); i++) {
				arrOfStr = stringList.get(i).toString().split("/");
				log.info("UPC " + arrOfStr[6]);

				upc_albumidfrombatch = posgreyRepository.getAlbumTrackIdWithbatch(String.valueOf(arrOfStr[6]));
				contentIds.add(Integer.parseInt(upc_albumidfrombatch));
			}
		}
		// Input id is UPC

		else if (String.valueOf(content_id).length() > 9
				&& !(content_id.contains("2023") || content_id.contains("2024") || content_id.contains(","))) {
			Integer upc_albumid = null;
			upc_albumid = posgreyRepository.getAlbumTrackIdWithUPC(String.valueOf(content_id));
			/// int upc_albumid = posgreyRepository.getAlbumTrackIdWithUPC(content_id);
			String s = "UPC not delivered";
			if (upc_albumid == null) {
				String msg = content_id + ". Upc has not delivered";
				model.addAttribute("message", msg);
				return "UPC_delivered_status";
			}

			log.info("upc_albumid:->" + upc_albumid);
			content_id = String.valueOf(upc_albumid);
			log.info("content_id  " + content_id);
		}

		else if (content_id.contains(",")) {
			List<Integer> list1 = Arrays.asList(content_id.split(",")).stream().map(s -> Integer.parseInt(s.trim()))
					.collect(Collectors.toList());
			bulkTranscode = new HashSet<>(list1);

			List<Integer> arr = new ArrayList<>(list1);
			// requestStatusCheckList = posgreyRepository.requestStatus(arr);
			while (posgreyRepository.TranscodingSqsRequestBulkCheck(list1) > 0) {
				// requestStatusCheckList = posgreyRepository.requestStatus(arr);
				for (int i = 0; posgreyRepository.requestStatus(arr).size() > i; i++) {

					if (posgreyRepository.requestStatus(arr).get(i).contains("PENDING")) 
					{
						// Set<Integer> targetSet = new HashSet<>(arr);

						int callingUpdateQuery_pending = posgreyRepository.updatecount_pending(
								Long.valueOf((posgreyRepository.requestStatus(arr).get(i).split(",")[0])));

						pendingToQueued.pendingToQue(arr);

					}

					if (posgreyRepository.requestStatus(arr).size() > 0) {

						if (posgreyRepository.requestStatus(arr).size() > 0
								&& posgreyRepository.requestStatus(arr).get(i).contains("FAILED")) {
							log.info("requestStatusCheckList:FAILED: "
									+ posgreyRepository.requestStatus(arr).get(i).split(",")[0]);
							int callingUpdateQuery_Faild = posgreyRepository.Bulkupdatecount_faild(arr);
							log.info("callingUpdateQuery_Faild  " + callingUpdateQuery_Faild);
							transcodingSuccess = transcodingAndRightsAPICall.dotranscode(bulkTranscode);
							Thread.sleep(120000);

						}
						if (posgreyRepository.requestStatus(arr).size() > 0
								&& posgreyRepository.requestStatus(arr).get(i).contains("QUEUED")) {
							log.info("requestStatusCheckList;QUEUED: "
									+ posgreyRepository.requestStatus(arr).get(i).split(",")[0]);
							transcodingSuccess = transcodingAndRightsAPICall.dotranscode(bulkTranscode);
							Thread.sleep(120000);
						}
					}

				}
			}

			if (posgreyRepository.TranscodingSqsRequestCheck(bulkTranscode) == 0) {
				log.info("Rights Api Calling");
				String apiStatus = transcodingAndRightsAPICall.doRightsAssigment(bulkTranscode);
				log.info("apiStatus" + apiStatus);
				rightsStatusContents = posgreyRepository.RightsStatusCheck(bulkTranscode);
				model.addAttribute("rightsStatusContents", rightsStatusContents);
				return "VendorRightsStatus";
			}

		}

		// Input id is content_id

		// List<Long> myList = List.of(Long.valueOf(content_id));

		// getAlbumIdWithContentId =
		// posgreyRepository.getAlbumTrackContentId(Long.valueOf(content_id));
		// log.info(" getAlbumIdWithContentId " + getAlbumIdWithContentId);

		if (content_id.contains("2023") || content_id.contains("2024")) {
			getAlbumIdWithContentId = posgreyRepository.getAlbumTrackContentIdFortseries(contentIds);
			log.info(" getAlbumIdWithContentId " + getAlbumIdWithContentId);

			inputContentId = posgreyRepository.findByContentCode(Long.valueOf(upc_albumidfrombatch));

		} else {
			getAlbumIdWithContentId = posgreyRepository.getAlbumTrackContentId(Long.valueOf(content_id));
			log.info(" getAlbumIdWithContentId " + getAlbumIdWithContentId);

			inputContentId = posgreyRepository.findByContentCode(Long.valueOf(content_id));
		}

		if (getAlbumIdWithContentId.isEmpty()) {
			getTrackContentid = posgreyRepository.getTrackAlbumContentId(Long.valueOf(content_id));
			log.info(" getTrackContentid " + getTrackContentid);

			if (getTrackContentid != null) {

				for (String getAlbumId : getTrackContentid) {

					sepratedListIdWithComma = getAlbumId.toString().split(",");
					getcontentlist.add(Integer.parseInt(sepratedListIdWithComma[0]));
					getcontentlist.add(Integer.parseInt(sepratedListIdWithComma[1]));

				}

				log.info("getTrackContentidlist size::" + getcontentlist.size());
			}

		}

		if (getAlbumIdWithContentId != null) {

			for (String getAlbumId : getAlbumIdWithContentId) {

				sepratedListIdWithComma = getAlbumId.toString().split(",");
				getcontentlist.add(Integer.parseInt(sepratedListIdWithComma[0]));
				getcontentlist.add(Integer.parseInt(sepratedListIdWithComma[1]));

			}
			log.info("getAlbumIdWithContentIdlist size::" + getcontentlist.size());
		}

		try {

			String contentCode = "", ddexVendor = "";
			String dbDetails = null;

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
				case "IIP-DDS B.V":
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
				case "Phonographic Digital Limited_Hungama":
					status = mysqlRepository.getPhonographicFileMapping(contentCode);
					break;

				case "DPM Network Music Distribution":
					status = mysqlRepository.getDPMFileMapping(contentCode);
					break;

				case "Zee Music Company_Hungama":
					status = mysqlRepository.getzeelFileMapping(contentCode);
					break;
				case "One Digital Entertainment Pvt Ltd":
					status = mysqlRepository.getonedigitalFileMapping(contentCode);
					break;
				case "Sony Music":
					status = mysqlRepository.getSonyFileMapping(contentCode);
					break;
				case "Audio and Video Labs Inc. d/b/a CD Baby":
					status = mysqlRepository.getcdbabyFileMapping(contentCode);
					break;
				case "INgrooves":
					status = mysqlRepository.getINgroovesFileMapping(contentCode);
					break;
				case "Sanjivani Digital Entertainment Pvt Ltd":
					status = mysqlRepository.getSanjivaniFileMapping(contentCode);
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
								int getzeelFileDelete = mysqlRepository.getzeelFileDelete((fileId));
								log.info("deletefileIdCount " + getzeelFileDelete);
								break;

							case "Believe SAS":
								log.info("fileId " + fileId);
								int getBeliveFileDelete = mysqlRepository.getBeliveFileDelete((fileId));
								log.info("deletefileIdCount " + getBeliveFileDelete);

								break;
							case "Universal Music Group":
								log.info("fileId " + fileId);
								int getUniversalFileDelete = mysqlRepository.getUniversalFileDelete((fileId));
								log.info("deletefileIdCount " + getUniversalFileDelete);

								break;

							case "ERIK Business Consultancy Services LLP":
								log.info("fileId " + fileId);
								int geterikFileDelete = mysqlRepository.geterikFileDelete((fileId));
								log.info("deletefileIdCount " + geterikFileDelete);

								break;

							case "Saregama India Ltd":
								log.info("fileId " + fileId);
								int getsaregamaFileDelete = mysqlRepository.getSaregamaFileDelete((fileId));
								log.info("deletefileIdCount " + getsaregamaFileDelete);

								break;

							case "T-Series":
								log.info("fileId " + fileId);
								int getTSeriseFileDelete = mysqlRepository.getTSeriseFileDelete((fileId));
								log.info("deletefileIdCount " + getTSeriseFileDelete);

								break;

							case "Warner Music Group":
								log.info("fileId " + fileId);
								int getWarnerFileDelete = mysqlRepository.getWarnerFileDelete((fileId));
								log.info("deletefileIdCount " + getWarnerFileDelete);

								break;

							case "Phonographic Digital Limited_Hungama":
								log.info("fileId " + fileId);
								int getPhonographicFileDelete = mysqlRepository.getPhonographicFileDelete((fileId));
								log.info("deletefileIdCount " + getPhonographicFileDelete);

								break;

							case "DPM Network Music Distribution":
								log.info("fileId " + fileId);
								int getDPMFileDelete = mysqlRepository.getDPMFileDelete((fileId));
								log.info("deletefileIdCount " + getDPMFileDelete);
								break;

							case "IIP-DDS B.V":
								log.info("fileId " + fileId);
								int getFugaFileDelete = mysqlRepository.getFugaFileDelete((fileId));
								log.info("deletefileIdCount " + getFugaFileDelete);
								break;

							case "One Digital Entertainment Pvt Ltd":
								log.info("fileId " + fileId);
								int getonedigitalFileDelete = mysqlRepository.getonedigitalDelete((fileId));
								log.info("deletefileIdCount " + getonedigitalFileDelete);
								break;

							case "Sony Music":
								log.info("fileId " + fileId);
								int getsonyFileDelete = mysqlRepository.getsonyDelete((fileId));
								log.info("deletefileIdCount " + getsonyFileDelete);
								break;

							case "Audio and Video Labs Inc. d/b/a CD Baby":
								log.info("fileId " + fileId);
								int getcdbabyFileDelete = mysqlRepository.getcdbayDelete((fileId));
								log.info("deletefileIdCount " + getcdbabyFileDelete);
								break;

							case "INgrooves":
								log.info("fileId " + fileId);
								int getINgroovesDelete = mysqlRepository.getINgroovesDelete((fileId));
								log.info("deletefileIdCount " + getINgroovesDelete);
								break;
								
							case "Sanjivani Digital Entertainment Pvt Ltd":
								log.info("fileId " + fileId);
								int getSanjivaniDelete = mysqlRepository.getSanjivaniDelete((fileId));
								log.info("deletefileIdCount " + getSanjivaniDelete);
								break;

							}

						}

					}

				}
			}
			try {
				if (getAlbumIdWithContentId.size() > 0 || getTrackContentid.size() > 0) {
					// checkSqsRequestCount = posgreyRepository.TranscodingSqsRequestCheck(list);

					List<Integer> arr = new ArrayList<>(getcontentlist);
					// requestStatusCheckList = posgreyRepository.requestStatus(arr);

					for (int i = 0; posgreyRepository.requestStatus(arr).size() > i; i++) 
					{
						if (posgreyRepository.requestStatus(arr).get(i).contains("PENDING")) 
						{
							// Set<Integer> targetSet = new HashSet<>(arr);

							int callingUpdateQuery_pending = posgreyRepository.updatecount_pending(
									Long.valueOf((posgreyRepository.requestStatus(arr).get(i).split(",")[0])));

							pendingToQueued.pendingToQue(arr);

						}

						// rightsStatusPending = posgreyRepository.requestStatuspending(arr);

						if (posgreyRepository.requestStatus(arr).size() > 0
								&& posgreyRepository.requestStatus(arr).get(i).contains("FAILED")) {
							int callingUpdateQuery_Faild = posgreyRepository.updatecount_faild(
									Long.valueOf(posgreyRepository.requestStatus(arr).get(i).split(",")[0]));
						}
						// transcodingSuccess = transcodingAndRightsAPICall.dotranscode(targetSet);
						// return "RightsStatusPending";
						// log.info("requestStatusCheckList:PENDING: " +
						// posgreyRepository.requestStatus(arr));

					}

					while (posgreyRepository.TranscodingSqsRequestCheck(getcontentlist) > 0) {
						// requestStatusCheckList = posgreyRepository.requestStatus(arr);
						for (int i = 0; posgreyRepository.requestStatus(arr).size() > i; i++) {

							if (posgreyRepository.requestStatus(arr).size() > 0) {

								if (posgreyRepository.requestStatus(arr).size() > 0
										&& posgreyRepository.requestStatus(arr).get(i).contains("FAILED")) {
									log.info("requestStatusCheckList:FAILED: "
											+ posgreyRepository.requestStatus(arr).get(i).split(",")[0]);
									int callingUpdateQuery_Faild = posgreyRepository.updatecount_faild(
											Long.valueOf(posgreyRepository.requestStatus(arr).get(i).split(",")[0]));
									log.info("callingUpdateQuery_Faild  " + callingUpdateQuery_Faild);
									transcodingSuccess = transcodingAndRightsAPICall.dotranscode(getcontentlist);
									Thread.sleep(120000);

								}
								if (posgreyRepository.requestStatus(arr).size() > 0
										&& posgreyRepository.requestStatus(arr).get(i).contains("QUEUED")) {
									log.info("requestStatusCheckList;QUEUED: "
											+ posgreyRepository.requestStatus(arr).get(i).split(",")[0]);
									transcodingSuccess = transcodingAndRightsAPICall.dotranscode(getcontentlist);
									Thread.sleep(120000);
								}
							}

							else {
								break;
							}

						}
					}

					log.info("checkSqsRequestCount " + checkSqsRequestCount);

					// }
					try {

						if (posgreyRepository.TranscodingSqsRequestCheck(getcontentlist) == 0) {
							log.info("Rights Api Calling");
							String apiStatus = transcodingAndRightsAPICall.doRightsAssigment(getcontentlist);
							log.info("apiStatus" + apiStatus);
							rightsStatusContents = posgreyRepository.RightsStatusCheck(getcontentlist);
							model.addAttribute("rightsStatusContents", rightsStatusContents);
							return "VendorRightsStatus";

						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}

			catch (Exception e) {
				e.printStackTrace();
				flag = "fail";
			}

		}

		catch (NumberFormatException n) {
			n.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// }
		return null;

	}

}