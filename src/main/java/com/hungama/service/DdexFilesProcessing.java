package com.hungama.service;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class DdexFilesProcessing 
{
	  @PostMapping("/")
	  public String greetingSubmit(String contentId,String RetailerId,String Status,Model model) {
	    model.addAttribute("contentId", contentId);
	    model.addAttribute("RetailerId", RetailerId);
	    model.addAttribute("Status", Status);
	    
	    return "Rights";
	  }

	
	
	
}
