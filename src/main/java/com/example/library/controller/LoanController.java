package com.example.library.controller;

import com.example.library.dto.request.LoanRequest;
import com.example.library.dto.request.LoanReturnRequest;
import com.example.library.dto.response.LoanResponse;
import com.example.library.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanRequest request, Authentication authentication) {
        return ResponseEntity.ok(loanService.createLoan(request, authentication.getName()));
    }

    @PostMapping("/return")
    public ResponseEntity<LoanResponse> returnLoan(@Valid @RequestBody LoanReturnRequest request, Authentication authentication) {
        return ResponseEntity.ok(loanService.returnLoan(request, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> listLoans() {
        return ResponseEntity.ok(loanService.listLoans());
    }
}
