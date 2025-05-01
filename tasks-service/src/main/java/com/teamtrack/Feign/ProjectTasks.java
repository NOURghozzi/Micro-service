package com.teamtrack.Feign;

import com.teamtrack.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "project-service", configuration = FeignClientConfig.class)
public interface ProjectTasks {

    /*@GetMapping("/team/user/{userId}")
    List<Long> getTeamIdsByUserId(@PathVariable("userId") Long userId);*/
    @GetMapping("/project-management/project/find-project/{projectId}")
    void getProject(@PathVariable("projectId") Long teamId);
}