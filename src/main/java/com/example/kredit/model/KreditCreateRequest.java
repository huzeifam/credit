package com.example.kredit.model;

public class KreditCreateRequest {

    private Integer accountNo;
    private Double kreditAmount;
    private Integer interestRatePercentage;

    public KreditCreateRequest(Integer accountNo, Double kreditAmount, Integer interestRatePercentage) {
        this.accountNo = accountNo;
        this.kreditAmount = kreditAmount;
        this.interestRatePercentage = interestRatePercentage;
    }

    public Integer getAccountNo() {
        return accountNo;
    }

    public Double getKreditAmount() {
        return kreditAmount;
    }

    public Integer getInterestRatePercentage() {
        return interestRatePercentage;
    }
}
