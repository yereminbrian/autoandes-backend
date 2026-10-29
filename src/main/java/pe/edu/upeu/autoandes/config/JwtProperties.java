package pe.edu.upeu.autoandes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("autoandes.jwt")
public record JwtProperties(String secret, long expirationSeconds) {}
