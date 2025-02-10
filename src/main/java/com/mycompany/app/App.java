package com.mycompany.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class App {

    public static void main(String[] args) {
        // Atur port server ke 3000
        System.setProperty("server.port", "3001");
        SpringApplication.run(App.class, args);
    }

    // Endpoint untuk root ("/") yang mengembalikan pesan "Hello World!"
    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }
}
