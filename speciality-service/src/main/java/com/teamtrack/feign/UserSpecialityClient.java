package com.teamtrack.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-speciality-service")
public interface UserSpecialityClient {
    @GetMapping("/user-speciality/{userId}")
    List<Long> getSpecialitiesByUserId(@PathVariable Long userId);
}
