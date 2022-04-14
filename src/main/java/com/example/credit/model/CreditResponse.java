package com.example.credit.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    private LocalDateTime startDate;

    public CreditResponse(){

    }

    public CreditResponse(Integer creditNo, Integer accountNo, Double creditAmount, Integer periodInMonth, Double interestRate, Double interest, LocalDateTime startDate) {
        this.creditNo = creditNo;
        this.accountNo = accountNo;
        this.creditAmount = creditAmount;
        this.periodInMonth = periodInMonth;
        this.interestRate = interestRate;
        this.interest = interest;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }



    public void setCreditAmount(Double kreditAmount) {
        this.creditAmount = kreditAmount;
    }


}
