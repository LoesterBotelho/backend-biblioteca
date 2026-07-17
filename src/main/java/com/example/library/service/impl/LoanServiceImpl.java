package com.example.library.service.impl;

import com.example.library.dto.request.LoanItemRequest;
import com.example.library.dto.request.LoanRequest;
import com.example.library.dto.request.LoanReturnRequest;
import com.example.library.dto.response.LoanItemResponse;
import com.example.library.dto.response.LoanResponse;
import com.example.library.entity.AppUser;
import com.example.library.entity.Book;
import com.example.library.entity.InventoryItem;
import com.example.library.entity.Loan;
import com.example.library.entity.LoanItem;
import com.example.library.exception.BusinessException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.AuditEntryRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BranchRepository;
import com.example.library.repository.InventoryItemRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.LoanService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BranchRepository branchRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final AuditEntryRepository auditEntryRepository;

    private final int maxBooks;
    private final int minDays;
    private final int maxDays;


    public LoanServiceImpl(
            LoanRepository loanRepository,
            UserRepository userRepository,
            BookRepository bookRepository,
            BranchRepository branchRepository,
            InventoryItemRepository inventoryItemRepository,
            AuditEntryRepository auditEntryRepository,
            @Value("${library.loan.max-books}") int maxBooks,
            @Value("${library.loan.min-days}") int minDays,
            @Value("${library.loan.max-days}") int maxDays) {

        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.branchRepository = branchRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.auditEntryRepository = auditEntryRepository;

        this.maxBooks = maxBooks;
        this.minDays = minDays;
        this.maxDays = maxDays;
    }


    @Override
    @Transactional
    public LoanResponse createLoan(
            LoanRequest request,
            String operatorUsername) {


        AppUser borrower = userRepository.findById(request.getBorrowerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Borrower not found"));


        com.example.library.entity.Branch branch =
                branchRepository.findById(request.getBranchId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch not found"));


        int totalBooks = request.getItems()
                .stream()
                .mapToInt(LoanItemRequest::getQuantity)
                .sum();


        if (totalBooks > maxBooks) {
            throw new BusinessException(
                    "Maximum " + maxBooks + " books per loan is allowed");
        }


        if (request.getLoanDays() < minDays ||
                request.getLoanDays() > maxDays) {

            throw new BusinessException(
                    "Loan days must be between "
                    + minDays + " and " + maxDays);
        }


        AppUser operator = userRepository
                .findByUsername(operatorUsername)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Operator not found"));



        Loan loan = new Loan();

        loan.setBorrower(borrower);
        loan.setBranch(branch);
        loan.setLoanDate(LocalDateTime.now());
        loan.setDueDate(
                LocalDateTime.now()
                        .plusDays(request.getLoanDays()));

        loan.setStatus("ACTIVE");
        loan.setCreatedBy(operator);

        loan.setCost(
                request.getPaid()
                        ? calculateCost(request)
                        : BigDecimal.ZERO);



        for (LoanItemRequest itemRequest : request.getItems()) {


            Book book = bookRepository.findById(itemRequest.getBookId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Book not found"));


            InventoryItem inventory =
                    inventoryItemRepository
                            .findByBookIdAndBranchId(
                                    book.getId(),
                                    branch.getId())

                    .orElseThrow(() ->
                            new BusinessException(
                                    "Book is not available at this branch"));



            if (inventory.getQuantity()
                    < itemRequest.getQuantity()) {

                throw new BusinessException(
                        "Not enough stock for book "
                        + book.getTitle());
            }



            inventory.setQuantity(
                    inventory.getQuantity()
                    - itemRequest.getQuantity());


            inventoryItemRepository.save(inventory);


            LoanItem item =
                    new LoanItem(
                            book,
                            itemRequest.getQuantity());


            loan.addItem(item);
        }



        Loan savedLoan =
                loanRepository.save(loan);


        saveAudit(
                operator,
                "CREATE_LOAN",
                "Created loan " + savedLoan.getId());


        return mapToResponse(savedLoan);
    }



    @Override
    @Transactional
    public LoanResponse returnLoan(
            LoanReturnRequest request,
            String operatorUsername) {


        Loan loan =
                loanRepository.findById(request.getLoanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Loan not found"));



        if (!"ACTIVE".equals(loan.getStatus())) {

            throw new BusinessException(
                    "Loan is not active");
        }



        AppUser operator =
                userRepository.findByUsername(operatorUsername)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Operator not found"));



        loan.setReturnedDate(LocalDateTime.now());
        loan.setReturnedBy(operator);
        loan.setStatus("RETURNED");



        loan.getItems().forEach(item -> {


            InventoryItem inventory =
                    inventoryItemRepository
                    .findByBookIdAndBranchId(
                            item.getBook().getId(),
                            loan.getBranch().getId())

                    .orElseGet(() ->
                            new InventoryItem(
                                    item.getBook(),
                                    loan.getBranch(),
                                    0,
                                    0));



            inventory.setQuantity(
                    inventory.getQuantity()
                    + item.getQuantity());


            inventoryItemRepository.save(inventory);

        });



        Loan savedLoan =
                loanRepository.save(loan);



        saveAudit(
                operator,
                "RETURN_LOAN",
                "Returned loan " + savedLoan.getId());


        return mapToResponse(savedLoan);
    }



    @Override
    @Transactional(readOnly = true)
    public List<LoanResponse> listLoans() {


        return loanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }



    private void saveAudit(
            AppUser operator,
            String action,
            String description) {


        com.example.library.entity.AuditEntry auditEntry =
                new com.example.library.entity.AuditEntry(
                        action,
                        description,
                        operator,
                        LocalDateTime.now());


        auditEntryRepository.save(auditEntry);
    }



    private BigDecimal calculateCost(
            LoanRequest request) {


        return request.getItems()
                .stream()

                .map(item ->
                        bookRepository.findById(item.getBookId())

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Book not found"))

                        .getPrice()

                        .multiply(
                                BigDecimal.valueOf(
                                        item.getQuantity())))

                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add);
    }



    private LoanResponse mapToResponse(
            Loan loan) {


        LoanResponse response =
                new LoanResponse();


        response.setId(loan.getId());


        response.setBorrowerId(
                loan.getBorrower().getId());


        response.setBranchId(
                loan.getBranch().getId());


        response.setLoanDate(
                loan.getLoanDate());


        response.setDueDate(
                loan.getDueDate());


        response.setReturnedDate(
                loan.getReturnedDate());


        response.setCost(
                loan.getCost());


        response.setStatus(
                loan.getStatus());


        response.setCreatedBy(
                loan.getCreatedBy().getId());


        response.setReturnedBy(
                loan.getReturnedBy() != null
                        ? loan.getReturnedBy().getId()
                        : null);



        response.setItems(
                loan.getItems()
                .stream()

                .map(item ->
                        new LoanItemResponse(
                                item.getBook().getId(),
                                item.getBook().getTitle(),
                                item.getQuantity()))

                .collect(Collectors.toList()));



        return response;
    }
}