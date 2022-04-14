package com.example.credit.model;

public enum CreditResponseEnum {
    SIX_MONTH(6),
    TWELVE_MONTH(12),
    EIGHTEEN_MONTH(18),
    TWENTY_FOUR_MONTH(24),
    THIRTY_MONTH(30),
    THIRTY_SIX_MONTH(36);

    private Integer periodInMonth;

    CreditResponseEnum(Integer periodInMonth) {
        this.periodInMonth = periodInMonth;
    }

    public Integer getPeriodInMonth() {
        return periodInMonth;
    }
}
