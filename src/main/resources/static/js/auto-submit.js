document.addEventListener("DOMContentLoaded", () => {
  const inputs = document.querySelectorAll(".js-auto-submit");
  inputs.forEach((input) => {
    input.addEventListener("change", () => {
      const form = input.closest("form");
      if (form) {
        form.submit();
      }
    });
  });
});
