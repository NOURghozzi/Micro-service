package com.userService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableJpaRepositories("com.userService.repository")
@EnableFeignClients
@EnableDiscoveryClient
public class TeamtrackApplication {


	public static void main(String[] args) {
		SpringApplication.run(TeamtrackApplication.class, args);

	}


/*$2a$10$UbuMMKVxUH9a9ubwH6PDCeDi3Gb.mHYUvEnF0b24ksWWD3sc4yu7W
$2a$10$Gh8MNnXkcIT55O0r6PoK9uXspjTGbyHTVQejpPKhuxotedizulwJW

= MP : 0000*/
}
