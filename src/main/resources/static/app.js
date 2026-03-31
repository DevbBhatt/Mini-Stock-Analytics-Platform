let chart;

// Moving Average function
function movingAverage(data, period) {
    let result = [];

    for (let i = 0; i < data.length; i++) {
        if (i < period) {
            result.push(null);
        } else {
            let sum = 0;
            for (let j = i - period; j < i; j++) {
                sum += data[j];
            }
            result.push(sum / period);
        }
    }
    return result;
}

// Load companies
fetch('/api/companies')
.then(res => res.json())
.then(companies => {

    const c1 = document.getElementById('company1');
    const c2 = document.getElementById('company2');

    companies.forEach(c => {
        let opt1 = new Option(c, c);
        let opt2 = new Option(c, c);
        c1.add(opt1);
        c2.add(opt2);
    });

    loadChart(companies[0], companies[1]);

});

// Change events
document.getElementById('company1').addEventListener('change', updateChart);
document.getElementById('company2').addEventListener('change', updateChart);

function updateChart() {
    const s1 = document.getElementById('company1').value;
    const s2 = document.getElementById('company2').value;
    loadChart(s1, s2);
}

// Load chart (Compare)
function loadChart(symbol1, symbol2) {

    Promise.all([
        fetch(`/api/data/${symbol1}`).then(res => res.json()),
        fetch(`/api/data/${symbol2}`).then(res => res.json())
    ])
    .then(([data1, data2]) => {

        const labels = data1.map(d => d.date);

        const prices1 = data1.map(d => d.close);
        const prices2 = data2.map(d => d.close);

        const ma1 = movingAverage(prices1, 5);

        if (chart) chart.destroy();

        chart = new Chart(document.getElementById('chart'), {
            type: 'line',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: symbol1,
                        data: prices1,
                        borderWidth: 2
                    },
                    {
                        label: symbol2,
                        data: prices2,
                        borderWidth: 2
                    },
                    {
                        label: symbol1 + ' MA(5)',
                        data: ma1,
                        borderDash: [5,5]
                    }
                ]
            }
        });
    });
}

// Load Top Gainers / Losers
fetch('/api/top')
.then(res => res.json())
.then(data => {

    document.getElementById('gainers').innerHTML =
        data.gainers.map(g => `<div>${g}</div>`).join('');

    document.getElementById('losers').innerHTML =
        data.losers.map(l => `<div>${l}</div>`).join('');
});