package lk.ac.sliit.drivingschool.drivingschoolsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for testing
                .authorizeHttpRequests(auth -> auth
                        // IMPORTANT: Let the browser load your CSS file!
                        .requestMatchers("/student/subsystem.css").permitAll()
                        // Allow all other requests for now while you are building
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}