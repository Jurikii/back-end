package com.juriki.api_juriki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@SpringBootApplication
public class ApiJurikiApplication {

	public static void main(String[] args) {
		carregarEnv();
		SpringApplication.run(ApiJurikiApplication.class, args);
	}

	private static void carregarEnv() {
		try {
			var path = Path.of(".env");
			if (Files.exists(path)) {
				var props = new Properties();
				try (var reader = Files.newBufferedReader(path)) {
					props.load(reader);
				}
				props.forEach((key, value) -> {
					if (System.getProperty((String) key) == null) {
						System.setProperty((String) key, (String) value);
					}
				});
			}
		} catch (IOException e) {
			System.err.println("Aviso: não foi possível carregar .env: " + e.getMessage());
		}
	}

}
