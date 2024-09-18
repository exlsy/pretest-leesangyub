package com.pretest.leesangyub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Slf4j
@SpringBootApplication
@EntityScan(basePackages = {"com.pretest.leesangyub"})
public class PretaskApplication {

	public static void main(String[] args) {
		log.info("PretaskApplication start.!");
		SpringApplication.run(PretaskApplication.class, args);
	}

}
