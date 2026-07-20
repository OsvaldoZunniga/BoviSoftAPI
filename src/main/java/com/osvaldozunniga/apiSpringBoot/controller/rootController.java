package com.osvaldozunniga.apiSpringBoot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class rootController {
    @GetMapping("/")
    public String rootPath(){
        return "Welcome, this is BoviSoft APIREST and there u gonna find all the info about my cattle in Finca La Piolina. Thats the way Im gonna manage my cattles, including sells. Please check the endpoints available for more info.";
    };

}
