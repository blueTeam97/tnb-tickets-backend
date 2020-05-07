package com.blue.tnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.blue.tnb.*"})
@EnableJpaRepositories("com.blue.tnb.repository")
public class TnbApplication {

	public static void main(String[] args) {
		SpringApplication.run(TnbApplication.class, args);
	}

}
