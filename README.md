# 📈 Stock Analytics Platform

A full-stack financial data platform that collects, processes, and visualizes stock market data using real NSE Bhavcopy datasets.

---

## 🚀 Features

- 📊 Fetch and process real stock market data (NSE Bhavcopy)
- 🔄 Data cleaning and transformation using Java (ETL pipeline)
- 📈 REST APIs for stock insights
- 📉 Interactive charts using Chart.js
- 🔥 Top Gainers and Losers analysis
- 📊 Moving Average calculation
- 🔍 Compare multiple companies

---

## 🛠 Tech Stack

- **Backend:** Spring Boot (Java)
- **Database:** MySQL
- **Frontend:** HTML, JavaScript, Chart.js, Bootstrap
- **Data Processing:** Java 

---

## 📊 Data Source

- NSE Bhavcopy (UDiFF format)
- Processed locally using custom Java ETL pipeline

---

## 🔌 API Endpoints

### 1. Get Companies
GET /api/companies


### 2. Get Stock Data (Last 30 Days)

GET /api/data/{symbol}


### 3. Get Summary

GET /api/summary/{symbol}


### 4. Top Gainers & Losers

GET /api/top


### 5. Load Real Data

GET /api/load-data


---

## 📂 Project Structure


src/
├── controller/
├── service/
├── repository/
├── entity/
├── resources/
├── data/
├── static/


---

## ⚙️ Setup Instructions

1. Clone the repository
2. Configure MySQL in `application.properties`
3. Add Bhavcopy CSV files to:

src/main/resources/data/

4. Run the application
5. Call:  http://localhost:8080/api/load-data

6. Open dashboard:

http://localhost:8080/index.html


---

## 📸 Screenshots
![Dashboard](screenshots/dashboard.png)

---

## 🧠 Key Highlights

- Built custom ETL pipeline in Java
- Processed real-world financial data
- Implemented analytics like moving average and returns
- Full-stack implementation (Backend + UI)

---
## 🚀 Future Improvements

- Add caching (Redis)
- Deploy on cloud (Render / AWS)
- Add ML prediction model

---

## 👨‍💻 Author : Dev Bhatt
