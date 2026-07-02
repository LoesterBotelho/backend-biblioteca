package com.example.library.service;

import com.example.library.dto.request.LoanRequest;
import com.example.library.dto.request.LoanReturnRequest;
import com.example.library.dto.response.LoanResponse;

import java.util.List;

public interface LoanService {
    LoanResponse createLoan(LoanRequest request, String operatorUsername);
    LoanResponse returnLoan(LoanReturnRequest request, String operatorUsername);
    List<LoanResponse> listLoans();
}
