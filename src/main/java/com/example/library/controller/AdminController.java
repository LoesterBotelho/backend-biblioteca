package com.example.library.controller;

import com.example.library.dto.request.AuthorRequest;
import com.example.library.dto.request.BookRequest;
import com.example.library.dto.request.InventoryRequest;
import com.example.library.dto.response.AuthorResponse;
import com.example.library.dto.response.BookResponse;
import com.example.library.dto.response.InventoryResponse;
import com.example.library.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/authors")
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(adminService.createAuthor(request));
    }

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorResponse>> listAuthors() {
        return ResponseEntity.ok(adminService.listAuthors());
    }

    @PostMapping("/books")
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(adminService.createBook(request));
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookResponse>> listBooks() {
        return ResponseEntity.ok(adminService.listBooks());
    }

    @PostMapping("/inventory")
    public ResponseEntity<InventoryResponse> adjustStock(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(adminService.adjustStock(request));
    }

    @PostMapping("/inventory/unusable")
    public ResponseEntity<Void> markBookUnusable(@RequestParam Long bookId, @RequestParam Long branchId, @RequestParam int quantity) {
        adminService.markBookUnusable(bookId, branchId, quantity);
        return ResponseEntity.ok().build();
    }
}
