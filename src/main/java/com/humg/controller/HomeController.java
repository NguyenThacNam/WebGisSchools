package com.humg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.humg.service.LocationService;

@Controller

public class HomeController {
	  @Autowired
	    private LocationService locationService;

	    @GetMapping("/")
	    public String home(Model model) {
	        model.addAttribute("locations", locationService.getAll());
	        return "site/home"; 
	    }
}
