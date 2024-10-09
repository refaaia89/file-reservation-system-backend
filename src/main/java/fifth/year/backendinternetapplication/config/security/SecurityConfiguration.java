package fifth.year.backendinternetapplication.config.security;

import com.google.gson.Gson;
import fifth.year.backendinternetapplication.config.security.jwt.JwtAuthenticationEntryPoint;
import fifth.year.backendinternetapplication.config.security.jwt.JwtAuthenticationFilter;
import fifth.year.backendinternetapplication.dto.response.main.SuccessResponse;
import fifth.year.backendinternetapplication.utils.handlers.MyLogoutHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;
import java.util.HashMap;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    @Qualifier("delegatedAuthenticationEntryPoint")
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final MyLogoutHandler myLogoutHandler;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, MyLogoutHandler myLogoutHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.myLogoutHandler = myLogoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/api/auth/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .logoutUrl("/api/auth/logout")
                                .addLogoutHandler(myLogoutHandler)
                                .logoutSuccessHandler(
                                        (request, response, authentication) -> {
                                            SecurityContextHolder.clearContext();
                                            PrintWriter out = response.getWriter();
                                            response.setContentType("application/json");
                                            response.setCharacterEncoding("UTF-8");
                                            out.print(new Gson().toJson(new SuccessResponse(
                                                    "User Logout Successfully", new HashMap<>()
                                            )));
                                            out.flush();
                                        }
                                )
                );

        return http.build();
    }
}
