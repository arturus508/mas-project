package com.example.healthfitness.controller;

import com.example.healthfitness.service.DreamService;
import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.web.form.DreamForm;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller for dream logging.  This version no longer uses
 * {@code SecurityContextHolder} to look up the authenticated user.  It
 * instead delegates to {@link CurrentUserService} for the current user id.
 * Query parameters for dates are automatically converted via
 * {@link DateTimeFormat}.
 */
@Controller
@RequestMapping("/mind/dreams")
public class DreamViewController {

    private final DreamService dreamService;
    private final CurrentUserService currentUserService;

    public DreamViewController(DreamService dreamService,
                               CurrentUserService currentUserService) {
        this.dreamService = dreamService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String list(@RequestParam(value = "from", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                       @RequestParam(value = "to", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                       @RequestParam(value = "tag", required = false) String tag,
                       Model model) {
        Long userId = currentUserService.id();
        var items = dreamService.search(userId, from, to, tag);
        model.addAttribute("dreams", items);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("tag", tag);
        DreamForm form = new DreamForm();
        form.setDate(LocalDate.now());
        model.addAttribute("dreamForm", form);
        return "dreams";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("dreamForm") DreamForm form,
                      BindingResult bindingResult,
                      @RequestParam(value = "from", required = false)
                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                      @RequestParam(value = "to", required = false)
                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                      @RequestParam(value = "tag", required = false) String tag,
                      Model model) {
        Long userId = currentUserService.id();
        if (bindingResult.hasErrors()) {
            model.addAttribute("dreams", dreamService.listAll(userId));
            model.addAttribute("from", from);
            model.addAttribute("to", to);
            model.addAttribute("tag", tag);
            return "dreams";
        }
        dreamService.create(userId, form);
        String redirect = "redirect:/mind/dreams";
        if (from != null || to != null || (tag != null && !tag.isBlank())) {
            redirect += "?";
            boolean first = true;
            if (from != null) {
                redirect += "from=" + from;
                first = false;
            }
            if (to != null) {
                redirect += (first ? "" : "&") + "to=" + to;
                first = false;
            }
            if (tag != null && !tag.isBlank()) {
                redirect += (first ? "" : "&") + "tag=" + tag;
            }
        }
        return redirect;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @RequestParam(value = "from", required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                         @RequestParam(value = "to", required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                         @RequestParam(value = "tag", required = false) String tag) {
        Long userId = currentUserService.id();
        dreamService.delete(userId, id);
        String redirect = "redirect:/mind/dreams";
        if (from != null || to != null || (tag != null && !tag.isBlank())) {
            redirect += "?";
            boolean first = true;
            if (from != null) {
                redirect += "from=" + from;
                first = false;
            }
            if (to != null) {
                redirect += (first ? "" : "&") + "to=" + to;
                first = false;
            }
            if (tag != null && !tag.isBlank()) {
                redirect += (first ? "" : "&") + "tag=" + tag;
            }
        }
        return redirect;
    }
}
