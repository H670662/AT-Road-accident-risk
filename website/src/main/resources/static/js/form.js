const form = document.getElementById("predictForm");
const resultBox = document.getElementById("result");
const percentEl = document.getElementById("riskPercent");
const marker = document.getElementById("meterMarker");
const faceHolder = document.getElementById("faceHolder");

form.addEventListener("submit", async (e) => {
    e.preventDefault();

    resultBox.style.display = "flex";
    faceHolder.classList.remove("visible");
    marker.style.left = "0%";
    percentEl.textContent = "--";

    const data = {
        roadType: document.getElementById("roadType").value,
        maxSpeed: parseInt(document.getElementById("maxSpeed").value, 10),
        weather: document.getElementById("weather").value,
        timeOfDay: document.getElementById("timeOfDay").value
    };

    if (!data.roadType || !data.weather || !data.timeOfDay || Number.isNaN(data.maxSpeed)) {
        alert("Please fill all fields with valid values.");
        return;
    }

    const res = await fetch("/predict", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });

    if (!res.ok) {
        resultBox.style.display = "none";
        alert("Prediction failed");
        return;
    }

    const json = await res.json();
    const percent = Math.max(0, Math.min(100, json.percent ?? 0));

    percentEl.textContent = percent.toFixed(1) + "%";

    requestAnimationFrame(() => {
        marker.style.left = percent + "%";
    });

    setTimeout(() => {
        let face = "🙂";
        if (percent >= 10 && percent <= 25) face = "😐";
        if (percent > 25) face = "☹️";
        faceHolder.textContent = face;
        faceHolder.classList.add("visible");
    }, 900);
});
