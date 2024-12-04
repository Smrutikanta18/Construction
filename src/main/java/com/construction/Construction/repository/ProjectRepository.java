package com.construction.Construction.repository;

import com.construction.Construction.entities.AddProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<AddProject,Integer> {
    List<AddProject> findByStatus(String status);
    AddProject findByUrl(String url);

}
