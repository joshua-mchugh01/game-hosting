package com.example.demo.web.password.reset.command.service.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class FetchUserIdByRecoveryTokenQuery {

    String token;
}
