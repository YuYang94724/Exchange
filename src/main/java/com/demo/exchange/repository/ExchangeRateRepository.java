package com.demo.exchange.repository;

import com.demo.exchange.vo.ExchangeRate;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Repository
public class ExchangeRateRepository {

    public List<ExchangeRate> getExchangeRates(String country) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        List<ExchangeRate> dataList = new ArrayList<>();
        try {
            ClassPathResource classPathResource = new ClassPathResource("datas/20210101-20211231-FXCRT.csv");
            reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));
            String tmp;

            //tmp 20211230,USD,27.63,27.73
            while ((tmp = reader.readLine()) != null) {
                if (tmp.indexOf("日期") > 0 || !tmp.contains(country)) {
                    continue;
                }
                String[] data = tmp.split(",");
                if (data.length == 4) {
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setDate(data[0]);
                    exchangeRate.setCurrency(data[1]);
                    exchangeRate.setSpotBuyRate(data[2]);
                    exchangeRate.setSpotSellRate(data[3]);
                    dataList.add(exchangeRate);
                }
            }
        } catch (Exception e) {
            log.error("ExchangeRateRepository getExchangeRate error: ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    log.error("reader close error: ", e);
                }
            }
        }
        return dataList;
    }
}
