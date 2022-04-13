package com.example.kredit.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "kredits")
public class KreditResponse {
    @Id
    private Integer kreditNo;
    private Integer accountNo;
    private Double kreditAmount;
    private Integer interestRatePercentage;
    private Double interestRate;
    private LocalDateTime startDate;

    public KreditResponse(){

    }

    public KreditResponse(Integer kreditNo, Integer accountNo, Double kreditAmount, Integer interestRatePercentage, Double interestRate, LocalDateTime startDate) {
        this.kreditNo = kreditNo;
        this.accountNo = accountNo;
        this.kreditAmount = kreditAmount;
        this.interestRatePercentage = interestRatePercentage;
        this.interestRate = interestRate;
        this.startDate = startDate;
    }

    public Integer getKreditNo() {
        return kreditNo;
    }

    public Integer getAccountNo() {
        return accountNo;
    }

    public Double getKreditAmount() {
        return Math.round(kreditAmount*100.0)/100.0;
    }

    public Integer getInterestRatePercentage(){
        return interestRatePercentage;
    }

    public Double getInterestRate() {
        return Math.round(interestRate*100.0)/100.0;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }



    public void setKreditAmount(Double kreditAmount) {
        this.kreditAmount = kreditAmount;
    }


}
