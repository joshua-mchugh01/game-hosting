package com.example.demo.ovh.credential.feign.model;

import lombok.Data;

@Data
public class SshKeyApi {

    private String id;
    private String name;
    private String publicKey;
}
