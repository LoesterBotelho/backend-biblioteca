package com.example.library.service.impl;

import com.example.library.dto.request.AuthorRequest;
import com.example.library.dto.request.BookRequest;
import com.example.library.dto.request.InventoryRequest;
import com.example.library.dto.response.AuthorResponse;
import com.example.library.dto.response.BookResponse;
import com.example.library.dto.response.InventoryResponse;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Branch;
import com.example.library.entity.InventoryItem;
import com.example.library.exception.BusinessException;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BranchRepository;
import com.example.library.repository.InventoryItemRepository;
import com.example.library.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BranchRepository branchRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public AdminServiceImpl(AuthorRepository authorRepository,
                            BookRepository bookRepository,
                            BranchRepository branchRepository,
                            InventoryItemRepository inventoryItemRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.branchRepository = branchRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Override
    public AuthorResponse createAuthor(AuthorRequest request) {
        Author author = new Author(request.getName(), request.getBiography());
        author = authorRepository.save(author);
        return new AuthorResponse(author.getId(), author.getName(), author.getBiography());
    }

    @Override
    public BookResponse createBook(BookRequest request) {
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new BusinessException("Author not found"));
        Book book = new Book(request.getTitle(), request.getIsbn(), request.getSummary(), request.getPrice(), author);
        book = bookRepository.save(book);
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setIsbn(book.getIsbn());
        response.setSummary(book.getSummary());
        response.setPrice(book.getPrice());
        response.setAvailable(book.isAvailable());
        response.setAuthorName(author.getName());
        return response;
    }

    @Override
    public InventoryResponse adjustStock(InventoryRequest request) {
        if (request.getQuantity() < 0) {
            throw new BusinessException("Quantity adjustment must be non-negative");
        }
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new BusinessException("Book not found"));
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new BusinessException("Branch not found"));
        InventoryItem inventory = inventoryItemRepository.findByBookIdAndBranchId(request.getBookId(), request.getBranchId())
                .orElseGet(() -> new InventoryItem(book, branch, 0, 0));
        inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
        inventory = inventoryItemRepository.save(inventory);
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        response.setBookId(inventory.getBook().getId());
        response.setBranchId(inventory.getBranch().getId());
        response.setQuantity(inventory.getQuantity());
        response.setUnusableQuantity(inventory.getUnusableQuantity());
        return response;
    }

    @Override
    public void markBookUnusable(Long bookId, Long branchId, int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }
        InventoryItem inventory = inventoryItemRepository.findByBookIdAndBranchId(bookId, branchId)
                .orElseThrow(() -> new BusinessException("Inventory record not found"));
        if (inventory.getQuantity() < quantity) {
            throw new BusinessException("Not enough usable stock to mark unusable");
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setUnusableQuantity(inventory.getUnusableQuantity() + quantity);
        inventoryItemRepository.save(inventory);
    }

    @Override
    public List<AuthorResponse> listAuthors() {
        return authorRepository.findAll().stream()
                .map(author -> new AuthorResponse(author.getId(), author.getName(), author.getBiography()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> listBooks() {
        return bookRepository.findAll().stream()
                .map(book -> {
                    BookResponse response = new BookResponse();
                    response.setId(book.getId());
                    response.setTitle(book.getTitle());
                    response.setIsbn(book.getIsbn());
                    response.setSummary(book.getSummary());
                    response.setPrice(book.getPrice());
                    response.setAvailable(book.isAvailable());
                    response.setAuthorName(book.getAuthor() != null ? book.getAuthor().getName() : null);
                    return response;
                })
                .collect(Collectors.toList());
    }
}
