package com.enigmacamp.Loan.Management.System.service.loan;

import com.enigmacamp.Loan.Management.System.dto.request.LoanRequest;
import com.enigmacamp.Loan.Management.System.dto.request.LoanStatusUpdateRequest;
import com.enigmacamp.Loan.Management.System.dto.response.LoanResponse;
import com.enigmacamp.Loan.Management.System.entities.CustomerProfile;
import com.enigmacamp.Loan.Management.System.entities.Loan;
import com.enigmacamp.Loan.Management.System.entities.User;
import com.enigmacamp.Loan.Management.System.exception.BadRequestException;
import com.enigmacamp.Loan.Management.System.exception.ResourceNotFoundException;
import com.enigmacamp.Loan.Management.System.exception.UnauthorizedException;
import com.enigmacamp.Loan.Management.System.repository.CustomerProfileRepository;
import com.enigmacamp.Loan.Management.System.repository.LoanRepository;
import com.enigmacamp.Loan.Management.System.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.ResourceClosedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final CustomerProfileRepository customerProfileRepository;

    @Override
    @Transactional
    public LoanResponse applyLoan(String username, LoanRequest request) {
        // 1. Find user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Find customer profile
        CustomerProfile customer = customerProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        // 3. Business validation: Check if customer has uploaded document
        if(customer.getKtpPath() == null || customer.getSalarySlipPath() == null){
            throw new BadRequestException("Please upload KTP and Salary Slip before applying for loan");
        }

        // 4. Create Loan entity
        Loan loan = Loan.builder()
                .customer(customer)
                .amount(request.amount())
                .tenorMonths(request.tenorMonth())
                .interestRate(request.interestRate())
                .status(Loan.LoanStatus.PENDING)
                .build();

        // 5. Save loan
        Loan savedLoan = loanRepository.save(loan);

        // 6. Return response
        return mapToResponse(savedLoan);
    }

    @Override
    public List<LoanResponse> getMyLoans(String username) {
        // 1. Find user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Find customer profile
        CustomerProfile customer = customerProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        // 3. Get all loans for this customer
        List<Loan> myLoans = loanRepository.findByCustomer(customer);

        // 4. Convert to response
        return myLoans.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public LoanResponse getMyLoanById(String username, UUID loanId) {
        // 1. Find user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Find customer profile
        CustomerProfile customer = customerProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        // 3. Find loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        // 4. Security check: loan harus milik customer ini
        if(!loan.getCustomer().getId().equals(customer.getId())){
            throw new UnauthorizedException("You don't have permission to view this loan");
        }

        // 5. Return response
        return mapToResponse(loan);
    }

    // =============== Admin ================

    @Override
    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LoanResponse> getLoansByStatus(Loan.LoanStatus status) {
        return loanRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LoanResponse updateLoanStatus(UUID loanId, LoanStatusUpdateRequest request) {
        // 1. Find Loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceClosedException("Loan not found")]);

        // 2. Business validation: tidak boleh update loan yang approved atau rejected
        if(loan.getStatus() != Loan.LoanStatus.PENDING){
            throw new RuntimeException("Cannot update status of non-pending loan");
        }

        // 3. Update loan status
        loan.setStatus(request.status());

        Loan updatedLoan = loanRepository.save(loan);

        return mapToResponse(updatedLoan);
    }

    @Override
    @Transactional
    public void deleteLoan(UUID loanId) {
        if(!loanRepository.existsById(loanId)){
            throw new ResourceNotFoundException("Loan not found");
        }

        loanRepository.deleteById(loanId);
    }

    private LoanResponse mapToResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getCustomer().getId(),
                loan.getCustomer().getName(),
                loan.getCustomer().getEmail(),
                loan.getAmount(),
                loan.getTenorMonths(),
                loan.getInterestRate(),
                loan.getStatus(),
                loan.getCreatedAt(),
                loan.getUpdatedAt()
        );
    }
}
