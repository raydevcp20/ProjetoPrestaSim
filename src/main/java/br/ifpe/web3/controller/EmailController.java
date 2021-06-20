package br.ifpe.web3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
public class EmailController {
	
	@Autowired
	private JavaMailSender mailSender;

	@Value("${USERNAME_EMAIL}")
	private String email;
	
	@Autowired
	private TemplateEngine templateEngine;
	

	public void enviar(String destinatario, String assunto, String corpo) {
		new Thread() {
			
			@Override
			public void run() {
				SimpleMailMessage message = new SimpleMailMessage();
				message.setSubject(assunto);
				message.setText(corpo);
				message.setTo(destinatario);
				message.setFrom(email);

				try {
					mailSender.send(message);
				} catch (Exception e) {
					throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage());
				}
			}

		}.start();
	}
}
