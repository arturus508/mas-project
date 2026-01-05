
(() => {
  const STORAGE_KEY = "hub.sectionState";

  const readState = () => {
    try {
      return JSON.parse(localStorage.getItem(STORAGE_KEY)) || {};
    } catch {
      return {};
    }
  };

  const writeState = (state) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
  };

  const setCollapsed = (section, collapsed) => {
    section.classList.toggle("collapsed", collapsed);
    const button = section.querySelector(".section-toggle");
    if (button) {
      button.classList.toggle("is-collapsed", collapsed);
      button.setAttribute("aria-expanded", String(!collapsed));
    }
  };

  document.addEventListener("DOMContentLoaded", () => {
    const state = readState();
    const sections = document.querySelectorAll(".section[data-section]");

    sections.forEach((section) => {
      const key = section.getAttribute("data-section");
      if (!key) {
        return;
      }
      const defaultState = (section.getAttribute("data-default") || "expanded") === "collapsed";
      const collapsed = key in state ? state[key] : defaultState;
      setCollapsed(section, collapsed);
    });

    document.addEventListener("click", (event) => {
      const button = event.target.closest(".section-toggle");
      if (!button) {
        return;
      }
      const key = button.getAttribute("data-toggle-section");
      if (!key) {
        return;
      }
      const section = document.querySelector(`.section[data-section="${key}"]`);
      if (!section) {
        return;
      }
      const nextCollapsed = !section.classList.contains("collapsed");
      setCollapsed(section, nextCollapsed);
      state[key] = nextCollapsed;
      writeState(state);
    });
  });
})();