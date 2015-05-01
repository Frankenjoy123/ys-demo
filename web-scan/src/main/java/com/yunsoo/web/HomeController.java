package com.yunsoo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("/")
public class HomeController {

    @RequestMapping("{key}")
    public String scan(@PathVariable(value = "key") String key, Model model) {
        model.addAttribute("key", key);
        return "home";
    }

    @RequestMapping(value = "")
    public String home() {
        return "default";
    }

}
