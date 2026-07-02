package com.example.library.dto.response;

public class InventoryResponse {
    private Long id;
    private Long bookId;
    private Long branchId;
    private int quantity;
    private int unusableQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnusableQuantity() {
        return unusableQuantity;
    }

    public void setUnusableQuantity(int unusableQuantity) {
        this.unusableQuantity = unusableQuantity;
    }
}
