package com.softuni.stayeasy.web;

import com.softuni.stayeasy.model.dto.user.LoginBindingModel;
import com.softuni.stayeasy.model.dto.user.RegisterBindingModel;
import com.softuni.stayeasy.model.entity.user.User;
import com.softuni.stayeasy.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // --- REGISTER ---

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerData", new RegisterBindingModel());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterBindingModel registerData, Model model) {

        if (!registerData.getPassword().equals(registerData.getConfirmPassword())) {
            model.addAttribute("registerData", registerData);
            model.addAttribute("passwordMismatch", true);
            return "auth/register";
        }

        if (userService.existsByUsername(registerData.getUsername())) {
            model.addAttribute("registerData", registerData);
            model.addAttribute("usernameTaken", true);
            return "auth/register";
        }

        if (userService.existsByEmail(registerData.getEmail())) {
            model.addAttribute("registerData", registerData);
            model.addAttribute("emailTaken", true);
            return "auth/register";
        }

        userService.register(
                registerData.getUsername(),
                registerData.getEmail(),
                registerData.getPassword(),
                registerData.getFirstName(),
                registerData.getLastName()
        );

        return "redirect:/auth/login";
    }

    // --- LOGIN ---

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginData", new LoginBindingModel());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginBindingModel loginData,
                        Model model,
                        HttpSession session) {

        Optional<User> userOpt = userService.findByUsername(loginData.getUsername());

        // Use passwordEncoder.matches() to verify hashed password
        if (userOpt.isEmpty() || !passwordEncoder.matches(loginData.getPassword(), userOpt.get().getPassword())) {
            model.addAttribute("loginData", loginData);
            model.addAttribute("invalidCredentials", true);
            return "auth/login";
        }

        session.setAttribute("userId", userOpt.get().getId());
        session.setAttribute("username", userOpt.get().getUsername());
        session.setAttribute("userRole", userOpt.get().getRole().name());

        return "redirect:/";
    }

    // --- LOGOUT ---

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
