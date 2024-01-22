package com.demo.exchange.vo;

import lombok.Data;

@Data
public class ExchangeRate {

    private String date;
    private String currency;
    private String spotBuyRate;
    private String spotSellRate;
}
