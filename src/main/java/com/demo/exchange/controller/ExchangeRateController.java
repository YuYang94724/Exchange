package com.demo.exchange.controller;

import com.demo.exchange.service.ExchangeRateService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/api")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @PostMapping("/exchange-rates")
    public ResponseEntity<Object> getExchangeRates(String country) {
        String rsp = "";
        try {
            if (StringUtils.isNotBlank(country)) {
                rsp = exchangeRateService.getExchangeRates(country);
            }
        } catch (Exception e) {
            log.error("ExchangeRateController /change error: ", e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(rsp);
    }
}
