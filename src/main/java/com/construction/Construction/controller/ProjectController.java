package com.construction.Construction.controller;

import com.construction.Construction.entities.AddProject;
import com.construction.Construction.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;



    @GetMapping("/")  // Map the root URL to index.html
    public String getAllProjects(Model model) {
        List<AddProject> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);  // Add projects list to the model
        return "index"; // This will return the index.html view
    }

    @GetMapping("/index")  // Map the /index URL to index.html
    public String getAllProject(Model model) {
        List<AddProject> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);  // Add projects list to the model
        return "index"; // This will return the index.html view
    }

    @GetMapping("/completedprojects")  // Map to show completed projects
    public String getCompletedProjects(Model model) {
        // Fetch projects where status is 'Completed'
        List<AddProject> completedProjects = projectRepository.findByStatus("Completed");
        model.addAttribute("projects", completedProjects);  // Add completed projects to the model
        return "completedprojects";  // Return the completedprojects.html view
    }
    @GetMapping("/ongoingprojects")  // Map to show completed projects
    public String getOngoingProjects(Model model) {
        // Fetch projects where status is 'Completed'
        List<AddProject> ongoingProjects = projectRepository.findByStatus("Ongoing");
        model.addAttribute("projects", ongoingProjects);  // Add completed projects to the model
        return "ongoingprojects";  // Return the completedprojects.html view
    }
    @GetMapping("/projects-single")
    public String getProjectDetails(@RequestParam("url") String url, Model model) {
        // Fetch project by URL
        AddProject project = projectRepository.findByUrl(url);
        model.addAttribute("project", project);  // Add the project to the model
        return "projects-single";  // Return the projects-single.html view
    }
    @GetMapping("/projects-single2")
    public String getProjectDetails2(@RequestParam("url") String url, Model model) {
        // Fetch project by URL
        AddProject project = projectRepository.findByUrl(url);
        model.addAttribute("project", project);  // Add the project to the model
        return "projects-single2";  // Return the projects-single.html view
    }
    @GetMapping("/viewprojects")
    public String viewProjects(Model model) {
        List<AddProject> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);  // Add projects list to the model
        return "backend/viewprojects";  // Return the viewprojects.html view
    }
    @GetMapping("/viewprdelete/{id}")
    public String deleteProject(@PathVariable("id") int id) {
        Optional<AddProject> galleryOptional = projectRepository.findById(id);
        if (galleryOptional.isPresent()) {
            projectRepository.deleteById(id);
        }
        return "redirect:/viewprojects";
    }

    @PostMapping("/add")
    public String addProject(
            @RequestParam("status") String status,
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile image,
            @RequestParam("map") MultipartFile map,
            @RequestParam("fplan") MultipartFile fplan,
            @RequestParam("location") String location,
            @RequestParam("details") String details,
            Model model) {
        try {
            // Save files and get their relative paths
            String imagePath = saveFile(image, "static/images");
            String mapPath = saveFile(map, "static/images");
            String fplanPath = saveFile(fplan, "static/images");

            // Generate the URL-friendly name
            String urlName = name.replace(" ", "-").toLowerCase()
                    .replaceAll("[^a-z0-9-]", "") // Remove non-alphanumeric characters
                    .replaceAll("-+", "-");      // Remove duplicate hyphens

            // Create AddProject entity and save to database
            AddProject project = new AddProject();
            project.setStatus(status);
            project.setName(name);
            project.setImage(imagePath);
            project.setMap(mapPath);
            project.setFplan(fplanPath);
            project.setLocation(location);
            project.setDetails(details);
            project.setUrl(urlName);

            projectRepository.save(project);
            model.addAttribute("message", "Project added successfully!");
        } catch (IOException e) {
            model.addAttribute("message", "Error saving project: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/addproject"; // Redirect to the project list page
    }

    private String saveFile(MultipartFile file, String directory) throws IOException {
        if (!file.isEmpty()) {
            // Dynamically resolve the directory in the classpath
            File targetDir = new ClassPathResource(directory).getFile();
            if (!targetDir.exists()) {
                targetDir.mkdirs(); // Create the directory if it doesn't exist
            }

            // Build the file path
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(targetDir.getAbsolutePath() + File.separator + fileName);

            // Save the file
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Return the relative path for use in the database
            return fileName;
        }
        return null;
    }

    @GetMapping("/projectedit")
    public String showUpdateForm(@RequestParam("id") int id, Model model) {
        Optional<AddProject> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            model.addAttribute("project", projectOptional.get());
            return "backend/projectedit"; // Return the update project form view
        } else {
            model.addAttribute("message", "Project not found!");
            return "redirect:/viewprojects"; // Redirect to project list if project doesn't exist
        }
    }

    // POST method to update project details
    @PostMapping("/updateproject")
    public String updateProject(
            @RequestParam("id") int id,
            @RequestParam("status") String status,
            @RequestParam("name") String name,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "map", required = false) MultipartFile map,
            @RequestParam(value = "fplan", required = false) MultipartFile fplan,
            @RequestParam("location") String location,
            @RequestParam("details") String details,
            Model model) {
        try {
            Optional<AddProject> projectOptional = projectRepository.findById(id);
            if (projectOptional.isPresent()) {
                AddProject project = projectOptional.get();

                // Update fields
                project.setStatus(status);
                project.setName(name);
                project.setLocation(location);
                project.setDetails(details);

                // Update files if new ones are uploaded
                if (image != null && !image.isEmpty()) {
                    String imagePath = saveFile2(image, "static/images");
                    project.setImage(imagePath);
                }
                if (map != null && !map.isEmpty()) {
                    String mapPath = saveFile2(map, "static/images");
                    project.setMap(mapPath);
                }
                if (fplan != null && !fplan.isEmpty()) {
                    String fplanPath = saveFile2(fplan, "static/images");
                    project.setFplan(fplanPath);
                }

                // Generate the updated URL-friendly name
                String urlName = name.replace(" ", "-").toLowerCase()
                        .replaceAll("[^a-z0-9-]", "") // Remove non-alphanumeric characters
                        .replaceAll("-+", "-");      // Remove duplicate hyphens
                project.setUrl(urlName);

                // Save updated project to database
                projectRepository.save(project);

                model.addAttribute("message", "Project updated successfully!");
                return "redirect:/viewprojects";
            } else {
                model.addAttribute("message", "Project not found!");
                return "redirect:/viewprojects";
            }
        } catch (IOException e) {
            model.addAttribute("message", "Error updating project: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/viewprojects";
        }
    }

    private String saveFile2(MultipartFile file, String directory) throws IOException {
        if (!file.isEmpty()) {
            File targetDir = new ClassPathResource(directory).getFile();
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            Path path = Paths.get(targetDir.getAbsolutePath() + File.separator + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }
        return null;
    }


}
