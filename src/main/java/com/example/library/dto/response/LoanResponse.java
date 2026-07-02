package com.example.library.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class LoanResponse {
    private Long id;
    private Long borrowerId;
    private Long branchId;
    private LocalDateTime loanDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnedDate;
    private BigDecimal cost;
    private String status;
    private Long createdBy;
    private Long returnedBy;
    private List<LoanItemResponse> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDateTime returnedDate) {
        this.returnedDate = returnedDate;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getReturnedBy() {
        return returnedBy;
    }

    public void setReturnedBy(Long returnedBy) {
        this.returnedBy = returnedBy;
    }

    public List<LoanItemResponse> getItems() {
        return items;
    }

    public void setItems(List<LoanItemResponse> items) {
        this.items = items;
    }
}
