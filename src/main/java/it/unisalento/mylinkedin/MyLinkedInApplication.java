package it.unisalento.mylinkedin;

import it.unisalento.mylinkedin.iservices.IPostService;
import it.unisalento.mylinkedin.services.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class MyLinkedInApplication {


    public static void main(String[] args) {
        SpringApplication.run(MyLinkedInApplication.class, args);
    }
}
