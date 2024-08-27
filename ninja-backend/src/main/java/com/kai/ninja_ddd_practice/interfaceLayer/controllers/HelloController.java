package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@CrossOrigin(origins = "*")
public class HelloController {

    @GetMapping("/world")
    public String helloWorld() {
        return "Hello World!";
    }

}
