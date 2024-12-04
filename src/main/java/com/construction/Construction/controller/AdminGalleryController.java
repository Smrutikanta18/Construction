package com.construction.Construction.controller;

import com.construction.Construction.entities.Gallery;
import com.construction.Construction.repository.GalleryRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminGalleryController {


    @Autowired
    private GalleryRepository galleryRepository;

    @GetMapping("/gallerymanage")
    public String showGallery(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        List<Gallery> galleryList = galleryRepository.findAll();
        model.addAttribute("galleryList", galleryList);
        return "backend/gallerymanage"; // gallerymanage.html
    }

    @PostMapping("/add1")
    public String addGallery(@RequestParam("image") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Please select an image to upload.");
            return "redirect:/gallerymanage";
        }

        try {
            // Resolve the 'static/images' directory dynamically
            File staticDir = new ClassPathResource("static/images/").getFile();

            // Ensure the directory exists
            if (!staticDir.exists()) {
                staticDir.mkdirs();
            }

            // Create the full path for the file
            Path path = Paths.get(staticDir.getAbsolutePath() + File.separator + file.getOriginalFilename());

            // Copy the file to the directory
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Save the image path or filename to the database
            Gallery gallery = new Gallery();
            gallery.setPhoto(file.getOriginalFilename()); // Save relative path for easier access
            galleryRepository.save(gallery);

            model.addAttribute("message", "Image added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error occurred while uploading the image.");
            return "redirect:/gallerymanage";
        }

        return "redirect:/gallerymanage";
    }


    @GetMapping("/delete/{id}")
    public String deleteGallery(@PathVariable("id") int id) {
        Optional<Gallery> galleryOptional = galleryRepository.findById(id);
        if (galleryOptional.isPresent()) {
            galleryRepository.deleteById(id);
        }
        return "redirect:/gallerymanage";
    }

    @GetMapping("/updateg")
    public String showUpdateGalleryForm(@RequestParam("id") int id, Model model,HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        Optional<Gallery> galleryOptional = galleryRepository.findById(id);
        if (galleryOptional.isPresent()) {
            model.addAttribute("gallery", galleryOptional.get());
            return "backend/updateg"; // updateg.html
        }
        return "redirect:/gallerymanage";
    }


    @PostMapping("/update/{id}")
    public String updateGallery(@PathVariable("id") int id, @RequestParam("photo") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Please select an image to upload.");
            return "redirect:/gallerymanage";
        }

        // Fetch the gallery object from the database
        Optional<Gallery> galleryOptional = galleryRepository.findById(id);
        if (galleryOptional.isPresent()) {
            try {
                Gallery gallery = galleryOptional.get();

                // Dynamically resolve the 'static/images' directory
                File staticDir = new ClassPathResource("static/images/").getFile();

                // Ensure the directory exists
                if (!staticDir.exists()) {
                    staticDir.mkdirs();
                }

                // Build the new file path for the updated image
                Path newImagePath = Paths.get(staticDir.getAbsolutePath() + File.separator + file.getOriginalFilename());

                // Save the new image file
                Files.copy(file.getInputStream(), newImagePath, StandardCopyOption.REPLACE_EXISTING);

                // Update the gallery record with the new image path
                gallery.setPhoto(file.getOriginalFilename()); // Save the relative path
                galleryRepository.save(gallery);

                model.addAttribute("message", "Image updated successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Error occurred while updating the image.");
                return "redirect:/gallerymanage";
            }
        } else {
            model.addAttribute("error", "Gallery record not found.");
            return "redirect:/gallerymanage";
        }

        return "redirect:/gallerymanage";
    }

}

