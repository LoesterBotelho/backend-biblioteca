package com.example.library.service;

import com.example.library.dto.request.AuthorRequest;
import com.example.library.dto.request.BookRequest;
import com.example.library.dto.request.InventoryRequest;
import com.example.library.dto.response.AuthorResponse;
import com.example.library.dto.response.BookResponse;
import com.example.library.dto.response.InventoryResponse;

import java.util.List;

public interface AdminService {
    AuthorResponse createAuthor(AuthorRequest request);
    BookResponse createBook(BookRequest request);
    InventoryResponse adjustStock(InventoryRequest request);
    void markBookUnusable(Long bookId, Long branchId, int quantity);
    List<AuthorResponse> listAuthors();
    List<BookResponse> listBooks();
}
