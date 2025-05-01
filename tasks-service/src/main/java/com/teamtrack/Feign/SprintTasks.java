package com.teamtrack.Feign;

import com.teamtrack.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sprint-service", configuration = FeignClientConfig.class)
public interface SprintTasks {
    @GetMapping("/sprint-management/sprint/find-sprint/{sprintId}")
    void getSprint(@PathVariable("sprintId") Long teamId);
}


