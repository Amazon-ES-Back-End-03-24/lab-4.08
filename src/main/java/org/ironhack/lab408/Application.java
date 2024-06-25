package org.ironhack.lab408;

import org.ironhack.lab408.dtos.AuthorDTO;
import org.ironhack.lab408.dtos.BlogPostDTO;
import org.ironhack.lab408.model.Author;
import org.ironhack.lab408.model.Role;
import org.ironhack.lab408.model.User;
import org.ironhack.lab408.service.AuthorService;
import org.ironhack.lab408.service.BlogPostService;
import org.ironhack.lab408.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // To centralize configuration and allow to use it through dependency injection in our application
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // To have some data to start with, it's executed automatically
    @Bean
    CommandLineRunner run(UserService userService, AuthorService authorService, BlogPostService blogPostService) {
        return args -> {
            Role role1 = userService.saveRole(new Role(null, "ROLE_CONTRIBUTOR"));
            Role role2 = userService.saveRole(new Role(null, "ROLE_ADMIN"));

            userService.saveUser(new User(null, "John Doe", "john", "1234", List.of(role1), null));
            userService.saveUser(new User(null, "Jane Doe", "jane", "1234", List.of(role2), null));

            Author author1 = authorService.store(new AuthorDTO("Aiko Tanaka"));
            Author author2 = authorService.store(new AuthorDTO("Jonas Schmidt"));
            Author author3 = authorService.store(new AuthorDTO("Cas Van Dijk"));

            blogPostService.store(new BlogPostDTO("Boost Your Productivity with 10 Easy Tips", "Productivity - we all want it but it seems ...", author1.getId()));
            blogPostService.store(new BlogPostDTO("How to Focus", "Do you ever sit down to work and find yourself ...", author2.getId()));
            blogPostService.store(new BlogPostDTO("Learn to Speed Read in 30 Days", "Knowledge, not ability, is the great determiner of ...", author3.getId()));
        };
    }
}
