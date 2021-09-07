package com.fundamentos.springboot.fundamentos.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "user")
@Data
public class UserPojo {

    private String email;
    private String password;
    private int age;
}
