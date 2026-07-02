package com.example.library.dto.response;

public class LoanItemResponse {
    private Long bookId;
    private String title;
    private int quantity;

    public LoanItemResponse() {}

    public LoanItemResponse(Long bookId, String title, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.quantity = quantity;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
