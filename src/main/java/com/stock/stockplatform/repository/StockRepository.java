package com.stock.stockplatform.repository;

import com.stock.stockplatform.entity.StockData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StockRepository extends JpaRepository<StockData, Long> {

    List<StockData> findBySymbol(String symbol);

    List<StockData> findTop30BySymbolOrderByDateDesc(String symbol);

    @Query("SELECT DISTINCT s.symbol FROM StockData s")
    List<String> findDistinctSymbols();


    List<StockData> findTop5ByOrderByDailyReturnDesc();

    List<StockData> findTop5ByOrderByDailyReturnAsc();

    boolean existsBySymbolAndDate(String symbol, LocalDate date);

}