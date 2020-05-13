package com.blue.tnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@ComponentScan({"com.blue.tnb.*"})
@EnableJpaRepositories("com.blue.tnb.repository")
@EnableAsync
public class TnbApplication {

	public static void main(String[] args) {
		SpringApplication.run(TnbApplication.class, args);
	}


	@Bean(destroyMethod = "shutdown")
	public ExecutorService taskExecutor(){
		return Executors.newCachedThreadPool();
	}
}
