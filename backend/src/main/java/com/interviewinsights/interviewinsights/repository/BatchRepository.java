package com.interviewinsights.interviewinsights.repository;

import com.interviewinsights.interviewinsights.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;


//Repository for performing database operations on Batch entities.
public interface BatchRepository extends JpaRepository<Batch, Long> {
}
