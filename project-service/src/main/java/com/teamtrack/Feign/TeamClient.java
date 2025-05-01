package com.teamtrack.Feign;

import com.teamtrack.config.FeignClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "team-service", configuration = FeignClientConfig.class)
public interface TeamClient {

    @GetMapping("/team-management/team/user/{userId}")
    List<Long> getTeamIdsByUserId(@PathVariable("userId") Long userId);
    @GetMapping("/team-management/team/{teamId}")
    void checkTeamById(@PathVariable("teamId") Long teamId);
}


