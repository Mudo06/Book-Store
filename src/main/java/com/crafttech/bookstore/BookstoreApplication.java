package com.crafttech.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class BookstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);

		// Swagger UI'yi tarayıcıda açmak için
		openSwaggerUI();
	}

	private static void openSwaggerUI() {
		try {
			// Uygulama portu varsayılan olarak 8080
			String swaggerUrl = "http://localhost:8080/swagger-ui.html";
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI(swaggerUrl));
			} else {
				System.out.println("Otomatik tarayıcı açma desteklenmiyor.");
			}
		} catch (Exception e) {
			System.out.println("Swagger UI açılamadı: " + e.getMessage());
		}
	}
}
