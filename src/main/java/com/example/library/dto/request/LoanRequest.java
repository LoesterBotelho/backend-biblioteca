package com.example.library.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class LoanRequest {
    @NotNull
    private Long borrowerId;
    @NotNull
    private Long branchId;
    @Min(7)
    private int loanDays;
    @NotNull
    private Boolean paid;
    @NotEmpty
    @Valid
    private List<LoanItemRequest> items;

    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(int loanDays) {
        this.loanDays = loanDays;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public List<LoanItemRequest> getItems() {
        return items;
    }

    public void setItems(List<LoanItemRequest> items) {
        this.items = items;
    }
}
