package com.teamtrack.Feign;

import com.teamtrack.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "teamtrack", configuration = FeignClientConfig.class)
public interface UserTasks {
    @GetMapping("/user-management/api/users/find-user/{userId}")
    void getUser(@PathVariable("userId") Long userId);
}
