document.addEventListener("DOMContentLoaded", function () {
  const nav = document.querySelector(".navbar");
  if (!nav) {
    return;
  }

  const toggles = nav.querySelectorAll(".dropdown-toggle");
  if (!toggles.length) {
    return;
  }

  const closeAll = function () {
    nav.querySelectorAll(".dropdown-menu.show").forEach(function (menu) {
      menu.classList.remove("show");
    });
  };

  toggles.forEach(function (toggle) {
    toggle.addEventListener("click", function (event) {
      event.preventDefault();
      const parent = toggle.closest(".dropdown");
      if (!parent) {
        return;
      }
      const menu = parent.querySelector(".dropdown-menu");
      if (!menu) {
        return;
      }
      const isOpen = menu.classList.contains("show");
      closeAll();
      if (!isOpen) {
        menu.classList.add("show");
      }
    });
  });

  document.addEventListener("click", function (event) {
    if (!nav.contains(event.target)) {
      closeAll();
    }
  });
});
