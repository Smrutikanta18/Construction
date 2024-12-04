package com.construction.Construction.repository;

import com.construction.Construction.entities.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactForm, Integer> {
}
