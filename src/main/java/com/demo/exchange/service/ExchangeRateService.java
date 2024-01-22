package com.demo.exchange.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ExchangeRateService {

    String getExchangeRates(String country) throws JsonProcessingException;
}
