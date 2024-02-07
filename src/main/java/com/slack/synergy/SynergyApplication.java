package com.slack.synergy;

import com.slack.synergy.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SynergyApplication {
	@Autowired
	private InitService initService;

	public static void main(String[] args) {
		SpringApplication.run(SynergyApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		// Ajout d'un canal par défaut
		initService.createDefaultChannelIfNotExists();
		// Ajout d'un utilisateur par défaut
		initService.createInitialUserIfNotExists();
	}

}