package com.example.credit.repository;


import com.example.credit.model.CreditResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends JpaRepository<CreditResponse, Integer> {
}
