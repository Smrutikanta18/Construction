package com.construction.Construction.repository;

import com.construction.Construction.entities.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login , Integer> {
    Login findByEmail(String email);
}
