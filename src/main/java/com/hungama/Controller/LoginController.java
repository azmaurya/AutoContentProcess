package com.hungama.Controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	private static final Logger log = LogManager.getLogger(LoginController.class);

	@GetMapping("/")
	public String LoginPage()

	{
		return "index";
	}

	@GetMapping("/connection")
	public String WelcomePage(ModelMap model, @RequestParam String username, @RequestParam String password)

	{
		log.info("username ->" + username);
		log.info("password ->" + password);

		if (username.equals("admin") && password.equals("admin")) {

			return "VendorName";
		} else {
			return "LoginPage";
		}

	}

}
