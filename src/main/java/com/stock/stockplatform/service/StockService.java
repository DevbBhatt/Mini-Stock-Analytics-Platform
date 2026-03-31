    package com.stock.stockplatform.service;

    import com.stock.stockplatform.entity.StockData;
    import com.stock.stockplatform.repository.StockRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.io.*;
    import java.time.LocalDate;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Locale;
    import java.util.Map;
    import java.util.stream.Collectors;

    @Service
    public class StockService {

        @Autowired
        private StockRepository repo;


        public List<String> getCompanies() {
            return repo.findDistinctSymbols();
        }

        public List<StockData> getLast30Days(String symbol) {
            return repo.findTop30BySymbolOrderByDateDesc(symbol);
        }

        public Map<String, Double> getSummary(String symbol) {

            List<StockData> data = repo.findBySymbol(symbol);

            double high = data.stream().mapToDouble(StockData::getHigh).max().orElse(0);
            double low = data.stream().mapToDouble(StockData::getLow).min().orElse(0);
            double avg = data.stream().mapToDouble(StockData::getClose).average().orElse(0);

            return Map.of(
                    "52 Week High", high,
                    "52 Week Low", low,
                    "Average Close", avg
            );
        }


            public Map<String, List<String>> getTopStocks() {

                List<StockData> gainers = repo.findTop5ByOrderByDailyReturnDesc();
                List<StockData> losers = repo.findTop5ByOrderByDailyReturnAsc();

                // Sirf symbol + return show karenge (clean UI ke liye)
                List<String> topGainers = gainers.stream()
                        .map(g -> g.getSymbol() + " (" + String.format("%.2f", g.getDailyReturn()*100) + "%)")
                        .collect(Collectors.toList());

                List<String> topLosers = losers.stream()
                        .map(l -> l.getSymbol() + " (" + String.format("%.2f", l.getDailyReturn()*100) + "%)")
                        .collect(Collectors.toList());

                Map<String, List<String>> result = new HashMap<>();
                result.put("gainers", topGainers);
                result.put("losers", topLosers);

                return result;
            }

        public void processBhavcopy(String fileName) throws Exception {

            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("data/" + fileName);

            if (is == null) {
                System.out.println("File not found: " + fileName);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                String[] arr = line.split(",", -1);

                if (arr.length < 17) continue;

                try {
                    String symbol = arr[7];

                    double open = parseDoubleSafe(arr[13]);
                    double high = parseDoubleSafe(arr[14]);
                    double low = parseDoubleSafe(arr[15]);
                    double close = parseDoubleSafe(arr[16]);

                    if (open == 0 && high == 0 && low == 0 && close == 0) {
                        System.out.println("Skipping invalid row: " + symbol);
                        continue;
                    }

                    String dateStr = arr[0];
                    LocalDate date = LocalDate.parse(dateStr);

                    System.out.println("Saving: " + symbol);

                    StockData data = new StockData();
                    data.setSymbol(symbol);
                    data.setDate(date);
                    data.setOpen(open);
                    data.setHigh(high);
                    data.setLow(low);
                    data.setClose(close);

                    // Safe dailyReturn
                    double dailyReturn = 0;
                    if (open != 0) {
                        dailyReturn = (close - open) / open;
                    }
                    data.setDailyReturn(dailyReturn);

                    repo.save(data);

                    System.out.println(" Saved: " + symbol);

                } catch (Exception e) {
                    System.out.println(" Error parsing row");
                    e.printStackTrace();
                }
            }

            br.close();

            System.out.println(" File processed: " + fileName);
        }

        private double parseDoubleSafe(String value) {
            try {
                if (value == null || value.trim().isEmpty()) return 0;
                if (value.equals("∞") || value.equals("-") || value.equals("NA")) return 0;
                return Double.parseDouble(value);
            } catch (Exception e) {
                return 0;
            }
        }


        public void loadRealData() {

            try {
                String[] files = {
                        "cm30.csv",
                        "cm27.csv",
                        "cm25.csv",
                        "cm24.csv",
                        "cm23.csv",
                        "cm20.csv",
                        "cm18.csv",
                };

                for (String file : files) {
                    processBhavcopy(file);
                }

                System.out.println("Data Loaded from Local CSV");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }