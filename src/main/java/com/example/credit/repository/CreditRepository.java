package com.example.credit.repository;


import com.example.credit.model.CreditResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<CreditResponse, Integer> {

    @Query("select c from CreditResponse c where c.accountNo = ?1")
    List<CreditResponse> findCreditByAccountNo(Integer accountNo);


    @Transactional
    @Modifying
    @Query("DELETE FROM CreditResponse c WHERE c.accountNo = ?1")
    void deleteCreditByAccountNo(Integer accountNo);
}
