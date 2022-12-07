package com.example.hospitalreview.repository;

import com.example.hospitalreview.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
}
