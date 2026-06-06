package com.softuni.stayeasy.web;

import com.softuni.stayeasy.model.dto.user.LoginBindingModel;
import com.softuni.stayeasy.model.dto.user.RegisterBindingModel;
import com.softuni.stayeasy.model.entity.user.User;
import com.softuni.stayeasy.service.user.UserService;
import jakarta.servlet.http.HttpSession;
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

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // --- REGISTER ---

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerData", new RegisterBindingModel());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterBindingModel registerData, Model model) {

        //Check if password match
        if(!registerData.getPassword().equals(registerData.getConfirmPassword())) {
            model.addAttribute("registerData", registerData);
            model.addAttribute("passwordMismatch", true);
            return "auth/register";
        }

        //Check if username is taken
        if (userService.existsByUsername(registerData.getUsername())) {
            model.addAttribute("registerData", registerData);
            model.addAttribute("usernameTaken", true);
            return "auth/register";
        }

        //Check if email is taken
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

        //Check if user and password matches
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(loginData.getPassword())) {
            model.addAttribute("loginData", loginData);
            model.addAttribute("invalidCredentials", true);
            return "auth/login";
        }

        //Store user in session
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
