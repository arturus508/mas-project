(function(){
  "use strict";
  const $ = s => document.querySelector(s);
  let running = false, timer = null, state = "IN", remain = 0, inS = 4, exS = 4;

  function exists(id){ return document.getElementById(id) != null; }

  function readPreset(){
    const pat = $("#pattern")?.value;
    if (!pat) return;
    if (pat === "custom"){
      inS = Math.max(1, +($("#inS")?.value || 4));
      exS = Math.max(1, +($("#exS")?.value || 4));
    } else {
      const a = pat.split("");
      inS = +a[0]; exS = +a[1];
    }
  }

  function setPhase(phase, secs){
    const phaseEl = $("#phase"), cd = $("#countdown"), circle = $("#circle");
    if (!phaseEl || !cd || !circle) return;
    phaseEl.textContent = phase === "IN" ? "Breathe in" : "Breathe out";
    cd.textContent = secs + " s";
    circle.style.transform = phase === "IN" ? "scale(1.12)" : "scale(0.88)";
  }

  function tick(){
    if (!running) return;
    if (remain <= 0){
      state = (state === "IN") ? "OUT" : "IN";
      remain = (state === "IN") ? inS : exS;
      setPhase(state, remain);
    } else {
      remain--;
      const cd = $("#countdown");
      if (cd) cd.textContent = remain + " s";
    }
    timer = setTimeout(tick, 1000);
  }

  function start(){
    if (running) return;
    readPreset();
    running = true; state = "IN"; remain = inS;
    const sb = $("#startBtn"), xb = $("#stopBtn");
    if (sb) sb.disabled = true;
    if (xb) xb.disabled = false;
    setPhase(state, remain);
    tick();
  }

  function stop(){
    running = false;
    if (timer) clearTimeout(timer);
    const sb = $("#startBtn"), xb = $("#stopBtn"), phaseEl = $("#phase"), cd = $("#countdown"), circle = $("#circle");
    if (sb) sb.disabled = false;
    if (xb) xb.disabled = true;
    if (phaseEl) phaseEl.textContent = "Ready";
    if (cd) cd.textContent = "";
    if (circle) circle.style.transform = "scale(1.00)";
  }

  function onPresetChange(){
    const row = $("#customRow"), pat = $("#pattern")?.value;
    if (row) row.style.display = (pat === "custom") ? "grid" : "none";
  }

  function init(){
 
    if (!exists("startBtn")) return;

    const startBtn = $("#startBtn");
    const stopBtn  = $("#stopBtn");
    const pattern  = $("#pattern");

    startBtn.addEventListener("click", function(e){ e.preventDefault(); start(); });
    stopBtn.addEventListener("click",  function(e){ e.preventDefault(); stop();  });
    pattern.addEventListener("change", onPresetChange);
    onPresetChange();
  }

  if (document.readyState === "loading"){
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }

  
  window.breathingTimer = { start, stop };
})();
