const form = document.getElementById("form");
const citySelect = document.getElementById("city");
const chart = document.getElementById("weatherImg");

function updateChart() {
  const [lat, lon] = citySelect.value.split("|");
  const url = `/weather/chart?lat=${lat}&lon=${lon}&t=${Date.now()}`;
  chart.src = url;
}

// Carica il grafico inizialmente
window.addEventListener("DOMContentLoaded", updateChart);

// Aggiorna quando cambi città
citySelect.addEventListener("change", updateChart);

// Blocca il submit standard
form.addEventListener("submit", (e) => {
  e.preventDefault();
  updateChart();
});
