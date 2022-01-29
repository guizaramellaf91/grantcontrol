package br.com.zaratech.grantcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@SuppressWarnings("SpellCheckingInspection")
@SpringBootApplication
@Repository("br.com.zaratech.repository")
@ComponentScan(basePackages = {"br.com.zaratech"})
@EntityScan("br.com.zaratech.model")
@EnableJpaRepositories(basePackages = {"br.com.zaratech.repository", "br.com.zaratech.security"})
public class GrantControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrantControlApplication.class, args);
    }
}