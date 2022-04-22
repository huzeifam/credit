package com.example.credit.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "credits")
public class CreditResponse {
    @Id
    private Integer creditNo;
    private Integer accountNo;
    private Double creditAmount;
    private Integer periodInMonth;
    private Double interestRate;

    private Double interest;

    private Double totalRepayment;

    private Double remainingRepayment;
    private Double rates;
    private LocalDate startDate;

    public CreditResponse(){

    }

    public CreditResponse(Integer creditNo, Integer accountNo, Double creditAmount, Integer periodInMonth, Double interestRate, Double interest, Double totalRepayment, Double remainingRepayment, Double rates, LocalDate startDate) {
        this.creditNo = creditNo;
        this.accountNo = accountNo;
        this.creditAmount = creditAmount;
        this.periodInMonth = periodInMonth;
        this.interestRate = interestRate;
        this.interest = interest;
        this.totalRepayment = totalRepayment;
        this.remainingRepayment = remainingRepayment;
        this.rates = rates;
        this.startDate = startDate;
    }

    public Integer getCreditNo() {
        return creditNo;
    }

    public Integer getAccountNo() {
        return accountNo;
    }

    public Double getCreditAmount() {
        return Math.round(creditAmount*100.0)/100.0;
    }

    public Integer getPeriodInMonth() {
        return periodInMonth;
    }

    public Double getInterestRate(){
        return Math.round(interestRate*100.0)/100.0;
    }

    public Double getInterest() {
        return Math.round(interest*100.0)/100.0;
    }

    public Double getTotalRepayment() {
        return Math.round(totalRepayment*100.0)/100.0;
    }

    public Double getRemainingRepayment() {
        return Math.round(remainingRepayment*100.0)/100.0;
    }

    public Double getRates() {
        return Math.round(rates*100.0)/100.0;
    }

    public LocalDate getStartDate() {
        return startDate;
    }


    public void setRemainingRepayment(Double remainingRepayment) {
        this.remainingRepayment = remainingRepayment;
    }
}
