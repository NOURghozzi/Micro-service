package com.teamtrack.Feign;

import com.teamtrack.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "teamtrack", configuration = FeignClientConfig.class)
public interface UserClient {
    @GetMapping("/user-management/api/users/find-user/{userId}")
    Long getUser(@PathVariable("userId") Long userId);
}


