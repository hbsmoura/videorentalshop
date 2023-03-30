package com.hbsmoura.videorentalshop;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info=@Info(
				title="Video Rental Shop API",
				description = "API for managing a video rental shop, with access control for clients " +
						"and employees, with hierarchy levels for employees (regular and manager)."
		)
)
public class VideoRentalShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoRentalShopApplication.class, args);
	}

}
