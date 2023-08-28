package io.bootify.visit_managment_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();

    }

//    public static void main(String[] args) {
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        System.out.println(passwordEncoder.encode("abc123"));
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf((csrfConfigurer) -> {csrfConfigurer.disable();})
                .authorizeHttpRequests((auth)-> {
                    auth.requestMatchers(new AntPathRequestMatcher("/api/admin/**")).hasAuthority("ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/resident/**")).hasAuthority("RESIDENT")
                            .requestMatchers(new AntPathRequestMatcher("/api/gatekeeper/**")).hasAuthority("GATEKEEPER").anyRequest()
                            .authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();

    }
}
