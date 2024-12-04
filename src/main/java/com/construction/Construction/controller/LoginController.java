package com.construction.Construction.controller;

import com.construction.Construction.entities.Login;
import com.construction.Construction.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    @RequestMapping("/login")
    public String showLoginForm() {
        return "backend/login";
    }

    @PostMapping("/login")
    public String login(String email, String password, Model model, HttpSession session) {
        Login admin = loginRepository.findByEmail(email);
        if (admin != null && admin.getPassword().equals(password)) {
            session.setAttribute("admin", admin); // Store admin info in session
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid email or password"); // Add error message
        return "backend/login"; // Redirect back to login page
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/login"; // Redirect to login page
    }
}
