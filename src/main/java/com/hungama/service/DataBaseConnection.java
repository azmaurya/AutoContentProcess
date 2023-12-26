package com.hungama.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DataBaseConnection 
{

	 public static void main(String[] args) throws Exception {
	        String url = "http://34.86.122.205:86/ImageTranscoding/StartImageTranscoding";
	        String transcodingIds = "106579170"; // Replace with actual transcoding IDs

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
	    }
	

}
