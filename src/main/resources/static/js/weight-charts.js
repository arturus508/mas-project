(() => {
  const parseList = (value) => {
    if (!value) {
      return [];
    }
    let trimmed = value.trim();
    if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
      trimmed = trimmed.slice(1, -1);
    }
    if (!trimmed) {
      return [];
    }
    return trimmed.split(", ").map((item) => (item === "null" ? "" : item));
  };

  const parseNumberList = (value) =>
    parseList(value).map((item) => {
      const parsed = Number(item);
      return Number.isFinite(parsed) ? parsed : 0;
    });

  const buildWeightChart = (canvas) => {
    if (!canvas || typeof Chart === "undefined") {
      return;
    }
    const labels = parseList(canvas.dataset.weightLabels);
    const values = parseNumberList(canvas.dataset.weightValues);
    if (!labels.length || !values.length) {
      return;
    }
    const borderColor = canvas.dataset.weightColor || "#3b82f6";
    const backgroundColor =
      canvas.dataset.weightFill || "rgba(59, 130, 246, 0.12)";
    const showAxis = canvas.dataset.weightShowAxis === "true";
    const scales = showAxis
      ? { x: { display: false }, y: { title: { display: true, text: "kg" } } }
      : { x: { display: false }, y: { display: false } };

    new Chart(canvas, {
      type: "line",
      data: {
        labels,
        datasets: [
          {
            label: "Weight (kg)",
            data: values,
            borderColor,
            backgroundColor,
            borderWidth: 2,
            tension: 0.35,
            pointRadius: 3,
            pointBackgroundColor: borderColor,
            fill: true
          }
        ]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false }
        },
        interaction: {
          intersect: false,
          mode: "index"
        },
        scales
      }
    });
  };

  document
    .querySelectorAll("canvas[data-weight-labels][data-weight-values]")
    .forEach((canvas) => buildWeightChart(canvas));
})();
