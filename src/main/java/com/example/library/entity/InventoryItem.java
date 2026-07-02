package com.example.library.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_item")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int unusableQuantity;

    public InventoryItem() {}

    public InventoryItem(Book book, Branch branch, int quantity, int unusableQuantity) {
        this.book = book;
        this.branch = branch;
        this.quantity = quantity;
        this.unusableQuantity = unusableQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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
