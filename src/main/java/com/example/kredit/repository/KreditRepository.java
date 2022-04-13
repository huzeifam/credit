package com.example.kredit.repository;


import com.example.kredit.model.KreditResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KreditRepository extends JpaRepository<KreditResponse, Integer> {
}
