package br.ifpe.web3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import br.ifpe.web3.controller.EmailController;

@SpringBootApplication
public class Ad05RayaneSilvaApplication {
    
	public static void main(String[] args) {
		//System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow", "{}");
		SpringApplication.run(Ad05RayaneSilvaApplication.class, args);
	}
	

}
