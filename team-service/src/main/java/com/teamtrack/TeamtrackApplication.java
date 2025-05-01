package com.teamtrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("com.teamtrack.repository")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.teamtrack.Feign")
public class TeamtrackApplication {
	public static void main(String[] args) {
		SpringApplication.run(TeamtrackApplication.class, args);

	}

/*$2a$10$UbuMMKVxUH9a9ubwH6PDCeDi3Gb.mHYUvEnF0b24ksWWD3sc4yu7W = MP : 0000*/
}
