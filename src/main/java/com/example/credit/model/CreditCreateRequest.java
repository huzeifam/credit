package com.example.credit.model;

public class CreditCreateRequest {

    private Integer accountNo;
    private Double creditAmount;



    public CreditCreateRequest(Integer accountNo, Double creditAmount) {
        this.accountNo = accountNo;
        this.creditAmount = creditAmount;


    }

    public Integer getAccountNo() {
        return accountNo;
    }

    public Double getCreditAmount() {
        return creditAmount;
    }




}
