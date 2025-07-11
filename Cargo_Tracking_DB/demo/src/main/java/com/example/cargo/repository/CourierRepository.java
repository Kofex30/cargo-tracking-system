package com.example.cargo.repository;

import com.example.cargo.model.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourierRepository extends JpaRepository<Courier, Long> {
    // findById zaten JpaRepository'de var, tekrar yazmana gerek yok.
    Optional<Courier> findById(Long id);
    Courier findByUsername(String username);
    Optional<Courier> findByName(String name);


}
