package com.example.healthfitness.controller;

import com.example.healthfitness.service.CurrentUserService;
import com.example.healthfitness.service.UserService;
import com.example.healthfitness.web.form.PasswordChangeForm;
import com.example.healthfitness.web.form.ProfileNameForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileViewController {

    private final CurrentUserService currentUserService;
    private final UserService userService;

    public ProfileViewController(CurrentUserService currentUserService,
                                 UserService userService) {
        this.currentUserService = currentUserService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(@RequestParam(value = "nameUpdated", required = false) String nameUpdated,
                          @RequestParam(value = "passwordUpdated", required = false) String passwordUpdated,
                          Model model) {
        Long userId = currentUserService.id();
        var user = userService.getUserById(userId);
        model.addAttribute("user", user);
        ProfileNameForm nameForm = new ProfileNameForm();
        nameForm.setName(user.getName());
        model.addAttribute("profileNameForm", nameForm);
        model.addAttribute("passwordChangeForm", new PasswordChangeForm());
        model.addAttribute("nameUpdated", nameUpdated != null);
        model.addAttribute("passwordUpdated", passwordUpdated != null);
        model.addAttribute("pageTitle", "Profile");
        return "profile";
    }

    @PostMapping("/profile/name")
    public String updateName(@Valid ProfileNameForm profileNameForm,
                             BindingResult bindingResult,
                             Model model) {
        Long userId = currentUserService.id();
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.getUserById(userId));
            model.addAttribute("passwordChangeForm", new PasswordChangeForm());
            model.addAttribute("pageTitle", "Profile");
            return "profile";
        }
        userService.updateName(userId, profileNameForm.getName().trim());
        return "redirect:/profile?nameUpdated=1";
    }

    @PostMapping("/profile/password")
    public String updatePassword(@Valid PasswordChangeForm passwordChangeForm,
                                 BindingResult bindingResult,
                                 Model model) {
        Long userId = currentUserService.id();
        if (!passwordChangeForm.getNewPassword().equals(passwordChangeForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "mismatch", "Passwords do not match");
        }
        if (!userService.checkPassword(userId, passwordChangeForm.getCurrentPassword())) {
            bindingResult.rejectValue("currentPassword", "invalid", "Current password is incorrect");
        }
        if (bindingResult.hasErrors()) {
            var user = userService.getUserById(userId);
            model.addAttribute("user", user);
            ProfileNameForm nameForm = new ProfileNameForm();
            nameForm.setName(user.getName());
            model.addAttribute("profileNameForm", nameForm);
            model.addAttribute("pageTitle", "Profile");
            return "profile";
        }
        userService.updatePassword(userId, passwordChangeForm.getNewPassword());
        return "redirect:/profile?passwordUpdated=1";
    }
}
