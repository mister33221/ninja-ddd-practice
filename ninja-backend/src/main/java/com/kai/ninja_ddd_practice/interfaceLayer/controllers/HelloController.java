package com.kai.ninja_ddd_practice.interfaceLayer.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/world")
    public String helloWorld() {
        return "Hello World!";
    }

}
