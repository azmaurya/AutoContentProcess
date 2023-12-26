package com.hungama.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PendingToQueued 
{
	public String  pendingToQue(List<Integer>arr) throws IOException
	{

     String transcodingIds = arr.toString().replaceAll("(^\\[|\\]$)", "")+ ""; // Replace with actual transcoding IDs	
	 String url = "http://34.86.122.205:86/ImageTranscoding/StartImageTranscoding";

     URL apiUrl = new URL(url);
     HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
     connection.setRequestMethod("POST");
     connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
     connection.setDoOutput(true);

     String postData = "TranscodingIds=" + transcodingIds;
     try (OutputStream os = connection.getOutputStream()) {
         byte[] input = postData.getBytes(StandardCharsets.UTF_8);
         os.write(input, 0, input.length);
     }

     int responseCode = connection.getResponseCode();
     System.out.println("Response Code: " + responseCode);

     // Handle the response here if needed
     
     return postData ;
	}
	
	
 }
