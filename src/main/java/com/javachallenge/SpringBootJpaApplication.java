package com.javachallenge;

import com.javachallenge.entity.User;
import com.javachallenge.repository.UserRepository;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootJpaApplication implements CommandLineRunner {

    /*public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaApplication.class, args);
	}*/
   // private static final Logger log = LoggerFactory.getLogger(SpringBootJpaApplication.class);

    @Autowired
    private UserRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaApplication.class, args);
    }

    @Override
    public void run(String... args) {

        //log.info("StartApplication...");

        //repository.save(new User("Java"));
        
        System.out.println("\nfindAll()");
        repository.findAll().forEach(x -> System.out.println(x));

        

    }

}
