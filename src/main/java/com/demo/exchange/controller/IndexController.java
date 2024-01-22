package com.demo.exchange.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class IndexController {

    @GetMapping(value = {"/", "/index"})
    public String index(Model mode) {
        mode.addAttribute("list", Arrays.asList("USD","HKD","GBP","AUD","CAD","SGD",
                "CHF", "JPY", "ZAR", "SEK", "NZD", "THB", "EUR", "CNY"));
        return "show";
    }
}
