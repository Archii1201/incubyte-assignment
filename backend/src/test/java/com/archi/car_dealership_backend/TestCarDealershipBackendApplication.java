package com.archi.car_dealership_backend;

import org.springframework.boot.SpringApplication;

public class TestCarDealershipBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(CarDealershipBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
