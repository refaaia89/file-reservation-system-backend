package fifth.year.backendinternetapplication;

import fifth.year.backendinternetapplication.config.AuthProperties;
import fifth.year.backendinternetapplication.config.CheckInProperties;
import fifth.year.backendinternetapplication.db.DataLoader;
import fifth.year.backendinternetapplication.repository.PermissionRepository;
import fifth.year.backendinternetapplication.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties({AuthProperties.class, CheckInProperties.class})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
public class BackendInternetApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendInternetApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(
			RoleRepository roleRepository,
			PermissionRepository permissionRepository,
			PasswordEncoder passwordEncoder
	) {
		return args -> {
			DataLoader.seed(
					roleRepository,
					permissionRepository,
					passwordEncoder
			);
		};
	}
}