
package com.mistakemuseum.mistake_museum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;


@Controller
public class HomeController {

    @GetMapping("/")
public String home(Authentication authentication, Model model) {

    boolean loggedIn = authentication != null
            && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getPrincipal());

    model.addAttribute("isLoggedIn", loggedIn);

    return "index";
}

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "OK";
    }
}
