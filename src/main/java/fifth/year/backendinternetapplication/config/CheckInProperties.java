package fifth.year.backendinternetapplication.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "check.in")
public record CheckInProperties(Integer days) {
}