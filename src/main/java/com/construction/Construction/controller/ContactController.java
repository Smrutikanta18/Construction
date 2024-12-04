package com.construction.Construction.controller;

import com.construction.Construction.entities.ContactForm;
import com.construction.Construction.repository.ContactRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "contact";  // Return contact.html
    }

    @PostMapping("/contact")
    public String submitContactForm(ContactForm contactForm) {

        String submittedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        contactForm.setSubmittedAt(submittedAt);

        contactRepository.save(contactForm);

        return "redirect:/contact";
    }

    @GetMapping("/inquiry")
    public String showInquiries(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        List<ContactForm> inquiries = contactRepository.findAll();
        model.addAttribute("inquiries", inquiries);
        return "backend/inquiry"; // Return inquiries.html
    }

    @GetMapping("/deleteInquiry")
    public String deleteInquiry(@RequestParam("id") int id) {
        contactRepository.deleteById(id);
        return "redirect:/inquiry";
    }
}
