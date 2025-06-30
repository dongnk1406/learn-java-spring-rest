package com.stephen.spring_boot_api.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.stephen.spring_boot_api.entity.User;
import com.stephen.spring_boot_api.enums.Role;
import com.stephen.spring_boot_api.repository.UserRepository;

@Configuration
public class ApplicationInitConfig {
    private PasswordEncoder passwordEncoder;

    public ApplicationInitConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    // use ConditionalOnProperty to check condition. If the condition is true, the bean will be created 
    // in this case, if the database is MySQL, the bean will be created
    @ConditionalOnProperty(prefix = "spring", value = "datasource.driverClassName", havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin"));
                HashSet<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());
                // user.setRoles(roles);
                userRepository.save(user);
            }
        };
    }

}
