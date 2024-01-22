package com.demo.exchange.service.impl;

import com.demo.exchange.repository.ExchangeRateRepository;
import com.demo.exchange.service.ExchangeRateService;
import com.demo.exchange.vo.ExchangeRate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public String getExchangeRates(String country) throws JsonProcessingException {
        List<ExchangeRate> exchangeRates;
        List<ExchangeRate> newExchangeRates = null;
        try {
            List<String> yearOfDayList = generateDatesForYear(2021);
            exchangeRates = exchangeRateRepository.getExchangeRates(country);
            exchangeRates = exchangeRates.stream().filter(v -> v.getCurrency().equals(country)).collect(Collectors.toList());
            Map<String, ExchangeRate> exchangeRateMap = exchangeRates.stream().collect(Collectors.toMap(ExchangeRate::getDate, v -> v));
            for (String day : yearOfDayList) {
                if (!exchangeRateMap.containsKey(day)) {
                    LocalDate originalDate = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyyMMdd"));
                    LocalDate localDate = originalDate.minusDays(1);
                    String dateStr = DateTimeFormatter.ofPattern("yyyyMMdd").format(localDate);
                    ExchangeRate exchangeRateLastDay = exchangeRateMap.get(dateStr);
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setDate(day);
                    exchangeRate.setCurrency(exchangeRateLastDay.getCurrency());
                    exchangeRate.setSpotBuyRate(exchangeRateLastDay.getSpotBuyRate());
                    exchangeRate.setSpotSellRate(exchangeRateLastDay.getSpotSellRate());
                    exchangeRateMap.put(day, exchangeRate);
                }
            }
            newExchangeRates = new ArrayList<>(exchangeRateMap.values());
            newExchangeRates.sort(Comparator.comparing(ExchangeRate::getDate));
            newExchangeRates = newExchangeRates.stream().filter(v -> v.getDate().substring(6).equals("01")).toList();
        } catch (Exception e) {
            log.error("ExchangeRateServiceImpl getExchangeRate error: ", e);
        }
        return new ObjectMapper().writeValueAsString(newExchangeRates);
    }

    public static List<String> generateDatesForYear(int year) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int daysInYear = isLeapYear(year) ? 366 : 365;

        List<String> list = new ArrayList<>();
        // 生成365天的日期
        for (int i = 0; i < daysInYear; i++) {
            String formattedDate = sdf.format(calendar.getTime());
            list.add(formattedDate);
            // 將日期加1天
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
