package fifth.year.backendinternetapplication.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "auth")
public record AuthProperties(String secrete_key,long expiration_token, long expiration_refresh_token) {
}