package com.stock.stockplatform.controller;

import com.stock.stockplatform.entity.StockData;
import com.stock.stockplatform.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private StockService service;

    @GetMapping("/companies")
    public List<String> getCompanies() {
        return service.getCompanies();
    }


    @GetMapping("/data/{symbol}")
    public List<StockData> getData(@PathVariable String symbol) {
        return service.getLast30Days(symbol);
    }

    @GetMapping("/summary/{symbol}")
    public Map<String, Double> getSummary(@PathVariable String symbol) {
        return service.getSummary(symbol);
    }


    @GetMapping("/top")
    public Map<String, List<String>> getTopStocks() {
        return service.getTopStocks();
    }

    @GetMapping("/load-data")
    public String loadRealData() {
        service.loadRealData();
        return "Real NSE Data Loaded!";
    }

}