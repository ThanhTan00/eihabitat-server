package com.eihabitat.eihabitat_server.configuration;

import com.eihabitat.eihabitat_server.entity.Role;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.repository.RoleRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if(userRepository.findByEmail("eihabitat@gmail.com").isEmpty()) {


                Role adminRole = new Role("ADMIN", "role admin", null);
                Role userRole = new Role("USER", "role user", null);
                roleRepository.save(adminRole);
                roleRepository.save(userRole);
                HashSet<Role> roles = new HashSet<>();
                roles.add(adminRole);

                User user = User.builder()
                        .email("eihabitat@gmail.com")
                        .firstName("Eihabitat")
                        .lastName("Admin")
                        .password(passwordEncoder.encode("eihabitatadmin"))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: eihabitatadmin, please change your password!");
            }
        };
    };
}
