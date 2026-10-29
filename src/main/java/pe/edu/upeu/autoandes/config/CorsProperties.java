package pe.edu.upeu.autoandes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("autoandes.cors")
public record CorsProperties(String allowedOrigin) {}
