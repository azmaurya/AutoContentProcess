package com.hungama.TranscodingAndRightsAPICall;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hungama.net.Http;
import com.hungama.net.HttpException;
import com.hungama.net.HttpResponse;
import com.hungama.service.VendorStatus;

public class FindResponseStatus
{
    private static final Logger log = LogManager.getLogger(VendorStatus.class);


	public static String FairPlay = null;
	public static String WideWine = null;
	public static String Multicodec = null;
	public static HttpResponse response = null;
	public static FileWriter fw ;
	public static FileWriter fw1 ;
	public static String line = null;
	public static Http http = new Http();
	public static String[][] headers = { { "Authorization", "Basic aHVuZ2FtYS5jb206aHVuZ2FtYS5jb20=" }};

	public static void main(String args[]) throws HttpException, IOException {

		String content_id = "5063078354302-IELOI2274159";
		String validTill = "2022-12-30T05:28:22Z";
		String allowHD = "true";
		String deviceId = "HTTP/1.1";
		String filepath = "D:\\Upc\\Ingestion\\universal.txt";
		fw = new FileWriter("D:\\Upc\\Drm\\Success.txt");
		fw1 = new FileWriter("D:\\Upc\\Drm\\Faild.txt");
		fw1 = new FileWriter("D:\\Upc\\Drm\\Faild.txt");
		fw1 = new FileWriter("D:\\Upc\\Drm\\Faild.txt");

		
		try (BufferedReader br = new BufferedReader(new FileReader(filepath)))
		{
			

			while ((line = br.readLine()) != null) {

				Http http = new Http();
				// calling Drm API
				
				boolean status=true;
				
			if(status)
				
				{
				 FairPlay = "http://drm-labtest.hungama.com/DRM-Prod/GetWidevineToken?contentId=" +line+"&" + "validTill=" + validTill + "&" + "allowHD=" + allowHD + "deviceId=" +deviceId;
				 mymethod();
				}
				
				 if(status)
				{
					 FairPlay = "http://drm-labtest.hungama.com/DRM-Prod/GetFairPlayToken?contentId=" +line+"&" + "validTill=" + validTill + "&" + "allowHD=" + allowHD + "deviceId=" +deviceId;
					 mymethod();
				}
				 if(status)
				{
					FairPlay = "http://drm-labtest.hungama.com/DRM-Prod/GetMBBToken?contentId=" +line+"&" + "validTill=" + validTill + "&" + "allowHD=" + allowHD + "deviceId=" +deviceId;
					 mymethod();
				}
				 if(status)
				{
					 FairPlay = "http://drm-labtest.hungama.com/DRM-Prod/GetPlayReadyToken?contentId=" +line+"&" + "validTill=" + validTill + "&" + "allowHD=" + allowHD + "deviceId=" +deviceId;
					 mymethod();
				}
				

				 //FairPlay = "http://drm-labtest.hungama.com/DRM-Prod/GetFairPlayToken?contentId=" +line+"&" + "validTill=" + validTill + "&" + "allowHD=" + allowHD + "deviceId=" +deviceId;
				// FairPlay = "http://drm-labtest.hungama.com/DRM-Prod/GetWidevineToken?contentId=" +line+"&" + "validTill=" + validTill + "&" + "allowHD=" + allowHD + "deviceId=" +deviceId;
				// FairPlay = "http://drm-labtest.hungama.com/DRM-Prod/GetMBBToken?contentId=" +line+"&" + "validTill=" + validTill + "&" + "allowHD=" + allowHD + "deviceId=" +deviceId;
				// FairPlay = "http://drm-labtest.hungama.com/DRM-Prod/GetPlayReadyToken?contentId=" +line+"&" + "validTill=" + validTill + "&" + "allowHD=" + allowHD + "deviceId=" +deviceId;

				
				
				//FairPlay = "https://87mum-content.hungama.com/1568/" + line + "/master.m3u8";
				
	    		String[][] headers = { { "Authorization", "Basic aHVuZ2FtYS5jb206aHVuZ2FtYS5jb20=" }};
				//  FairPlay="http://mdn.hungama.com/streaming/"+line+"/4/8?duration=PT0H30M0S&cdn=akamai&agent=application&cms=ms2&protocol=filedl&origin=S3";

				//FairPlay="https://mdn.hungama.com/streaming1/"+line+"/4/1235/?protocol=hls&cdn=akamai&cms=ms2&duration=PT6H0M0S&agent=application";
				//mymethod();
				
				//FairPlay="http://mdn.hungama.com/streaming/"+line+"/4/8?duration=PT0H30M0S&cdn=akamai&agent=application&cms=ms2&protocol=filedl&origin=S3";
				mymethod();
				
				//log.info("FairPlay " + FairPlay);
				//response = http.get(FairPlay,headers);
				 
	
			}

			fw.close();
			fw1.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void mymethod() throws IOException, HttpException
	{
		//response = http.get(FairPlay,headers);//For mdn
		response = http.get(FairPlay);//For drm
		String responseStr = response.getResponseAsString();
		System.out.println("responseStr "+responseStr);
		if (response.getStatusCode()==200)
		{

			System.out.println("Success");
			//fw.write(responseStr + "\n");
		fw.write(line + "\n");

		}

		
		  else 
		  {
		
		  System.out.println("Faild"); 
		  fw1.write(line + "\n");
		  
		  }
		 
		
	}
}