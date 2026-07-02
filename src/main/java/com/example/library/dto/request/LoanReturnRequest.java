package com.example.library.dto.request;

import jakarta.validation.constraints.NotNull;

public class LoanReturnRequest {
    @NotNull
    private Long loanId;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }
}
