package com.construction.Construction.controller;

import com.construction.Construction.entities.Gallery;
import com.construction.Construction.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gallery")
public class GalleryController {

    @Autowired
    private GalleryRepository galleryRepository;

    @GetMapping
    public String showGallery(Model model) {
        List<Gallery> galleryList = galleryRepository.findAll();
        model.addAttribute("galleryList", galleryList);
        return "gallery"; // gallery.html
    }


}
