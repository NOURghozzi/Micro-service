package com.userService.dto;

import com.userService.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class MailRequest {
    private User to;
    private String subject;
    private String body;

}