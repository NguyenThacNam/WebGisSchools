package com.humg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    
   
    @GetMapping("/login")
    public String showLoginForm() {
        return "admin/login";
    }
 
    @GetMapping("/admin/dashboard")
    public String showDashboard() {
        return "admin/dashboard";
    }
    
   
   
}
