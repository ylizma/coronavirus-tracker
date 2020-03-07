package com.ylizma.coronatracker2.controllers;

import com.ylizma.coronatracker2.models.LocationStates;
import com.ylizma.coronatracker2.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService dataService;

    @GetMapping("/")
    public String home(Model model, @RequestParam(name = "s", defaultValue = "") String key) {

        int todaytotalcases = dataService.getAllstates().stream().mapToInt(LocationStates::getTotalCases).sum();
        model.addAttribute("title", "corona states");
        model.addAttribute("total", todaytotalcases);

        if (!key.isEmpty()) {
            model.addAttribute("data", dataService.search(key));
            model.addAttribute("search",key);
        } else{
            model.addAttribute("data", dataService.getAllstates());
            model.addAttribute("search","");
        }


        return "home";

    }
}
