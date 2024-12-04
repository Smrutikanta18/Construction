package com.construction.Construction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HeaderController {

    @RequestMapping("/index")
    public String home(){
        return "index";
    }
    @RequestMapping("/")
    public String home2(){
        return "index";
    }

    @RequestMapping("/about")
    public String about(){
        return "about";
    }
    @RequestMapping("/services")
    public String service(){
        return "services";
    }
    @RequestMapping("/contact")
    public String contact(){
        return "contact";
    }
    @RequestMapping("/gallery")
    public String gallery(){
        return "gallery";
    }
    @RequestMapping("/completedprojects")
    public String completedprojects(){
        return "completedprojects";
    }
    @RequestMapping("/ongoingprojects")
    public String ongoingprojects(){
        return "ongoingprojects";
    }
    @RequestMapping("/addproject")
    public String addproject(){
        return "backend/addproject";
    }


}
