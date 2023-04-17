package com.hungama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = AutoContentProcessApplication.class)
public class AutoContentProcessApplication extends SpringBootServletInitializer 
{

	public static void main(String[] args)
	{
		SpringApplication.run(AutoContentProcessApplication.class, args);
	}
	
	@Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	      return builder.sources(AutoContentProcessApplication.class);
	  }

}
