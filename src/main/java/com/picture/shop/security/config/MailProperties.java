package com.picture.shop.security.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "ps.mail")
@Configuration
@EnableConfigurationProperties
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailProperties {

    private String username;

    private String password;

    private boolean auth;

    private boolean starttls;

    private String host;

    private int port;

    private String trust;
}