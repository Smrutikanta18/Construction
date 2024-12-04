package com.construction.Construction.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @RequestMapping
    public String dashboard(HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login"; // Redirect to login if not logged in
        }
        return "backend/dashboard"; // Show dashboard page
    }
}
