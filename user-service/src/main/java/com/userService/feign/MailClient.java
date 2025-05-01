package com.userService.feign;

import com.userService.dto.MailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "mailing-service")
public interface MailClient {}
/*
    @PostMapping("/mail/send")
    void sendMail(@RequestBody MailRequest request);
}*/
